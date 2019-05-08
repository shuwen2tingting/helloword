package com.sw.test.eureka;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("eureka-client")
public interface HelloRemote {

    @GetMapping("/eureka-client-info")
    String getEurekaClientUrl();

    @GetMapping("{myUrl}/get")
    public String getString(@PathVariable String myUrl,@RequestParam String name);

    @GetMapping("{myUrl}/getP")
    public String getP(@PathVariable String myUrl,@RequestParam Peopel name);
}
