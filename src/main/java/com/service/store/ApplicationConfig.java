package com.service.store;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("config")
public class ApplicationConfig {

    private String tokenKey;
    private int bcryptSalts;

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

    /**
     * @return the bcryptSalts
     */
    public int getBcryptSalts() {
        return bcryptSalts;
    }

    /**
     * @param bcryptSalts the bcryptSalts to set
     */
    public void setBcryptSalts(int bcryptSalts) {
        this.bcryptSalts = bcryptSalts;
    }
}