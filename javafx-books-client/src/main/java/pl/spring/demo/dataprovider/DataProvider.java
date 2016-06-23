package pl.spring.demo.dataprovider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;

import pl.spring.demo.dataprovider.impl.DataProviderImpl;
import pl.spring.demo.model.BookEntity;

/**
 * Provides data.
 *
 * @author Leszek
 */
public interface DataProvider {

	/**
	 * Instance of this interface.
	 */
	DataProvider INSTANCE = new DataProviderImpl();

	/**
	 * Finds persons with their name containing specified string and/or given
	 * sex.
	 *
	 * @param name
	 *            string contained in name
	 * @param sex
	 *            sex
	 * @return collection of persons matching the given criteria
	 * @throws Exception 
	 * @throws IOException 
	 */
	Collection<BookEntity> findAllBooks() throws Exception;
	
	Collection<BookEntity> findBooksByTitle(String title) throws Exception;
	
	void addBook(BookEntity book) throws MalformedURLException, IOException;
	void deleteBook(BookEntity book) throws IOException;
	
	
}
