package com.porquin;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreRepository extends MongoRepository<Store, String> {

	List<Store> findByUsername();
}