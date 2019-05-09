package com.sw.test.eureka;

import feign.Feign;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@ConditionalOnClass(Feign.class)
public class FeignConfiguration {

//    @Bean
//    public WebMvcConfigurationSupport feignWebRegistrations() {
//        return new WebMvcConfigurationSupport() {
//            @Override
//            public RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
//                return new FeignRequestMappingHandlerMapping();
//            }
//        };
//    }
//
//    private static class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
//        @Override
//        protected boolean isHandler(Class<?> beanType) {
//            return super.isHandler(beanType) &&
//                    !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
//        }
//    }
}
