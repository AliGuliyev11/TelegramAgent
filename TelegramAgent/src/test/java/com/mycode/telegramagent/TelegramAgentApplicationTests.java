package com.mycode.telegramagent;

import com.mycode.telegramagent.utils.ExpiredDateGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.mycode.telegramagent.utils.ExpiredDateGenerator.getExpiredDate;

class TelegramAgentApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(getExpiredDate("09:00","19:00",21));
    }

}
