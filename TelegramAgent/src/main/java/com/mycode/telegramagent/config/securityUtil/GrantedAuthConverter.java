package com.mycode.telegramagent.config.securityUtil;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ali Guliyev
 * @version 1.0
 * */

public class GrantedAuthConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    /** Converted keycloak roles */

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccess = source.getClaimAsMap("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");
        System.out.println("Size: "+roles.size());
        return roles.stream()
                .map(rn -> new SimpleGrantedAuthority("ROLE_" + rn))
                .collect(Collectors.toList());
    }
}
