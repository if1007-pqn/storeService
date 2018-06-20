package com.service.store.model;

import java.util.List;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "store")

@Getter
public class Store {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;
    private List<Level> levels;

    public Store(String username, String password, List<Level> levels) {
        this.username = username;
        this.password = password;
        this.levels = levels;
    }

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

    /**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the levels
	 */
	public List<Level> getLevels() {
		return levels;
	}

	/**
	 * @param levels the levels to set
	 */
	public void setLevels(List<Level> levels) {
		this.levels = levels;
	}

}