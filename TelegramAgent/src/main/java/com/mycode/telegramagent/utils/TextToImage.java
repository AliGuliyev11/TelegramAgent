package com.mycode.telegramagent.utils;

import com.mycode.telegramagent.dto.JasperDto;
import com.mycode.telegramagent.repositories.JasperMessageRepo;
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

public class TextToImage {

    @SneakyThrows
    public static void textToImage(JasperDto jasperDto, String languages, LocaleMessageService messageService,
                                   String location, String resourceFile, JasperMessageRepo jasperMessageRepo) {
        File file = ResourceUtils.getFile(resourceFile);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("money", getJasperMessage("jasper.price", languages, jasperMessageRepo, messageService));
        parameter.put("moneyIcon", "\uD83D\uDCB5");
        parameter.put("date", getJasperMessage("jasper.date", languages, jasperMessageRepo, messageService));
        parameter.put("dateIcon", "\uD83D\uDCC5");
        parameter.put("description", getJasperMessage("jasper.description", languages, jasperMessageRepo, messageService));
        if (jasperDto.getNote() != null) {
            parameter.put("note",getJasperMessage("jasper.note", languages, jasperMessageRepo, messageService));
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
