package it.cnr.istc.psts.wikitel.Repository;

import org.springframework.data.repository.CrudRepository;

import it.cnr.istc.psts.wikitel.db.FileEntity;


public interface FileRepository extends CrudRepository<FileEntity, Long> {

}
