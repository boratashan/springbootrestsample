
package com.sprinbootsamples.restsample.service;

import java.util.Collection;

import com.sprinbootsamples.restsample.model.Book;
import com.sprinbootsamples.restsample.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BookItemsServiceImpl implements BookItemsService {

	private BookRepository bookRepository;

    @Autowired
     public BookItemsServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
    }


	@Override
	@Transactional(readOnly = true)
	public Collection<Book> findAllBooks(int offset, int count) throws DataAccessException {
		return bookRepository.findAll(offset, count);
	}

	@Override
	@Transactional(readOnly = true)
	public Book findBookById(int id) throws DataAccessException {
		return bookRepository.findById(id);
	}

	@Override
    @Transactional
	public void saveBook(Book book) throws DataAccessException {
		bookRepository.save(book);
	}

	@Override
	@Transactional
	public void deleteBook(Book book) throws DataAccessException {
		bookRepository.delete(book);
	}

}
