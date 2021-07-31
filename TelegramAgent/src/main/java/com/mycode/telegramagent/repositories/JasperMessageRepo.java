package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.JasperMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote Repository of jasper message
 */

public interface JasperMessageRepo extends JpaRepository<JasperMessage, Long> {
    JasperMessage getJasperMessageByKeyword(String keyword);
}
