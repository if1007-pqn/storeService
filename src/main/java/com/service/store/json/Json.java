package com.service.store.json;

import com.google.gson.Gson;
import com.service.store.exception.InvalidJsonException;

public class Json<T> {
    
    private Gson gson;
    private Class<T> type;

    public Json(Class <T> type) {
        this.gson = new Gson();
        this.type = type;
    }

    public T fromJson(String json) {
        try {
            T r = gson.fromJson(json, type);
            return r;
        } catch (Exception e) {
            throw new InvalidJsonException();
        }
    }
}