package com.example.bankcards.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    @Getter
    @Setter
    private String secret;
    @Getter
    @Setter
    private Duration lifetime;
}
