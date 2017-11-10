package com.sprinbootsamples.restsample.configuration;

import com.sprinbootsamples.restsample.util.logging.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
   @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        ApiVersionRequestMappingHandlerMapping mappingHandlerMapping = new ApiVersionRequestMappingHandlerMapping("v");
        mappingHandlerMapping.setOrder(0);
        mappingHandlerMapping.setInterceptors(getInterceptors());
        return mappingHandlerMapping;
    }

    @Bean
    public FilterRegistrationBean filterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LoggingFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("loggingFilter");
        registration.setOrder(1);
        return registration;
    }
}
