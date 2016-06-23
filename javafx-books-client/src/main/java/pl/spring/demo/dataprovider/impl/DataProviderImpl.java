package pl.spring.demo.dataprovider.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.spring.demo.dataprovider.DataProvider;
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

	// REV: adres serwera powinien byc pobrany z konfiguracji
	private static final String API_URL = "http://localhost:8080/api";
	
	// REV: static'i to zly pomysl w tej sytuacji
	private static HttpURLConnection conn;
	private static BufferedReader br;

	public DataProviderImpl() {
	}

	private static String getJSON_DataFromREST(String query) throws IOException, MalformedURLException {
		try {
			URL url = new URL(query);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != 200) {
				// REV: Trzeba rozroznic pusty wynik od bledu 
				return null;
			}
			br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String jsonData = br.readLine();
			// REV: nie zamykasz polaczenia przy bledzie
			conn.disconnect();
			return jsonData;
		} catch (IOException e) {
			// REV: bleda nalezy logowac na levelu ERROR i przekazac wyjatek
			LOG.debug("IOException" + e);
			// REV: printStackTrace to zlo
			e.printStackTrace();
			// REV: dlaczego MalformedURLException? exception chaining
			throw new MalformedURLException();
			// return null;
		}
	}

	private static HttpURLConnection prepareConnection(String myUrl, String method) throws IOException {

		// REV: lepiej uzyc frameworka do RESTow
		URL url = new URL(myUrl);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);

		conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestMethod(method);

		return conn;
	}

	@Override
	public Collection<BookEntity> findAllBooks() throws Exception {
		LOG.debug("Entering findAllBooks()");

		try {
			String jsonDataResponse = getJSON_DataFromREST(API_URL + "/books/");

			if (jsonDataResponse != null) {

				books = Arrays.asList(objectMapper.readValue(jsonDataResponse, BookEntity[].class));
			}
		} catch (Exception e) {
			// REV: j.w.
			LOG.debug("IOException" + e);
			e.printStackTrace();
			throw new Exception();
			// return null;
		}
		return books;
	}

	@Override
	public Collection<BookEntity> findBooksByTitle(String title) throws Exception {

		try {
			String jsonDataResponse = getJSON_DataFromREST(API_URL + "/books/title/" + title);

			if (jsonDataResponse != null) {

				books = Arrays.asList(objectMapper.readValue(jsonDataResponse, BookEntity[].class));
			}
		} catch (IOException e) {
			// REV: j.w.
			LOG.debug("IOException" + e);
			throw new Exception();
		}
		return books;
	}

	@Override
	public void addBook(BookEntity book) throws MalformedURLException, IOException {

		byte[] jsonBytes = objectMapper.writeValueAsBytes(book);

		conn = prepareConnection(API_URL + "/books/", "POST");

		OutputStream os = conn.getOutputStream();
		os.write(jsonBytes);
		os.close();

		int HttpResult = conn.getResponseCode();
		if (HttpResult != HttpURLConnection.HTTP_CREATED) {
			LOG.debug("HTTP response code: " + HttpResult);
			// REV: zawsze ustawiaj message w wyjatku, dlaczego IOException?
			throw new IOException();
		}
		// REV: j.w.
		conn.disconnect();

	}

	@Override
	public void deleteBook(BookEntity book) throws IOException {

		byte[] jsonBytes = objectMapper.writeValueAsBytes(book);
		
		Long bookId = book.getId();
		
		conn = prepareConnection(API_URL + "/books/" + bookId, "DELETE");
		//conn.setRequestMethod("DELETE");

		OutputStream os = conn.getOutputStream();
		os.write(jsonBytes);
		os.flush();
		os.close();

		int HttpResult = conn.getResponseCode();
		if (HttpResult != HttpURLConnection.HTTP_OK) {
			LOG.debug("HTTP response code: " + HttpResult);
			// REV: j.w.
			throw new IOException();
		}
		// REV: j.w.
		conn.disconnect();

	}
}
