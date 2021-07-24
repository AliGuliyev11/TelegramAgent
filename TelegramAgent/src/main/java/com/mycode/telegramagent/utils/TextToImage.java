package com.mycode.telegramagent.utils;

import com.mycode.telegramagent.dto.JasperDto;
import com.mycode.telegramagent.enums.Languages;
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

public class TextToImage {

    @SneakyThrows
    public static void textToImage(JasperDto jasperDto, Languages languages, LocaleMessageService messageService,
                                   String location, String resourceFile) {
        File file = ResourceUtils.getFile(resourceFile);
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


        final float zoom = 1f;
        final String extension="jpg";
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
