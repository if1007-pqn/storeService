package com.porquin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
public class StoreService {
 
    @Autowired
    private StoreRepository storeRepository;
 
    public void save(String username, String password, List<Level> levels) {
        storeRepository.save(new Store(username, password, levels));
    }
 
    public List<Store> findAll() {
       return storeRepository.findAll();
    }
 
    public List<Store> findByUsername() {
        return storeRepository.findByUsername();
    }

    public long count() {
        return storeRepository.count();
    }
 
    public Optional<Store> findById(String id) {
        return storeRepository.findById(id);
    }
 
    public void delete(String id) {
        storeRepository.deleteById(id);
    }
 
}