package pl.spring.demo.service;

import java.util.List;

import pl.spring.demo.model.BookEntity;
import pl.spring.demo.to.BookTo;

public interface BookService {

    List<BookTo> findAllBooks();
    List<BookTo> findBooksByTitle(String title);
    List<BookTo> findBooksByAuthor(String author);
    List<BookTo> findBookById(Long id);
    
    List<BookEntity> findAll();
    List<BookEntity> findBooksByTitleE(String title);

    //List<BookEntity> findBooksByCriteria(BookSearchCriteria bookSearchCriteria);

    BookTo saveBook2(BookTo book);
    BookEntity saveBook(BookEntity book);
	void deleteBook(Long id);
}
