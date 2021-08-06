package com.mycode.telegramagent.services.LifeCycle;

import com.mycode.telegramagent.dto.MailDTO;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import com.mycode.telegramagent.services.email.EmailServiceImpl;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;


/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote This class for Agent entity's EntityListener
 */

@Getter
@Setter
@NoArgsConstructor
public class AgentLifeCycle {
    EmailServiceImpl emailService;
    LocaleMessageService messageService;

    public AgentLifeCycle(EmailServiceImpl emailService, LocaleMessageService messageService) {
        this.emailService = emailService;
        this.messageService = messageService;
    }

    @Value("${agent.email.confirmation.limit}")
    long limitConfirmationEmail;

    @Value("${email.verify.agent.url}")
    String emailUrl;

    @Value("${confirmation.template}")
    String confirmationTemp;

    /**
     * After agent creation agent enters this method
     */

    @PostPersist
    private void afterPost(Agent agent) {
        String url = emailUrl + agent.getHashCode();
        String text = messageService.getMessage("agent.lifecycle.email.text", limitConfirmationEmail);
        MailDTO mail = MailDTO.builder().to(agent.getEmail()).subject(messageService.getMessage("agent.confirmation.subject"))
                .buttonName(messageService.getMessage("agent.confirmation.button.text")).text(text).templateName(confirmationTemp).link(url).build();

        emailService.sendSimpleMessage(mail);
    }
}
