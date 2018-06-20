package com.service.store.db;

import java.util.List;

import com.service.store.model.Store;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreMongoRepository extends MongoRepository<Store, String> {

	List<Store> findByUsername(String username);
}