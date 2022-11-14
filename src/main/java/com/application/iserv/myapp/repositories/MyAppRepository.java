package com.application.iserv.myapp.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class MyAppRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<String> getImage() {

        String sql = "SELECT images.image FROM images WHERE images.image_id = '1'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<String>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
