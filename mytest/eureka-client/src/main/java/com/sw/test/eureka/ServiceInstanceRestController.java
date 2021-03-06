package com.sw.test.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class ServiceInstanceRestController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    EurekaClientConfigBean eurekaClientConfigBean;

    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }

    @GetMapping("/eureka-service-info")
    public Object getEurekaServerUrl(){
        return eurekaClientConfigBean.getServiceUrl();
    }

    @GetMapping("/eureka-client-info")
    public String getEurekaClientUrl(){
        return eurekaClientConfigBean.getAvailabilityZones().toString();
    }

    @GetMapping("{myUrl}/get")
    public String getString(@PathVariable String myUrl,String name){
        return myUrl+"--"+name;
    }

    @GetMapping("{myUrl}/getP")
    public String getPeopel(@PathVariable String myUrl, Peopel p){
        log.error(p.toString());
        return myUrl+"--"+p.toString();
    }

    @GetMapping("/getP")
    public String getP(){
        return "client";
    }
}
