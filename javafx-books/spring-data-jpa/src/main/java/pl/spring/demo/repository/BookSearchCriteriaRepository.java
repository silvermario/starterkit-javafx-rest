package pl.spring.demo.repository;

import java.util.List;


import pl.spring.demo.entity.BookEntity;
import pl.spring.demo.searchcriteria.BookSearchCriteria;


public interface BookSearchCriteriaRepository {
	
	List<BookEntity> findBooksByCriteria(BookSearchCriteria bookSearchCriteria);
}
