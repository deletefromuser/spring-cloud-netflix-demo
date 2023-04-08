package com.example.eurekaclient.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        log.info("[preHandle]" + "[" + request.getMethod()
                + "]" + request.getRequestURI());
        log.debug("---------    Header start   --------");
        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();

            log.debug(name + ": " + StringUtils.join(request.getHeaders(name).asIterator(), ", "));
        }
        log.debug("---------    Header end   --------");

        return true;
    }
}
