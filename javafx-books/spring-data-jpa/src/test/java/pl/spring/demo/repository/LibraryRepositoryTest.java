package pl.spring.demo.repository;

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

@ContextConfiguration(locations = "CommonRepositoryTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class LibraryRepositoryTest {

	@Autowired
	LibraryRepository libraryRepository;
	
	@Autowired
	BookRepository bookRepository;
	
	@Test
	public void testShouldFindLibrariesByName() {
		// given
		String prefix = "bibl";
		// when
		List<LibraryEntity> foundLibraries = libraryRepository.findLibraryByName(prefix);
		// then
		assertEquals(2, foundLibraries.size());
		assertEquals("Biblioteka Wroclawska", foundLibraries.get(0).getName());
		assertEquals("Biblioteka Ossolineum", foundLibraries.get(1).getName());
	}

}
