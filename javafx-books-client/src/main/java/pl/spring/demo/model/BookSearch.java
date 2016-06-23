package pl.spring.demo.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

/**
 * Data displayed on the person search screen.
 *
 * @author Leszek
 */
public class BookSearch {

	private final StringProperty title = new SimpleStringProperty();
	//private final StringProperty author = new SimpleStringProperty();
	private final SetProperty<AuthorEntity> author = new SimpleSetProperty<>();
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

	public final ObservableSet<AuthorEntity> getAuthor() {
		return author.get();
	}

	public final void setAuthor(ObservableSet<AuthorEntity> value) {
		author.set(value);
	}

	public SetProperty<AuthorEntity> authorProperty() {
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
