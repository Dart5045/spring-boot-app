package com.mylearning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private static int COUNTER = 0;

    record Ping(String result){}

    @GetMapping("/ping")
    public Ping getPingPong() {
        return new Ping("Pong: %s".formatted(++COUNTER));
    }
}