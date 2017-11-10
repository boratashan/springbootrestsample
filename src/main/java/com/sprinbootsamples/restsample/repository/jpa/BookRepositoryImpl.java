package com.sprinbootsamples.restsample.repository.jpa;

import com.sprinbootsamples.restsample.repository.BookRepository;
import com.sprinbootsamples.restsample.model.Book;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Book> findAll(int offset, int count) throws DataAccessException {
        Query query = this.em.createQuery("SELECT book FROM Book book");
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return query.getResultList();
    }

    @Override
    public Book findById(int id) throws DataAccessException {
        return this.em.find(Book.class, id);
    }

    @Override
    public void save(Book book) throws DataAccessException {
        if (book.getId() == null) {
            this.em.persist(book);
        } else {
            this.em.merge(book);
        }
    }

    @Override
    public void delete(Book book) throws DataAccessException {
        this.em.remove(book);
    }
}
