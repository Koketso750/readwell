package com.webapp.book;

import com.webapp.user.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepo;

    public void save(Book book) {
        bookRepo.save(book);
    }

    public Book findById(Integer id) {
        return bookRepo.findById(id).orElse(null);
    }

    public List<Book> findBooksByUser(User user){
        List<Book> books = bookRepo.findByCreatedBy(user);

        return books;
    }

    public List<Book> getBooksByCategoryAndUser(String category, User user){
        List<Book> books = bookRepo.findByCategoryStartingWithAndCreatedBy(category, user);

        return books;
    }

    // Add this method
    public List<Book> findPublicFictionBooks(String category) {
        return bookRepo.findByBookAvailabilityAndCategory("Public", category);
    }

    @Transactional
    public void removeBookByUser(User user){
        bookRepo.deleteByCreatedBy(user);
    }
}
