package com.porquin.db;

import java.util.List;

import com.porquin.model.Store;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreMongoRepository extends MongoRepository<Store, String> {

	List<Store> findByUsername(String username);
}