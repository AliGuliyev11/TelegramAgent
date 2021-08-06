package com.mycode.telegramagent;

import com.google.gson.Gson;
import com.mycode.telegramagent.models.JasperMessage;
import com.mycode.telegramagent.repositories.JasperMessageRepo;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static com.mycode.telegramagent.utils.GetMessages.getJasperMessage;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class LocaleTest {

    @Autowired
    JasperMessageRepo jasperMessageRepo;
    @Autowired
    LocaleMessageService localeMessageService;

    @Test
    void jasperPriceTest(){
        JasperMessage botMessage = new JasperMessage();
        Map<String, String> message = new HashMap<>();
        message.put("AZ", "Qiymət");
        message.put("EN", "Price");
        message.put("RU", "Цена");
        Gson gson = new Gson();
        String endingMessage = gson.toJson(message);
        botMessage.setMessage(endingMessage);
        botMessage.setKeyword("jasper.price");
        jasperMessageRepo.save(botMessage);
        String language = "AZ";
        String keyword = "jasper.price";
        String expected = "Qiymət";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language,jasperMessageRepo,localeMessageService));
    }

    @Test
    void jasperNoteTest(){
        JasperMessage botMessage = new JasperMessage();
        Map<String, String> message = new HashMap<>();
        message.put("AZ", "Qeyd");
        message.put("EN", "Note");
        message.put("RU", "Примечание");
        Gson gson = new Gson();
        String endingMessage = gson.toJson(message);
        botMessage.setMessage(endingMessage);
        botMessage.setKeyword("jasper.note");
        jasperMessageRepo.save(botMessage);
        String language = "AZ";
        String keyword = "jasper.note";
        String expected = "Qeyd";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language,jasperMessageRepo,localeMessageService));
    }

    @Test
    void jasperDescTest(){
        JasperMessage botMessage = new JasperMessage();
        Map<String, String> message = new HashMap<>();
        message.put("AZ", "Xarakteristika");
        message.put("EN", "Description");
        message.put("RU", "Описание");
        Gson gson = new Gson();
        String endingMessage = gson.toJson(message);
        botMessage.setMessage(endingMessage);
        botMessage.setKeyword("jasper.description");
        jasperMessageRepo.save(botMessage);
        String language = "EN";
        String keyword = "jasper.description";
        String expected = "Description";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language,jasperMessageRepo,localeMessageService));
    }

    @Test
    @Order(1)
    void jasperDateTest(){
        JasperMessage botMessage = new JasperMessage();
        Map<String, String> message = new HashMap<>();
        message.put("RU", "Дата");
        Gson gson = new Gson();
        String endingMessage = gson.toJson(message);
        botMessage.setMessage(endingMessage);
        botMessage.setKeyword("jasper.date");
        jasperMessageRepo.save(botMessage);
        String language = "RU";
        String keyword = "jasper.date";
        String expected = "Дата";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language,jasperMessageRepo,localeMessageService));
    }

    @Test
    void jasperDateLocaleTest(){
        String language = "AZ";
        String keyword = "jasper.date";
        String expected = "Date";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language,jasperMessageRepo,localeMessageService));
    }


}
