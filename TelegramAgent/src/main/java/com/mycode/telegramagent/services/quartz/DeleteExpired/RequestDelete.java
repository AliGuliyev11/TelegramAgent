package com.mycode.telegramagent.services.quartz.DeleteExpired;

import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.repositories.OrderRepo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Configuration
public class RequestDelete extends QuartzJobBean {

    OrderDAO dao;

    public RequestDelete(OrderDAO dao) {
        this.dao = dao;
    }

    @Value("${delete.request.time.range}")
    int day;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Date now = new Date();
        Date expected = DateUtils.addDays(now, -1*day);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(expected);
        System.out.println(date);
        dao.checkAndDeleteRequest(date);
    }
}
