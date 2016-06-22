package pl.spring.demo.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * Data displayed on the person search screen.
 *
 * @author Leszek
 */
public class BookSearch {

	private final StringProperty title = new SimpleStringProperty();
	private final StringProperty author = new SimpleStringProperty();
	private final ListProperty<BookEntity> result = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<>()));

	public final String getTitle() {
		return title.get();
	}

	public final void setTitle(String value) {
		title.set(value);
	}

	public StringProperty titleProperty() {
		return title;
	}

	public final String getAuthor() {
		return author.get();
	}

	public final void setAuthor(String value) {
		author.set(value);
	}

	public StringProperty authorProperty() {
		return author;
	}

	public final List<BookEntity> getResult() {
		return result.get();
	}

	public final void setResult(List<BookEntity> value) {
		result.setAll(value);
	}

	public ListProperty<BookEntity> resultProperty() {
		return result;
	}

	@Override
	public String toString() {
		return "BookSearch [title=" + title + ", author=" + author + ", result=" + result + "]";
	}

	

}
