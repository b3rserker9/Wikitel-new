package it.cnr.istc.psts.wikitel.MongoRepository;
import it.cnr.istc.psts.wikitel.Mongodb.RuleMongo;
import it.cnr.istc.psts.wikitel.Mongodb.SuggestionMongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RuleMongoRepository extends MongoRepository<RuleMongo, String>{
	
	RuleMongo findByTitle(String name);
	
	@Query(value= "{id : ?0}", fields="{'suggestions':1, 'id':0}")
	RuleMongo findSug(String id);

}
