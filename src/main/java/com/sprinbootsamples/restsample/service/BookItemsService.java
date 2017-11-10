package com.sprinbootsamples.restsample.service;

import java.util.Collection;

import com.sprinbootsamples.restsample.model.Book;
import org.springframework.dao.DataAccessException;



public interface BookItemsService {

	Collection<Book> findAllBooks(int offset, int count) throws DataAccessException;
	Book findBookById(int id) throws DataAccessException;
	void saveBook(Book book) throws DataAccessException;
	void deleteBook(Book book) throws DataAccessException;
}
