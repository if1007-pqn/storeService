package com.service.store;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("config")
public class ApplicationConfig {

    private String tokenKey;

    /**
     * @return the tokenKey
     */
    public String getTokenKey() {
        return tokenKey;
    }

    /**
     * @param tokenKey the tokenKey to set
     */
    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}