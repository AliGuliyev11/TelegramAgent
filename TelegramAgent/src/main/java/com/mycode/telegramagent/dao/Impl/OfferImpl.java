package com.mycode.telegramagent.dao.Impl;

import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.dto.JasperDto;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.dto.RabbitOffer;
import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.Languages;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.rabbitmq.offerOrder.publisher.RabbitOfferService;
import com.mycode.telegramagent.repositories.AgentRepo;
import com.mycode.telegramagent.repositories.OfferRepo;
import com.mycode.telegramagent.repositories.OrderRepo;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.mycode.telegramagent.utils.OfferToJasper.offerToJasper;

@Component
public class OfferImpl implements OfferDAO {

    private final OfferRepo offerRepo;
    private final RabbitOfferService offerService;
    private final AgentRepo agentRepo;
    private ModelMapper modelMapper = new ModelMapper();
    private final OrderRepo userRequest;
    private final LocaleMessageService messageService;

    public OfferImpl(OfferRepo offerRepo, RabbitOfferService offerService, AgentRepo agentRepo, OrderRepo userRequest,
                     LocaleMessageService messageService) {
        this.offerRepo = offerRepo;
        this.offerService = offerService;
        this.agentRepo = agentRepo;
        this.userRequest = userRequest;
        this.messageService = messageService;
    }

    @SneakyThrows
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Offer saveOffer(String uuid, String email, OfferDto offerDto) {
        Agent agent = agentRepo.getAgentByEmail(email);
        UserRequest order = userRequest.getOrderByUserId(uuid, email);
        order.setAgentRequestStatus(AgentRequestStatus.Offer_Made);
        JasperDto jasperDto = offerToJasper(agent.getAgencyName(), offerDto);
        textToImage(jasperDto, order.getLanguage());
        File photo = new File("src/main/resources/static/docs/offer.jpg");


        Offer offer = modelMapper.map(offerDto, Offer.class);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        offer.setAgent(agent);
        offer.setUserRequest(order);
        Offer savedOffer = offerRepo.save(offer);
        RabbitOffer rabbitOffer = RabbitOffer.builder().userId(uuid).offerId(savedOffer.getId()).file(photo).build();
        offerService.send(rabbitOffer);
        order.setOffer(savedOffer);
        userRequest.save(order);
        return savedOffer;
    }

    @Override
    public void offerAccepted(ReplyToOffer replyToOffer) {
        Offer offer=offerRepo.findById(replyToOffer.getOfferId()).get();
        offer.setAcceptedDate(new Date());
        offer.setPhoneNumber(replyToOffer.getPhoneNumber());
        UserRequest userRequest=offer.getUserRequest();
        userRequest.setAgentRequestStatus(AgentRequestStatus.Accepted);
        offer.setUserRequest(userRequest);
        offerRepo.save(offer);
    }

    @Override
    public UserRequest getRequestByUUIDAndEmail(String uuid, String email) {
        return userRequest.getOrderByUserId(uuid, email);
    }

    @Override
    public List<Offer> getAgentOffers(String email) {
        return offerRepo.getOffersByAgent_Email(email);
    }

    @SneakyThrows
    public void textToImage(JasperDto jasperDto, Languages languages) {
        File file = ResourceUtils.getFile("src/main/resources/static/docs/offer.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("money", messageService.getMessage("jasper.price", languages));
        parameter.put("moneyIcon", "\uD83D\uDCB5");
        parameter.put("date", messageService.getMessage("jasper.date", languages));
        parameter.put("dateIcon", "\uD83D\uDCC5");
        parameter.put("description", messageService.getMessage("jasper.description", languages));
        if (jasperDto.getNote() != null) {
            parameter.put("note", messageService.getMessage("jasper.note", languages));
        }
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(jasperDto));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);

        final String extension = "jpg";
        final String location = "src/main/resources/static/docs/";
        final float zoom = 1f;
        String fileName = "offer";
        int pages = jasperPrint.getPages().size();
        for (int i = 0; i < pages; i++) {
            try (OutputStream out = new FileOutputStream(location + fileName + "." + extension)) {
                BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, i, zoom);
                ImageIO.write(image, extension, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Offer getOfferById(Long id) {
        return offerRepo.findById(id).get();
    }
}
