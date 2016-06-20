package pl.spring.demo.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import pl.spring.demo.dao.BookDaoImplTest;
import pl.spring.demo.dao.LibraryDaoImplTest;
import pl.spring.demo.repository.BookRepositoryTest;
import pl.spring.demo.repository.BookSearchCriteriaRepositoryTest;
import pl.spring.demo.repository.LibraryRepositoryTest;
import pl.spring.demo.service.LibraryServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ BookDaoImplTest.class,
				LibraryDaoImplTest.class,
				BookRepositoryTest.class,
				BookSearchCriteriaRepositoryTest.class,
				LibraryRepositoryTest.class,
				LibraryServiceTest.class })
public class AllTests {

}
