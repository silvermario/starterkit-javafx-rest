package pl.spring.demo.dao;

import pl.spring.demo.entity.LibraryEntity;

import java.util.List;

public interface LibraryDao extends Dao<LibraryEntity, Long> {

    List<LibraryEntity> findLibrariesByName(String name);
}
