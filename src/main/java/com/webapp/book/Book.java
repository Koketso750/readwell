package com.webapp.book;


import com.webapp.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String author;

    private String category;

    private byte[] pdfFile;

    private String bookAvailability;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    public Book(String name, String author, String category, String bookAvailability, User createdBy) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.bookAvailability = bookAvailability;
        this.createdBy = createdBy;
    }

    public Book() {

    }

    // Getters and Setters

    public String getBookAvailability() {
        return bookAvailability;
    }

    public void setBookAvailability(String bookAvailability) {
        this.bookAvailability = bookAvailability;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte[] getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(byte[] pdfFile) {
        this.pdfFile = pdfFile;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}

