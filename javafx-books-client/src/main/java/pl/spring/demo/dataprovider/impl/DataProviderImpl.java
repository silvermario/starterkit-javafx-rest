package pl.spring.demo.dataprovider.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

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

	private Collection<BookEntity> books = new ArrayList<>();
	private ObjectMapper objectMapper = new ObjectMapper();

	private static HttpURLConnection conn;
	private static BufferedReader br;

	public DataProviderImpl() {

		// TODO: delete, it's for local testing purposes
		// createDummydata();
	}

	public void createDummydata() {

		Set<AuthorEntity> authorsSet = new HashSet<AuthorEntity>();
		BookEntity book1 = new BookEntity(1L, "Pierwsza ksiazka 1");
		authorsSet.add(new AuthorEntity(1L, "Mariusz", "Kopec"));
		book1.setAuthors(authorsSet);

		BookEntity book2 = new BookEntity(1L, "Druga ksiazka 2");
		authorsSet.add(new AuthorEntity(1L, "Mariusz", "Kopec"));
		book2.setAuthors(authorsSet);

		BookEntity book3 = new BookEntity(1L, "Trzecia ksiazka");
		authorsSet.add(new AuthorEntity(1L, "Mariusz", "Kopec"));
		book3.setAuthors(authorsSet);

		books.add(book1);
		books.add(book2);
		books.add(book3);

	}

	private static String getJSON_DataFromRestPoint(String query) throws IOException {
		try {
			URL url = new URL(query);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != 200) {
				return null;
			}
			br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String jsonData = br.readLine();
			conn.disconnect();
			return jsonData;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public Collection<BookEntity> findAllBooks() {
		LOG.debug("Entering findAllBooks()");

		try {
			String jsonDataResponse = getJSON_DataFromRestPoint("http://localhost:8080/api/books/");

			if (jsonDataResponse != null) {

				books = Arrays.asList(objectMapper.readValue(jsonDataResponse, BookEntity[].class));
			}
		} catch (IOException e) {
			LOG.debug("IOException" + e);
			e.printStackTrace();
			return null;
		}
		return books;
	}

	@Override
	public Collection<BookEntity> findBooksByTitleDummyData(String title) {
		LOG.debug("Entering findBooksByTitle()");

		Collection<BookEntity> result = books.stream().filter(b -> (b.getTitle().contains(title)) //
		).collect(Collectors.toList());
		LOG.debug("result: " + result);
		return result;
	}

	@Override
	public Collection<BookEntity> findBooksByTitle(String title) {
		LOG.debug("Entering findBooksByTitle()");
		
		try {
			String jsonDataResponse = getJSON_DataFromRestPoint("http://localhost:8080/api/books/title/" + title);

			if (jsonDataResponse != null) {

				books = Arrays.asList(objectMapper.readValue(jsonDataResponse, BookEntity[].class));
			}
		} catch (IOException e) {
			LOG.debug("IOException" + e);
			e.printStackTrace();
			return null;
		}
		LOG.debug("Leaving findBooksByTitle()");
		return books;
	}
}
