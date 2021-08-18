package com.feedify.tests.entitytests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.transaction.BeforeTransaction;

import com.feedify.database.entity.Category;
import com.feedify.database.repository.CategoryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CategoryTest {

    @Autowired
    private CategoryRepository categoryRepo;

    @BeforeTransaction
    public void init(){
        Category category = new Category();
        category.setName("Politik");
        category.setId(1l);
        categoryRepo.save(category);
    }

    @Test
    public void findCategoryTest(){
        Category category = categoryRepo.findById(1l).get();
        assertEquals(category.getId(), 1l);
    }

    @Test
    public void updateCategoryTest(){
        Category category = categoryRepo.findById(1l).get();
        category.setId(5l);
        category = categoryRepo.save(category);
        assertNotEquals(category.getId(), 1l);
    }

    @Test
    public void findAllCategoryTest(){
        List<Category> categories = categoryRepo.findAll();
        assertSame(categories.size(), 1);
    }

}
