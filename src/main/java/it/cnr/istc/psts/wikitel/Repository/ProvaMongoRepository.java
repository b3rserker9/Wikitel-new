package it.cnr.istc.psts.wikitel.Repository;
import it.cnr.istc.psts.wikitel.db.Provamongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProvaMongoRepository extends MongoRepository<Provamongo, String>{

}
