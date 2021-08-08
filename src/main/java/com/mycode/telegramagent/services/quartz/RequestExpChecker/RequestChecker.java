package com.mycode.telegramagent.services.quartz.RequestExpChecker;

import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.dto.WarningDto;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.rabbitmq.offerOrder.publisher.RabbitOfferService;
import com.mycode.telegramagent.repositories.AgentMessageRepo;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.mycode.telegramagent.utils.GetMessages.getJasperMessage;


/**
 * @author Ali Guliyev
 * @version 1.0
 * If request expired date equals now change agent request status to expired
 */


@Configuration
public class RequestChecker extends QuartzJobBean {

    OrderDAO dao;
    RabbitOfferService rabbitOfferService;
    AgentMessageRepo agentMessageRepo;
    LocaleMessageService localeMessageService;

    public RequestChecker(OrderDAO dao, RabbitOfferService rabbitOfferService,
                          AgentMessageRepo agentMessageRepo, LocaleMessageService localeMessageService) {
        this.dao = dao;
        this.rabbitOfferService = rabbitOfferService;
        this.agentMessageRepo = agentMessageRepo;
        this.localeMessageService = localeMessageService;
    }

    /**
     * Check user request status
     */

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = dateFormat.format(date);
        List<UserRequest> userRequestList = dao.getExpiredRequests(strDate);
        System.out.println(userRequestList.size());

        for (var item : userRequestList) {
            if (!dao.checkAgentMadeOfferOrNot(item.getUserId())) {
                JSONObject jsonObject = new JSONObject(item.getUserRequest());
                String language = jsonObject.getString("lang");
                rabbitOfferService.warn(WarningDto.builder()
                        .text(getJasperMessage("warning.message", language, agentMessageRepo, localeMessageService))
                        .userId(item.getUserId()).build());
            }
        }

        dao.requestChecker(strDate);
        System.out.println(strDate);
    }
}
