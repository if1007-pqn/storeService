package com.porquin.db;

import java.util.List;
import java.util.Optional;

import com.porquin.model.Store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
public class StoreMongoService {
 
    @Autowired
    private StoreMongoRepository repository;
 
    public void save(Store obj) {
        repository.save(obj);
    }

    public List<Store> findAll() {
       return repository.findAll();
    }
 
    public List<Store> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public long count() {
        return repository.count();
    }
 
    public Optional<Store> findById(String id) {
        return repository.findById(id);
    }
 
    public void delete(String id) {
        repository.deleteById(id);
    }
 
}