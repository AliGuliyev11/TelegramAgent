package com.mycode.telegramagent;

import com.mycode.telegramagent.enums.Languages;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocaleTest {

    @Autowired
    LocaleMessageService messageService;

    @Test
    public void jasperPriceAzTest() {
        Languages languages = Languages.AZ;
        Assertions.assertEquals("Qiymət", messageService.getMessage("jasper.price", languages));
    }

    @Test
    public void jasperPriceRuTest() {
        Languages languages = Languages.RU;
        Assertions.assertEquals("Цена", messageService.getMessage("jasper.price", languages));
    }

    @Test
    public void jasperPriceEnTest() {
        Languages languages = Languages.EN;
        Assertions.assertEquals("Price", messageService.getMessage("jasper.price", languages));
    }

    @Test
    public void jasperDateAzTest() {
        Languages languages = Languages.AZ;
        Assertions.assertEquals("Tarix", messageService.getMessage("jasper.date", languages));
    }

    @Test
    public void jasperDateRuTest() {
        Languages languages = Languages.RU;
        Assertions.assertEquals("Дата", messageService.getMessage("jasper.date", languages));
    }

    @Test
    public void jasperDateEnTest() {
        Languages languages = Languages.EN;
        Assertions.assertEquals("Date", messageService.getMessage("jasper.date", languages));
    }

    @Test
    public void jasperDateDescTest() {
        Languages languages = Languages.AZ;
        Assertions.assertEquals("Xarakteristika", messageService.getMessage("jasper.description", languages));
    }

    @Test
    public void jasperDescRuTest() {
        Languages languages = Languages.RU;
        Assertions.assertEquals("Описание", messageService.getMessage("jasper.description", languages));
    }

    @Test
    public void jasperDescEnTest() {
        Languages languages = Languages.EN;
        Assertions.assertEquals("Description", messageService.getMessage("jasper.description", languages));
    }

    @Test
    public void jasperNoteDescTest() {
        Languages languages = Languages.AZ;
        Assertions.assertEquals("Qeyd", messageService.getMessage("jasper.note", languages));
    }

    @Test
    public void jasperNoteRuTest() {
        Languages languages = Languages.RU;
        Assertions.assertEquals("Примечание", messageService.getMessage("jasper.note", languages));
    }

    @Test
    public void jasperNoteEnTest() {
        Languages languages = Languages.EN;
        Assertions.assertEquals("Note", messageService.getMessage("jasper.note", languages));
    }

}
