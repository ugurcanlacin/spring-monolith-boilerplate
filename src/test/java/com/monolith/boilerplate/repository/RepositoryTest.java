package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.BoilerplateApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;

@SpringBootTest(classes = BoilerplateApplication.class)
public class RepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @Test
    void injectedComponentsAreNotNull(){
        Assertions.assertNotNull(jdbcTemplate);
        Assertions.assertNotNull(entityManager);
    }
}
