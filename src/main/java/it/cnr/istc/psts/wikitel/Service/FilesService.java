package it.cnr.istc.psts.wikitel.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.cnr.istc.psts.wikitel.Repository.FilesRepository;
import it.cnr.istc.psts.wikitel.db.Files;

@Service
public class FilesService {

	
	@Autowired
	private FilesRepository filerepository;
	
	@Transactional
    public Files save(Files f) {
        return this.filerepository.save(f);
    }
	
	@Transactional
	public Files filePerId(Long id) {
		Optional<Files> result = filerepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public List<Files> all() {
		return (List<Files>) filerepository.findAll();
	}
}
