package pl.spring.demo.service;

import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import pl.spring.demo.entity.LibraryEntity;
import pl.spring.demo.repository.BookRepository;
import pl.spring.demo.repository.LibraryRepository;

@ContextConfiguration(locations = "CommonServiceTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class LibraryServiceTest {

	@Autowired
	LibraryRepository libraryRepository;
	
	@Autowired
	LibraryService libraryService;
	
	@Autowired
	BookRepository bookRepository;
	
	@Test
	public void testShouldDeleteLibraryWithBooks() {
		// given
		final long libraryId = 1;
		LibraryEntity library = libraryRepository.getOne(libraryId);
		// when
		libraryService.deleteLibrary(library);
		// then
		assertEquals(1, bookRepository.count());
		assertEquals(2, libraryRepository.count());
		assertEquals(null, libraryRepository.findOne(libraryId));		
	}

}
