package com.sw.test.eureka;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="eureka-client",fallback=HelloRemoteFail.class)
public interface HelloRemote {

    @GetMapping("/eureka-client-info")
    String getEurekaClientUrl();

    @GetMapping("{myUrl}/get")
    public String getString(@PathVariable("myUrl") String myUrl,@RequestParam("name") String name);

    @GetMapping("{myUrl}/getP")
    public String getP(@PathVariable("myUrl") String myUrl,@RequestParam("name") Peopel name);

    @GetMapping("/getP")
    public String getP();
}
