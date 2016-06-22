package pl.spring.demo.searchcriteria;


public class BookSearchCriteria {

	private String title;
	private String author;
	private String libraryName;

	public BookSearchCriteria(){
		
	}
	

	public BookSearchCriteria(String title, String author, String libraryName) {
		this.title = title;
		this.author = author;
		this.libraryName = libraryName;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getLibraryName() {
		return libraryName;
	}

}
