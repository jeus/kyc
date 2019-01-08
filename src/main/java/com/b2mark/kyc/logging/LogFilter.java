/**
 * <h1>RestFul controll KYC</h1>
 * every request log with sel4j.
 *
 * @author b2mark
 * @version 1.0
 * @since 2018
 */

package com.b2mark.kyc.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class LogFilter implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("LogFilter is Intilize");
    }

    //TODO: have to change this section for security check.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        LOG.info("http request is :{}" + request.getRequestURI());
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");


        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        LOG.info("Filter is closing");
    }
}
