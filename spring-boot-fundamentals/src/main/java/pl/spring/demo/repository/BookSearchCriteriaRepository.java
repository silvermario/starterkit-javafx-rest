package pl.spring.demo.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import pl.spring.demo.model.BookEntity;
import pl.spring.demo.searchcriteria.BookSearchCriteria;

@Repository
public interface BookSearchCriteriaRepository {
	
	List<BookEntity> findBooksByCriteria(BookSearchCriteria bookSearchCriteria);
}
