package com.porquin;

import java.util.List;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "store")
@Getter
public class Store {
    @Id
    public String id;

    // @Indexed(unique = true)
    private String username;

    private String password;
    private List<Level> levels;

    public Store(String username, String password, List<Level> levels) {
        this.username = username;
        this.password = password;
        this.levels = levels;
    }
}