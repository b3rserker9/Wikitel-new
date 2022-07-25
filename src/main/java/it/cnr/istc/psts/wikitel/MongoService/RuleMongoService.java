package it.cnr.istc.psts.wikitel.MongoService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.cnr.istc.psts.wikitel.MongoRepository.RuleMongoRepository;
import it.cnr.istc.psts.wikitel.Mongodb.RuleMongo;
import it.cnr.istc.psts.wikitel.db.ModelEntity;

@Service
public class RuleMongoService {

	@Autowired
	private RuleMongoRepository rulemongrep;
	
	
	@Transactional
	public RuleMongo getRule(String id) {
		Optional<RuleMongo> result  = this.rulemongrep.findById(id);
		return result.orElse(null);
	}
}
