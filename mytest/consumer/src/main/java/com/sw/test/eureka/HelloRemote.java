package com.sw.test.eureka;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("eureka-client")
public interface HelloRemote {

    @GetMapping("/eureka-client-info")
    String getEurekaClientUrl();
}
