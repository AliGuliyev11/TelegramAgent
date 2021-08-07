package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.AgentMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote Repository of jasper message
 */

public interface AgentMessageRepo extends JpaRepository<AgentMessage, Long> {
    AgentMessage getJasperMessageByKeyword(String keyword);
}
