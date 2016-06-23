package pl.spring.demo.web.api;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.spring.demo.model.BookEntity;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@RestController
@RequestMapping("/api")
public class BooksController {

    @Autowired
    private BookService bookService;
    
    @RequestMapping(
            value = "/books",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<BookEntity>> getBooks() {

        Collection<BookEntity> books = bookService.findAll();

        return new ResponseEntity<Collection<BookEntity>>(books,
                HttpStatus.OK);
    }
    
    @RequestMapping(
            value = "/books/title/{title}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookEntity>> getBooks(@PathVariable("title") String title) {

    	List<BookEntity> books = bookService.findBooksByTitleE(title);
        if (books == null) {
            return new ResponseEntity<List<BookEntity>>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<BookEntity>>(books, HttpStatus.OK);
    }

    @RequestMapping(
    		value = "/books",
    		method = RequestMethod.POST,
    		consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createBook2(@RequestBody BookEntity book) {
		
    	if (book.getId() != null && !bookService.findBookById(book.getId()).isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		bookService.saveBook(book);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

    @RequestMapping(
            value = "/books/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookEntity> deleteBook(@PathVariable("id") Long id,
            @RequestBody BookEntity book) {
    	
    	if (id == null || bookService.findBookById(id).isEmpty()) {
			return new ResponseEntity<BookEntity>(HttpStatus.NOT_FOUND);
		}
    	bookService.deleteBook(id);
        return new ResponseEntity<BookEntity>(HttpStatus.OK);
    }
}
