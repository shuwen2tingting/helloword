package com.sw.test.eureka;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
public class HelloRemoteFail implements HelloRemote{

    @Override
    public String getEurekaClientUrl() {
        return "error";
    }

    @Override
    public String getString(String myUrl, String name) {
        return "error";
    }

    @Override
    public String getP(String myUrl, Peopel name) {
        return "error";
    }

    @Override
    public String getP(){
        return "error";
    }
}
