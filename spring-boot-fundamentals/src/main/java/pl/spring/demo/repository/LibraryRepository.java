package pl.spring.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.spring.demo.model.LibraryEntity;

@Repository
public interface LibraryRepository extends JpaRepository<LibraryEntity, Long> {

	  @Query("select library from LibraryEntity library where upper(library.name) like concat(upper(:name), '%')")
	    public List<LibraryEntity> findLibraryByName(@Param("name") String name);
	
}
