package com.sw.test.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;

@RestController
public class ServiceInstanceRestController {
    @Autowired
    HelloRemote helloRemote;

    @GetMapping("/eureka-client-info")
    public String getEurekaClientUrl(){
        return helloRemote.getEurekaClientUrl();
    }
}
