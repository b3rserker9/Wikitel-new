package it.cnr.istc.psts.wikitel.MongoRepository;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.cnr.istc.psts.wikitel.Mongodb.RuleMongo;
import it.cnr.istc.psts.wikitel.Mongodb.SuggestionM;

public interface SuggestionMRepository extends MongoRepository<SuggestionM, String> {

}
