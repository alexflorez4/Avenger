package com.shades.security;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsernameRememberingAuthenticationFailureHandler implements AuthenticationFailureHandler{

    private UsernamePasswordAuthenticationFilter filter;
    private String redirectTarget;

    public void setFilter(UsernamePasswordAuthenticationFilter filter) {
        this.filter = filter;
    }

    public void setRedirectTarget(String redirectTarget) {
        this.redirectTarget = redirectTarget;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        AuthenticationException e)
            throws IOException, ServletException {

        String usernameProperty = filter.getUsernameParameter();

        //Find out what the username was
        String userEmail = httpServletRequest.getParameter(usernameProperty);

        //redirect, to the url <context-root>/login?jsp?error&username=
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + redirectTarget + "?error&username=" + userEmail);
    }
}
