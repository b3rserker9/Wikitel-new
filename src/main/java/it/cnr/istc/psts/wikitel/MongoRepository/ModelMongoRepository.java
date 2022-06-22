package it.cnr.istc.psts.wikitel.MongoRepository;

import it.cnr.istc.psts.wikitel.db.modelmongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModelMongoRepository extends MongoRepository<modelmongo, String>{

}
