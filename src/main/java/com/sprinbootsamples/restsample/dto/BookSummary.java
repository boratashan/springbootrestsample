package com.sprinbootsamples.restsample.dto;

import com.sprinbootsamples.restsample.model.Book;


import java.math.BigDecimal;

public class BookSummary {
    public int id;
    public String title;
    public BigDecimal price;
    public String link;

    private BookSummary(int id, String title, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public static BookSummary from(Book source) {
        return new BookSummary(source.getId(), source.getTitle(), source.getPrice());
    }

    public BookSummary setResourceLink(String link) {
        this.setLink(link);
        return this;
    }

}
