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

@RestController("eureka-client")
public class ServiceInstanceRestController {
    @Autowired
    HelloRemote helloRemote;

    @GetMapping("/eureka-client-info")
    public String getEurekaClientUrl(){
        return helloRemote.getEurekaClientUrl();
    }

    @GetMapping("/getString")
    public String getString(){
        String s=  "hello";
        return helloRemote.getString("sw",s);
    }

    @GetMapping("/getP")
    public String getP(){
        Peopel peopel = new Peopel();
        peopel.setAge(20);
        peopel.setName("hamapi");
        return helloRemote.getP("url-from-consumer",peopel);
    }
}
