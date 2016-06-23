package pl.spring.demo.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import pl.spring.demo.dataprovider.DataProvider;
import pl.spring.demo.model.AuthorEntity;
import pl.spring.demo.model.BookEntity;
import pl.spring.demo.model.BookSearch;

/**
 * Controller for the Book Search screen
 * 
 * @author MAKOPEC
 *
 */
public class BookSearchController {

	private static final Logger LOG = Logger.getLogger(BookSearchController.class);

	/**
	 * Resource bundle loaded with this controller. JavaFX injects a resource
	 * bundle specified in {@link FXMLLoader#load(URL, ResourceBundle)} call.
	 * <p>
	 * NOTE: The variable name must be {@code resources}.
	 * </p>
	 */
	@FXML
	private ResourceBundle resources;

	/**
	 * URL of the loaded FXML file. JavaFX injects an URL specified in
	 * {@link FXMLLoader#load(URL, ResourceBundle)} call.
	 * <p>
	 * NOTE: The variable name must be {@code location}.
	 * </p>
	 */
	@FXML
	private URL location;

	/**
	 * JavaFX injects an object defined in FXML with the same "fx:id" as the
	 * variable name.
	 */
	@FXML
	private TextField titleField;

	@FXML
	private TextField newTitle;

	@FXML
	private TextField newAuthorFirstName;

	@FXML
	private TextField newAuthorLastName;

	@FXML
	private Button searchButton;

	@FXML
	private Button addBookButton;

	@FXML
	private TableView<BookEntity> resultTable;

	@FXML
	private TableColumn<BookEntity, Long> idColumn;

	@FXML
	private TableColumn<BookEntity, String> titleColumn;

	@FXML
	private TableColumn<BookEntity, Set<AuthorEntity>> authorsColumn;

	private final DataProvider dataProvider = DataProvider.INSTANCE;

	private final BookSearch model = new BookSearch();

	public BookSearchController() {
		LOG.debug("Constructor: titleField = " + titleField);
	}

	@FXML
	private void initialize() {
		LOG.debug("initialize(): titleField = " + titleField);

		initializeResultTable();

		/*
		 * Bind controls properties to model properties.
		 */
		titleField.textProperty().bindBidirectional(model.titleProperty());
		resultTable.itemsProperty().bind(model.resultProperty());

		model.setTitle("");

		/*
		 * Make the add book button inactive when both fields are empty
		 */
		addBookButton.disableProperty()
				.bind(Bindings.isEmpty(newTitle.textProperty()).or(Bindings.isEmpty(newAuthorFirstName.textProperty()))
						.or(Bindings.isEmpty(newAuthorLastName.textProperty())));

	}

