package com.mycode.telegramagent.services.quartz.RequestExpChecker;

import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.repositories.OrderRepo;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestChecker extends QuartzJobBean {

    OrderDAO dao;

    public RequestChecker(OrderDAO dao) {
        this.dao = dao;
    }

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = dateFormat.format(date);
        dao.requestChecker(strDate);

//        Date date=simpleDateFormat.parse(a);
        System.out.println(strDate);
    }
}
