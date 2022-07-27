package it.cnr.istc.psts.wikitel.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.cnr.istc.psts.wikitel.Repository.RuleSuggestionRelationRepository;
import it.cnr.istc.psts.wikitel.db.RuleSuggestionRelationEntity;
import it.cnr.istc.psts.wikitel.db.RuleSuggestionRelationId;

@Service
public class RuleSuggestionRelationService {

	 @Autowired
	 private RuleSuggestionRelationRepository relationrep;
	 
	 @Transactional
	    public RuleSuggestionRelationEntity saverelation(RuleSuggestionRelationEntity relation ) {
	     RuleSuggestionRelationId id = new RuleSuggestionRelationId();
	     id.setRule_id(relation.getRule().getId());
	     id.setSuggestion_id(relation.getSuggestion().getId());
	     relation.setId(id);
		 
		 return this.relationrep.save(relation);
	    }
	 @Transactional
	 public void delete(RuleSuggestionRelationEntity ruleSuggestionRelationId) {
		 this.relationrep.delete(ruleSuggestionRelationId);
	 }
	 
}
