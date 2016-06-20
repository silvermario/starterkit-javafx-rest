package pl.spring.demo.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import pl.spring.demo.entity.BookEntity;
import pl.spring.demo.repository.BookSearchCriteriaRepository;
import pl.spring.demo.searchcriteria.BookSearchCriteria;

//@Transactional(Transactional.TxType.SUPPORTS)
@Service("bookRepository")
public class BookRepositoryImpl implements BookSearchCriteriaRepository {

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<BookEntity> findBooksByCriteria(BookSearchCriteria bookSearchCriteria) {
		// Constructing list of criteria
		List<Predicate> predicates = new ArrayList<Predicate>();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BookEntity> criteriaQuery = builder.createQuery(BookEntity.class);
		Root<BookEntity> bookRoot = criteriaQuery.from(BookEntity.class);

		if (bookSearchCriteria.getTitle() != null) {
			predicates.add(builder.equal(bookRoot.get("title"), bookSearchCriteria.getTitle()));
		}

		if (bookSearchCriteria.getLibraryName() != null) {
			Join library = bookRoot.join("library");
			predicates.add(builder.equal(library.get("name"), bookSearchCriteria.getLibraryName()));
		}

		if (bookSearchCriteria.getAuthor() != null) {
			Join authors = bookRoot.join("authors");
			predicates.add(builder.equal(authors.get("lastName"), bookSearchCriteria.getAuthor()));
		}

		criteriaQuery.select(bookRoot).where(predicates.toArray(new Predicate[] {}));

		TypedQuery<BookEntity> query = entityManager.createQuery(criteriaQuery);
		return query.getResultList();
	}

}
