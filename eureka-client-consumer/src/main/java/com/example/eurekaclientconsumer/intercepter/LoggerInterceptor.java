package com.example.eurekaclientconsumer.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.eurekaclientconsumer.filter.UserContext;
import com.example.eurekaclientconsumer.filter.UserContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        log.info("[preHandle][" + request + "]" + "[" + request.getMethod()
                + "]" + request.getRequestURI());

        log.info("---------    Header start   --------");
        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();

            log.info(name + ": " + StringUtils.join(request.getHeaders(name).asIterator(), ", "));
        }
        log.info("---------    Header end   --------");

        return true;
    }

    // not work
    // @Override
    // public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
    //         ModelAndView modelAndView) throws Exception {
    //     response.addHeader(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
    //     HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    // }
}
