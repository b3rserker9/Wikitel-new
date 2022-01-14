package it.cnr.istc.psts.wikitel.Service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.cnr.istc.psts.wikitel.Repository.FileRepository;
import it.cnr.istc.psts.wikitel.db.FileEntity;
import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.UserEntity;


@Service
public class FileService {
	
	@Autowired
	private FileRepository filerepository;
	
	@Transactional
	public FileEntity save(MultipartFile file) throws IOException {
	String name = file.getOriginalFilename();
	System.out.println(file.getBytes());
	try {
		FileEntity f = new FileEntity(name, file.getContentType(), file.getBytes());
		filerepository.save(f);	
		return f;			
	}
	catch(Exception e) {
	    e.printStackTrace();
	return null;
	}
	}
	
	public FileEntity getfile(Long id){
		
		Optional<FileEntity> result =filerepository.findById(id);
		return result.orElse(null);
	}
	
	

}
