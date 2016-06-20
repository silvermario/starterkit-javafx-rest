package pl.spring.demo.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.spring.demo.entity.BookEntity;
import pl.spring.demo.searchcriteria.BookSearchCriteria;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "CommonRepositoryTest-context.xml")
public class BookSearchCriteriaRepositoryTest {

	@Autowired
	private BookSearchCriteriaRepository bookSearchCriteriaRepository;

	@Test
	public void testShouldFindAllBooks() {
		// given
		BookSearchCriteria bookSearchCriteria = new BookSearchCriteria();
		// when
		List<BookEntity> allBooks = bookSearchCriteriaRepository.findBooksByCriteria(bookSearchCriteria);
		// then
		assertFalse(allBooks.isEmpty());
		assertEquals(3, allBooks.size());
	}

	@Test
	public void testShouldFindBookByTitle() {
		// given
		String bookTitle = "Druga książka";
		BookSearchCriteria bookSearchCriteria = new BookSearchCriteria(bookTitle, null, null);
		// when
		List<BookEntity> allBooks = bookSearchCriteriaRepository.findBooksByCriteria(bookSearchCriteria);
		// then
		assertFalse(allBooks.isEmpty());
		assertEquals(1, allBooks.size());
		assertEquals("Druga książka", allBooks.get(0).getTitle());
	}

	@Test
	public void testShouldFindBookByAuthor() {
		// given
		String authorName = "Nowak";
		BookSearchCriteria bookSearchCriteria = new BookSearchCriteria(null, authorName, null);
		// when
		List<BookEntity> allBooks = bookSearchCriteriaRepository.findBooksByCriteria(bookSearchCriteria);
		// then
		assertFalse(allBooks.isEmpty());
		assertEquals(1, allBooks.size());
		assertEquals("Druga książka", allBooks.get(0).getTitle());
	}

	@Test
	public void testShouldFindBookByLibraryName() {
		// given
		String libraryName = "Biblioteka Wroclawska";
		BookSearchCriteria bookSearchCriteria = new BookSearchCriteria(null, null, libraryName);
		// when
		List<BookEntity> allBooks = bookSearchCriteriaRepository.findBooksByCriteria(bookSearchCriteria);
		// then
		assertFalse(allBooks.isEmpty());
		assertEquals(2, allBooks.size());
		assertEquals("Trzecia książka", allBooks.get(1).getTitle());
	}

	@Test
	public void testShouldFindBookByLibraryNameAndByTitle() {
		// given
		String libraryName = "Biblioteka Wroclawska";
		String bookName = "Pierwsza książka";
		BookSearchCriteria bookSearchCriteria = new BookSearchCriteria(bookName, null, libraryName);
		// when
		List<BookEntity> allBooks = bookSearchCriteriaRepository.findBooksByCriteria(bookSearchCriteria);
		// then
		assertFalse(allBooks.isEmpty());
		assertEquals(1, allBooks.size());
		assertEquals("Pierwsza książka", allBooks.get(0).getTitle());
	}
}
