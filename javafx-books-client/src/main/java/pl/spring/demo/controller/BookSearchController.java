package pl.spring.demo.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import pl.spring.demo.dataprovider.DataProvider;
import pl.spring.demo.dataprovider.data.SexVO;
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
	private TextField newAuthors;

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

	/**
	 * The JavaFX runtime instantiates this controller.
	 * <p>
	 * The @FXML annotated fields are not yet initialized at this point.
	 * </p>
	 */
	public BookSearchController() {
		LOG.debug("Constructor: titleField = " + titleField);
	}

	/**
	 * The JavaFX runtime calls this method after loading the FXML file.
	 * <p>
	 * The @FXML annotated fields are initialized at this point.
	 * </p>
	 * <p>
	 * NOTE: The method name must be {@code initialize}.
	 * </p>
	 */
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
				.bind(Bindings.isEmpty(newTitle.textProperty()).or(Bindings.isEmpty(newAuthors.textProperty())));

	}

	private void initializeResultTable() {
		/*
		 * Define what properties of BookEntity will be displayed in different
		 * columns.
		 */
		idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
		titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
		authorsColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAuthors()));
		// titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		/*
		 * Show specific text for an empty table. This can also be done in FXML.
		 */
		resultTable.setPlaceholder(new Label(resources.getString("table.emptyText")));

		/*
		 * When table's row gets selected say given person's name.
		 */
		resultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BookEntity>() {

			@Override
			public void changed(ObservableValue<? extends BookEntity> observable, BookEntity oldValue,
					BookEntity newValue) {
				LOG.debug(newValue + " selected");

				if (newValue != null) {
					Task<Void> backgroundTask = new Task<Void>() {

						@Override
						protected Void call() throws Exception {
							// speaker.say(newValue.getName());
							// resultTable.setContextMenu(initContextmenu());
							return null;
						}

						@Override
						protected void failed() {
							LOG.error("Could not say name: " + newValue.getTitle(), getException());
						}
					};
					new Thread(backgroundTask).start();
				}
			}
		});

		resultTable.setRowFactory(new Callback<TableView<BookEntity>, TableRow<BookEntity>>() {
			@Override
			public TableRow<BookEntity> call(TableView<BookEntity> tableView) {
				final TableRow<BookEntity> row = new TableRow<>();
				final ContextMenu contextMenu = new ContextMenu();
				final MenuItem removeMenuItem = new MenuItem("Delete");
				removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						LOG.debug(resultTable.getSelectionModel().getSelectedItem().toString());
						showMessageBox("Book Deleted!");
						resultTable.getItems().remove(row.getItem());
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
	 */
	@FXML
	private void searchButtonAction(ActionEvent event) {
		LOG.debug("'Search' button clicked");

		searchButtonAction();
	}

	private void searchButtonAction() {
		/*
		 * Use task to execute the potentially long running call in background
		 * (separate thread), so that the JavaFX Application Thread is not
		 * blocked.
		 */
		Task<Collection<BookEntity>> backgroundTask = new Task<Collection<BookEntity>>() {

			/**
			 * This method will be executed in a background thread.
			 */
			@Override
			protected Collection<BookEntity> call() throws Exception {
				LOG.debug("call() called");

				Collection<BookEntity> result;
				
				if(model.getTitle() == "" || model.getTitle() == null) {
					result = dataProvider.findAllBooks();
				} else {
					result = dataProvider.findBooksByTitle(model.getTitle());
				}
				
				LOG.debug("model.getTitle(): " + model.getTitle());
				/*
				 * Value returned from this method is stored as a result of task
				 * execution.
				 */
				return result;
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
				model.setResult(new ArrayList<BookEntity>(result));

				/*
				 * Reset sorting in the result table.
				 */
				resultTable.getSortOrder().clear();
			}
		};
		
		new Thread(backgroundTask).start();
	}

	@FXML
	public void addBook() throws Exception {

		Task<Void> backgroundTask = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				// saveBook();
				LOG.debug("Saving book");
				return null;
			}
		};
		new Thread(backgroundTask).start();
	}

	private void showMessageBox(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info!");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
