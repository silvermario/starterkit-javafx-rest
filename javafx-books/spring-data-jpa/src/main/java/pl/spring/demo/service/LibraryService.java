package pl.spring.demo.service;

import org.springframework.stereotype.Service;

import pl.spring.demo.entity.LibraryEntity;

public interface LibraryService {

	public void deleteLibrary(LibraryEntity library);

}
