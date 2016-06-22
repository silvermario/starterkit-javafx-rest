package pl.spring.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.spring.demo.model.LibraryEntity;
import pl.spring.demo.repository.LibraryRepository;
import pl.spring.demo.service.LibraryService;

@Service
public class LibraryServiceImpl implements LibraryService {

	@Autowired
	LibraryRepository libraryRepository;
	
	@Override
	public void deleteLibrary(LibraryEntity library) {
		if(library != null) {
			libraryRepository.delete(library);			
		}
	}

}
