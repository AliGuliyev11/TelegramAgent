package com.mycode.telegramagent.dao.Impl;

import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.dto.JasperDto;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.dto.RabbitOffer;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.rabbitmq.offerOrder.publisher.RabbitOfferService;
import com.mycode.telegramagent.repositories.AgentRepo;
import com.mycode.telegramagent.repositories.OfferRepo;
import com.mycode.telegramagent.repositories.OrderRepo;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;

import static com.mycode.telegramagent.utils.OfferToJasper.offerToJasper;

@Component
public class OfferImpl implements OfferDAO {

    private final OfferRepo offerRepo;
    private final RabbitOfferService offerService;
    private final AgentRepo agentRepo;
    private ModelMapper modelMapper = new ModelMapper();
    private final OrderRepo userRequest;

    public OfferImpl(OfferRepo offerRepo, RabbitOfferService offerService, AgentRepo agentRepo, OrderRepo userRequest) {
        this.offerRepo = offerRepo;
        this.offerService = offerService;
        this.agentRepo = agentRepo;
        this.userRequest = userRequest;
    }

    @SneakyThrows
    @Override
    public Offer saveOffer(String uuid, String email, OfferDto offerDto) {
        Agent agent = agentRepo.getAgentByEmail(email);
        UserRequest order = userRequest.getOrderByUserId(uuid,email);
        JasperDto jasperDto = offerToJasper(agent.getAgencyName(), offerDto);
        test(jasperDto);
        File photo = new File("src/main/resources/static/docs/offer.jpg");
        Offer offer = modelMapper.map(offerDto, Offer.class);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        offer.setAgent(agent);
        offer.setUserRequest(order);
        Offer savedOffer = offerRepo.save(offer);
        RabbitOffer rabbitOffer= RabbitOffer.builder().userId(uuid).offerId(savedOffer.getId()).file(photo).build();
        offerService.send(rabbitOffer);
        return savedOffer;
    }

    @SneakyThrows
    public void test(JasperDto jasperDto) {
        File file = ResourceUtils.getFile("src/main/resources/static/docs/offer.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("money", "Qiymət");
        parameter.put("moneyIcon", "\uD83D\uDCB5");
        parameter.put("date", "Tarix");
        parameter.put("dateIcon", "\uD83D\uDCC5");
        parameter.put("description", "Description");
        parameter.put("note", "Note");
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
