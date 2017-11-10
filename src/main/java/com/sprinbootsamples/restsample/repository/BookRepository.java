package com.sprinbootsamples.restsample.repository;

import org.springframework.dao.DataAccessException;
import com.sprinbootsamples.restsample.model.Book;


import java.util.Collection;

public interface BookRepository {
    Collection<Book> findAll(int offset, int count) throws DataAccessException;
    Book findById(int id) throws DataAccessException;
    void save(Book book) throws DataAccessException;
    void delete(Book book) throws DataAccessException;
}
