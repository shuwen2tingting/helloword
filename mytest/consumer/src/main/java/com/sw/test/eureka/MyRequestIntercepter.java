package com.sw.test.eureka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: Administrator
 * @date: 2019/05/21
 * @description:
 */
@Component
public class MyRequestIntercepter implements RequestInterceptor {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void apply(RequestTemplate template) {
        if(template.method().equals("GET") && template.body() !=null){
            try{
                JsonNode jsonNode = objectMapper.readTree(template.body());
                template.body(null);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