	private void initializeResultTable() {

		idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
		titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
		/*
		 * authorsColumn.setCellValueFactory(cellData -> new
		 * ReadOnlyStringWrapper(
		 * cellData.getValue().getAuthors().iterator().next().getFirstName() +
		 * " " +
		 * cellData.getValue().getAuthors().iterator().next().getLastName()));
		 */
		authorsColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAuthors()));
		authorsColumn.setCellFactory(column -> {
			return new TableCell<BookEntity, Set<AuthorEntity>>() {
				@Override
				protected void updateItem(Set<AuthorEntity> authorsSet, boolean empty) {

					super.updateItem(authorsSet, empty);
					StringBuilder authorsStringBuilder = new StringBuilder();

					if (empty) {
						setText(null);
						setStyle("");

					} else {

						for (AuthorEntity author : authorsSet) {
							authorsStringBuilder.append(author.getFirstName() + " " + author.getLastName() + " ");
						}
						setText(authorsStringBuilder.toString());
					}
				}
			};
		});

		/*
		 * Show specific text for an empty table. This can also be done in FXML.
		 */
		emptyResultTable();

		/*
		 * Delete action menu context
		 */
		resultTable.setRowFactory(new Callback<TableView<BookEntity>, TableRow<BookEntity>>() {
			@Override
			public TableRow<BookEntity> call(TableView<BookEntity> tableView) {
				final TableRow<BookEntity> row = new TableRow<>();
				final ContextMenu contextMenu = new ContextMenu();
				final MenuItem removeMenuItem = new MenuItem("Delete");
				removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						BookEntity selectedBook = resultTable.getSelectionModel().getSelectedItem();
						LOG.debug(selectedBook);
						try {
							deleteBook(selectedBook, row);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				contextMenu.getItems().add(removeMenuItem);
				// Set context menu on row, but use a binding to make it only
				// show for non-empty rows:
				row.contextMenuProperty()
						.bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
				return row;
			}
		});
	}

	/**
	 * The JavaFX runtime calls this method when the <b>Search</b> button is
	 * clicked.
	 *
	 * @param event
	 *            {@link ActionEvent} holding information about this event
	 * @throws Exception
	 */
	@FXML
	private void searchButtonAction(ActionEvent event) throws Exception {
		LOG.debug("'Search' button clicked");

		searchButtonAction();
	}

	private void searchButtonAction() throws Exception {

		Task<Collection<BookEntity>> backgroundTask = new Task<Collection<BookEntity>>() {

			@Override
			protected Collection<BookEntity> call() throws Exception {
				LOG.debug("call() called");

				Collection<BookEntity> result;

				try {
					if (model.getTitle().equals("") || model.getTitle() == null) {
						result = dataProvider.findAllBooks();
					} else {
						result = dataProvider.findBooksByTitle(model.getTitle());
					}

					return result;

				} catch (Exception e) {
					LOG.debug( e);
					throw new Exception();
					// return null;
				}

			}

			/**
			 * This method will be executed in the JavaFX Application Thread
			 * when the task finishes.
			 */
			@Override
			protected void succeeded() {
				LOG.debug("succeeded() called");

				/*
				 * Get result of the task execution.
				 */
				Collection<BookEntity> result = getValue();

				/*
				 * Copy the result to model.
				 */
				if(result == null) {
					model.setResult(null);
					emptyResultTable();
				} else {
					model.setResult(new ArrayList<BookEntity>(result));
					
				}

				/*
				 * Reset sorting in the result table.
				 */
				resultTable.getSortOrder().clear();
			}

		};

		new Thread(backgroundTask).start();

		exceptionHandling(backgroundTask);
	}

	@FXML
	public void addBookAction(ActionEvent event) throws Exception {

		addBookAction();

	}

	public void addBookAction() throws Exception {

		Task<Void> backgroundTask = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				saveBook();
				LOG.debug("Saving book");
				return null;
			}

			@Override
			protected void succeeded() {

				showMessageBox("Book added!");
			}
		};
		new Thread(backgroundTask).start();

		exceptionHandling(backgroundTask);

	}

	public void saveBook() throws Exception {

		Set<AuthorEntity> authors = new HashSet<>();
		authors.add(new AuthorEntity(newAuthorFirstName.getText(), newAuthorLastName.getText()));
		BookEntity book = new BookEntity(null, newTitle.getText());
		book.setAuthors(authors);

		dataProvider.addBook(book);

		clearTextFields();

	}

	public void deleteBook(BookEntity book, TableRow<BookEntity> row) throws Exception {

		Task<Void> backgroundTask = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				dataProvider.deleteBook(book);
				LOG.debug("Deleting book");
				return null;
			}

			@Override
			protected void succeeded() {

				showMessageBox("Book deleted!");
				resultTable.getItems().remove(row.getItem());
			}
		};
		new Thread(backgroundTask).start();

		exceptionHandling(backgroundTask);

	}

	private void showMessageBox(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info!");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void clearTextFields() {
		newAuthorFirstName.setText("");
		newAuthorLastName.setText("");
		newTitle.setText("");
	}

	private void exceptionHandling(Task<?> taskName) {
		// observe throwed exceptions
		taskName.exceptionProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				Exception ex = (Exception) newValue;
				ex.printStackTrace();
				showMessageBox("Server connection problem!");
			}
		});
	}
	
	private void emptyResultTable(){
		resultTable.setPlaceholder(new Label(resources.getString("table.emptyText")));
	}
}
