package com.freshman.webapi.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

@Service
public class SecurityAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        if (collection == null) {
            return;
        }

        Iterator<ConfigAttribute> ite = collection.iterator();

        while (ite.hasNext()) {

            ConfigAttribute ca = ite.next();
            String needRole = ((SecurityConfig) ca).getAttribute();

            //System.out.println("authentication size"+ authentication.getAuthorities().size());
            // ga 为用户所被赋予的权限。 needRole 为访问相应的资源应该具有的权限。
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                String authority = ga.getAuthority().trim();
                if (needRole.trim().equals(authority)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("没有权限");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
