package pl.spring.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.spring.demo.model.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

	@Query("select book from BookEntity book where upper(book.title) like concat('%', upper(:title), '%')")
	public List<BookEntity> findBookByTitle(@Param("title") String title);

	@Query("select book from BookEntity book JOIN book.authors author where upper(author.firstName) like concat('%', upper(:author), '%') or upper(author.lastName) like concat('%', upper(:author), '%')")
	public List<BookEntity> findBookByAuthor(@Param("author") String author);
	
	@Query("select book from BookEntity book where book.id = (:id)")
	public List<BookEntity> findBookById(@Param("id") Long id);

	/*@Query("select book from BookEntity book where upper(book.title) like concat(upper(:title), '%') and upper(book.authors) like concat('%', upper(:author), '%')")
	public List<BookEntity> findBookByTitleAndAuthor(@Param("title") String title, @Param("author") String author);
*/
}
