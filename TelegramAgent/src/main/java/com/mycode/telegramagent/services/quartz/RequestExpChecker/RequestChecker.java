package com.mycode.telegramagent.services.quartz.RequestExpChecker;

import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.rabbitmq.offerOrder.publisher.RabbitOfferService;
import com.mycode.telegramagent.repositories.OrderRepo;
import com.mycode.telegramagent.services.LifeCycle.OrderLifeCycle;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author Ali Guliyev
 * @version 1.0
 * If request expired date equals now change agent request status to expired
 */


@Configuration
public class RequestChecker extends QuartzJobBean {

    OrderDAO dao;

    public RequestChecker(OrderDAO dao) {
        this.dao = dao;
    }

    /**
     * Check user request status
     * If
     */

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = dateFormat.format(date);
        List<UserRequest> userRequestList = dao.getExpiredRequests(strDate);
        System.out.println(userRequestList.size());
        dao.requestChecker(strDate);
        System.out.println(strDate);
    }
}
