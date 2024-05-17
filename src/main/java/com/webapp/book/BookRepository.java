package com.webapp.book;

import com.webapp.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {
    List<Book> findByCreatedBy(User user);
    List<Book> findByCategoryStartingWithAndCreatedBy(String category, User user);
    List<Book> findByBookAvailabilityAndCategory(String bookAvailability, String category);
    void deleteByCreatedBy(User user);// Add this method
}
