
package com.sprinbootsamples.restsample.rest;

import com.sprinbootsamples.restsample.configuration.VersionedApi;
import com.sprinbootsamples.restsample.dto.BookSummary;
import com.sprinbootsamples.restsample.service.BookItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.sprinbootsamples.restsample.model.Book;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;



@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("items")
@VersionedApi(1)
public class BooksRestController {

	@Autowired
	private BookItemsService bookItemsService;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@VersionedApi({1, 2})
	public ResponseEntity<Collection<BookSummary>> getAllBooks(@RequestParam(value = "offset",
																			 required = false,
																			 defaultValue = "1") int offset,
                                                               @RequestParam(value = "count",
																	         required = false,
																	         defaultValue = "3") int count,
                                                               HttpServletRequest request){

		String requestUri = request.getRequestURI();
		Collection<BookSummary> books = new ArrayList<BookSummary>();
		for(Book book : this.bookItemsService.findAllBooks(offset, count)) {
			books.add(BookSummary.from(book).setResourceLink(String.format("%s/%d", requestUri, book.getId())));
		}
		if (books.isEmpty()){
			return new ResponseEntity<Collection<BookSummary>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<BookSummary>>(books, HttpStatus.OK);
	}



	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@VersionedApi({1,2})
	public ResponseEntity<Book> getBookById(@PathVariable("id") int id){

		Book book  = this.bookItemsService.findBookById(id);

		if(book == null){
			return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Book>(book, HttpStatus.OK);
	}



	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@VersionedApi({1,2})
	public ResponseEntity<Book> addBook(@RequestBody @Valid Book book, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (book == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Book>(headers, HttpStatus.BAD_REQUEST);
		}
		this.bookItemsService.saveBook(book);
		headers.setLocation(ucBuilder.path("/api/items/{id}").buildAndExpand(book.getId()).toUri());
		return new ResponseEntity<Book>(book, headers, HttpStatus.CREATED);
	}




	@RequestMapping(value = "/{bookId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@VersionedApi({1,2})
	public ResponseEntity<Book> updateBook(@PathVariable("bookId") int bookId, @RequestBody @Valid Book book, BindingResult bindingResult){
		BindingErrorsResponse errors = new BindingErrorsResponse();
		HttpHeaders headers = new HttpHeaders();
		if(bindingResult.hasErrors() || (book == null)){
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Book>(headers, HttpStatus.BAD_REQUEST);
		}
		Book currentBook = this.bookItemsService.findBookById(bookId);
		if(currentBook == null){
			return new ResponseEntity<Book>(HttpStatus.NOT_FOUND);
		}
		currentBook.setAuthor(book.getAuthor());
		currentBook.setImage(book.getImage());
		currentBook.setPrice(book.getPrice());
		currentBook.setTitle(book.getTitle());

		this.bookItemsService.saveBook(currentBook);
		return new ResponseEntity<Book>(currentBook, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{bookId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional
	@VersionedApi({1,2})
	public ResponseEntity<Void> deleteBook(@PathVariable("bookId") int bookId){
		Book book = this.bookItemsService.findBookById(bookId);
		if(book == null){
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		this.bookItemsService.deleteBook(book);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
