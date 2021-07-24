package com.mycode.telegramagent;

import org.junit.jupiter.api.Test;

import static com.mycode.telegramagent.utils.ExpiredDateGenerator.getExpiredDate;

class TelegramAgentApplicationTests {


    @Test
    void contextLoads() {
        String[] workingDays={"1","2","3","4","5"};
        System.out.println(getExpiredDate("09:00", "19:00", 8, workingDays));
    }

}
