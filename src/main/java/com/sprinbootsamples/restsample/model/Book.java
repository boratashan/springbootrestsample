package com.sprinbootsamples.restsample.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Book {
    private Integer id;
    private String title;
    private String author;
    private BigDecimal price;
    private String image;


    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = true, length = 128)
    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "author", nullable = true, length = 64)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Basic
    @Column(name = "price", nullable = true, precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Basic
    @Column(name = "image", nullable = true, length = 256)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book books = (Book) o;
        return id == books.id &&
                Objects.equals(title, books.title) &&
                Objects.equals(author, books.author) &&
                Objects.equals(price, books.price) &&
                Objects.equals(image, books.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, price, image);
    }
}
