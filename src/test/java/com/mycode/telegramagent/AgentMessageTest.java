package com.mycode.telegramagent;

import com.google.gson.Gson;
import com.mycode.telegramagent.models.AgentMessage;
import com.mycode.telegramagent.repositories.AgentMessageRepo;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static com.mycode.telegramagent.utils.GetMessages.getJasperMessage;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class AgentMessageTest {

    @Autowired
    AgentMessageRepo agentMessageRepo;
    @Autowired
    LocaleMessageService localeMessageService;

    @Test
    @Order(1)
    void jasperPriceTest(){
        AgentMessage botMessage = new AgentMessage();
        Map<String, String> message = new HashMap<>();
        message.put("AZ", "Qiymət");
        message.put("EN", "Price");
        message.put("RU", "Цена");
        Gson gson = new Gson();
        String endingMessage = gson.toJson(message);
        botMessage.setMessage(endingMessage);
        botMessage.setKeyword("jasper.price");
        agentMessageRepo.save(botMessage);
        String language = "AZ";
        String keyword = "jasper.price";
        String expected = "Qiymət";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }

    @Test
    @Order(2)
    void jasperNoteTest(){
        AgentMessage botMessage = new AgentMessage();
        Map<String, String> message = new HashMap<>();
        message.put("AZ", "Qeyd");
        message.put("EN", "Note");
        message.put("RU", "Примечание");
        Gson gson = new Gson();
        String endingMessage = gson.toJson(message);
        botMessage.setMessage(endingMessage);
        botMessage.setKeyword("jasper.note");
        agentMessageRepo.save(botMessage);
        String language = "AZ";
        String keyword = "jasper.note";
        String expected = "Qeyd";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }

    @Test
    @Order(3)
    void jasperDescTest(){
        AgentMessage botMessage = new AgentMessage();
        Map<String, String> message = new HashMap<>();
        message.put("AZ", "Xarakteristika");
        message.put("EN", "Description");
        message.put("RU", "Описание");
        Gson gson = new Gson();
        String endingMessage = gson.toJson(message);
        botMessage.setMessage(endingMessage);
        botMessage.setKeyword("jasper.description");
        agentMessageRepo.save(botMessage);
        String language = "EN";
        String keyword = "jasper.description";
        String expected = "Description";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }

    @Test
    @Order(4)
    void jasperWarningMessageTest(){
        AgentMessage botMessage = new AgentMessage();
        Map<String, String> message = new HashMap<>();
        message.put("AZ", "Təklif müddəti bitdi.Artıq bundan sonra təklif gəlməyəcək");
        message.put("EN", "The offer has expired. The offer will no longer come");
        message.put("RU", "Срок действия предложения истек. Предложение больше не будет.");
        Gson gson = new Gson();
        String endingMessage = gson.toJson(message);
        botMessage.setMessage(endingMessage);
        botMessage.setKeyword("warning.message");
        agentMessageRepo.save(botMessage);
        String language = "EN";
        String keyword = "warning.message";
        String expected = "The offer has expired. The offer will no longer come";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }

    @Test
    @Order(5)
    void jasperWarningRepeatTest(){
        AgentMessage botMessage = new AgentMessage();
        Map<String, String> message = new HashMap<>();
        message.put("AZ", "Təklif müddəti bitib artıq.İstəyiniz qeydə alınmadı.");
        message.put("EN", "Offer has expired. Your request has not been registered.");
        message.put("RU", "Срок действия предложения истек. Ваш запрос не зарегистрирован.");
        Gson gson = new Gson();
        String endingMessage = gson.toJson(message);
        botMessage.setMessage(endingMessage);
        botMessage.setKeyword("warning.repeat");
        agentMessageRepo.save(botMessage);
        String language = "AZ";
        String keyword = "warning.repeat";
        String expected = "Təklif müddəti bitib artıq.İstəyiniz qeydə alınmadı.";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }


    @Test
    @Order(6)
    void jasperDateTest(){
        AgentMessage botMessage = new AgentMessage();
        Map<String, String> message = new HashMap<>();
        message.put("RU", "Дата");
        Gson gson = new Gson();
        String endingMessage = gson.toJson(message);
        botMessage.setMessage(endingMessage);
        botMessage.setKeyword("jasper.date");
        agentMessageRepo.save(botMessage);
        String language = "RU";
        String keyword = "jasper.date";
        String expected = "Дата";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }

    @Test
    void jasperDateLangNotFound(){
        String language = "DE";
        String keyword = "jasper.date";
        String expected = "Date";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }

    @Test
    void jasperPriceLangNotFound(){
        String language = "DE";
        String keyword = "jasper.price";
        String expected = "Price";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }

    @Test
    void jasperDescriptionLangNotFound(){
        String language = "DE";
        String keyword = "jasper.description";
        String expected = "Description";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }

    @Test
    void jasperNoteLangNotFound(){
        String language = "DE";
        String keyword = "jasper.note";
        String expected = "Note";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }

    @Test
    void jasperWarningMessageLangNotFound(){
        String language = "DE";
        String keyword = "warning.message";
        String expected = "The offer has expired. The offer will no longer come.";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }

    @Test
    void jasperWarningRepeatLangNotFound(){
        String language = "DE";
        String keyword = "warning.repeat";
        String expected = "Offer has expired. Your request has not been registered.";
        Assertions.assertEquals(expected, getJasperMessage(keyword, language, agentMessageRepo,localeMessageService));
    }




}
