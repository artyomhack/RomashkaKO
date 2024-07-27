package com.artyom.romashkako;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.artyom.romashkako")
public class RomashkaKoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RomashkaKoApplication.class, args);
    }

}
