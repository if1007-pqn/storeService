package com.service.store.json;

import java.util.Date;

public class PurchaseInfoJson {
    private double porcent;
    private Date expiration;

    public PurchaseInfoJson(double porcent, Date expiration) {
        this.porcent = porcent;
        this.expiration = expiration;
    }

    /**
     * @return the expiration
     */
    public Date getExpiration() {
        return expiration;
    }

    /**
     * @return the porcent
     */
    public double getPorcent() {
        return porcent;
    }

}