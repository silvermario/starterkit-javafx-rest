package pl.spring.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.spring.demo.mapper.BookMapper;
import pl.spring.demo.model.BookEntity;
import pl.spring.demo.repository.BookRepository;
import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    
    /*@Autowired
    private BookSearchCriteriaRepository bookSearchCriteriaRepositoryImpl;*/

    @Override
    public List<BookTo> findAllBooks() {
        return BookMapper.map2To(bookRepository.findAll());
    }

    @Override
    public List<BookEntity> findAll() {
    	// TODO Auto-generated method stub
    	List<BookEntity> books = bookRepository.findAll();
    	
    	return books;
    }
    
    @Override
    public List<BookTo> findBooksByTitle(String title) {
        return BookMapper.map2To(bookRepository.findBookByTitle(title));
    }
    @Override
    public List<BookEntity> findBooksByTitleE(String title) {
        return bookRepository.findBookByTitle(title);
    }

    @Override
    public List<BookTo> findBooksByAuthor(String author) {
        return BookMapper.map2To(bookRepository.findBookByAuthor(author));
    }

    @Override
    @Transactional(readOnly = false)
    public BookTo saveBook2(BookTo book) {
        BookEntity entity = BookMapper.map(book);
        entity = bookRepository.save(entity);
        return BookMapper.map(entity);
    }


	@Override
	public List<BookTo> findBookById(Long id) {
		return BookMapper.map2To(bookRepository.findBookById(id));
	}

	@Override
	@Transactional(readOnly = false)
	public BookEntity saveBook(BookEntity book) {
		return bookRepository.save(book);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteBook(Long id) {
		bookRepository.delete(id);
	}

	/*@Override
	public List<BookEntity> findBooksByCriteria(BookSearchCriteria bookSearchCriteria) {
		//return BookMapper.map2To(bookRepository.findBooksByCriteria(bookSearchCriteria));
		
		return bookSearchCriteriaRepositoryImpl.findBooksByCriteria(bookSearchCriteria);
		//return null;
	}*/
	
	
}
