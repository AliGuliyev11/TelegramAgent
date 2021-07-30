package com.mycode.telegramagent;

import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocaleTest {

    @Autowired
    LocaleMessageService messageService;



}
