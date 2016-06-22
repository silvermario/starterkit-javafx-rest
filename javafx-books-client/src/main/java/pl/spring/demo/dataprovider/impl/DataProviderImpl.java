package pl.spring.demo.dataprovider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import pl.spring.demo.dataprovider.DataProvider;
import pl.spring.demo.model.AuthorEntity;
import pl.spring.demo.model.BookEntity;

/**
 * Provides data. Data is stored locally in this object. Additionally a call
 * delay is simulated.
 *
 * @author Leszek
 */
public class DataProviderImpl implements DataProvider {

	private static final Logger LOG = Logger.getLogger(DataProviderImpl.class);

	/**
	 * Delay (in ms) for method calls.
	 */
	private static final long CALL_DELAY = 3000;

	private Collection<BookEntity> books = new ArrayList<>();

	public DataProviderImpl() {
		
		createDummydata();
	}
	
	
	public void createDummydata(){
		
		Set<AuthorEntity> authorsSet = new HashSet<AuthorEntity>();
		BookEntity book1 = new BookEntity(1L, "Pierwsza ksiazka 1");
		authorsSet.add(new AuthorEntity(1L, "Mariusz","Kopec"));
		book1.setAuthors(authorsSet);
		
		BookEntity book2 = new BookEntity(1L, "Druga ksiazka 2");
		authorsSet.add(new AuthorEntity(1L, "Mariusz","Kopec"));
		book2.setAuthors(authorsSet);
		
		
		BookEntity book3 = new BookEntity(1L, "Trzecia ksiazka");
		authorsSet.add(new AuthorEntity(1L, "Mariusz","Kopec"));
		book3.setAuthors(authorsSet);		
		
		books.add(book1);
		books.add(book2);
		books.add(book3);
		
	}

	@Override
	public Collection<BookEntity> findAllBooks() {
		LOG.debug("Entering findAllBooks()");

		/*
		 * Simulate a call delay.
		 */
		try {
			Thread.sleep(CALL_DELAY);
		} catch (InterruptedException e) {
			throw new RuntimeException("Thread interrupted", e);
		}


		LOG.debug("Leaving findAllBooks()");
		//return result;
		return books;
	}

	@Override
	public Collection<BookEntity> findBooksByTitle(String title) {
		LOG.debug("Entering findBooksByTitle()");
		
		Collection<BookEntity> result = books.stream().filter(b -> //
		/*((name == null || name.isEmpty()) || (name != null && !name.isEmpty() && p.getName().contains(name))) //
				&& //
				((sex == null) || (sex != null && p.getSex() == sex)) //
		).collect(Collectors.toList());*/
		(b.getTitle().contains(title)) //
		).collect(Collectors.toList());
		LOG.debug("Leaving findAllBooks()");
		LOG.debug("result: " + result);
		return result;
	}
}
