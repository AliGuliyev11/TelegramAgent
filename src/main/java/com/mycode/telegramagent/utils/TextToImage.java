package com.mycode.telegramagent.utils;

import com.mycode.telegramagent.dto.JasperDto;
import com.mycode.telegramagent.repositories.AgentMessageRepo;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;

import static com.mycode.telegramagent.utils.GetMessages.getJasperMessage;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote Class for convert text to jasper report
 */

public class TextToImage {

    /** This method for converting text to jasper report then convert it to image
     * @param jasperDto jasper DTO,see OfferToJasper class
     * @param languages user reuqest language
     * @param messageService message service which get message from locale bundle
     * @param location file location
     * @param resourceFile jasper resource file location
     * @param agentMessageRepo message entity
     * */

    @SneakyThrows
    public static void textToImage(JasperDto jasperDto, String languages, LocaleMessageService messageService,
                                   String location, String resourceFile, AgentMessageRepo agentMessageRepo) {
        File file = ResourceUtils.getFile(resourceFile);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("money", getJasperMessage("jasper.price", languages, agentMessageRepo, messageService));
        parameter.put("date", getJasperMessage("jasper.date", languages, agentMessageRepo, messageService));
        parameter.put("description", getJasperMessage("jasper.description", languages, agentMessageRepo, messageService));
        if (jasperDto.getNote() != null) {
            parameter.put("note",getJasperMessage("jasper.note", languages, agentMessageRepo, messageService));
        }
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(jasperDto));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);


        final float zoom = 1f;
        final String extension = "jpg";
        int pages = jasperPrint.getPages().size();
        for (int i = 0; i < pages; i++) {
            try (OutputStream out = new FileOutputStream(location)) {
                BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, i, zoom);
                ImageIO.write(image, extension, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
