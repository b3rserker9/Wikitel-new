package it.cnr.istc.psts.wikitel.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.cnr.istc.psts.wikitel.Repository.ModelRepository;
import it.cnr.istc.psts.wikitel.Repository.RuleRepository;
import it.cnr.istc.psts.wikitel.Repository.RuleSuggestionRelationRepository;
import it.cnr.istc.psts.wikitel.Repository.WikiSuggestionRepository;
import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.RuleEntity;
import it.cnr.istc.psts.wikitel.db.RuleSuggestionRelationEntity;
import it.cnr.istc.psts.wikitel.db.UserEntity;
import it.cnr.istc.psts.wikitel.db.WikiSuggestionEntity;

@Service
public class ModelService {

	@Autowired
	private ModelRepository modelRepository;
	
	 @Autowired
	 private WikiSuggestionRepository wikisuggestionrepository;
	 
	 @Autowired
	 private RuleRepository rulerepository;
	 
	
	
	@Transactional
    public ModelEntity save(ModelEntity model) {
        return this.modelRepository.save(model);
    }
	
	@Transactional
	public List<ModelEntity> getModelTeacher(UserEntity teacher) {
		List<ModelEntity> result = this.modelRepository.findByTeachers(teacher);
		return result;
	}
	
	@Transactional
	public ModelEntity getModel(Long id) {
		Optional<ModelEntity> result  = this.modelRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public WikiSuggestionEntity getpage(String page) {
		Optional<WikiSuggestionEntity> result = this.wikisuggestionrepository.findByPage(page);
		return result.orElse(null);
	}
	
	@Transactional
    public WikiSuggestionEntity savewikisuggestion(WikiSuggestionEntity wikisugg) {
        return this.wikisuggestionrepository.save(wikisugg);
    }
	
	@Transactional
    public RuleEntity saverule(RuleEntity rule) {
        return this.rulerepository.save(rule);
    }
	
	
	
	@Transactional
	public RuleEntity getRule(Long id) {
		Optional<RuleEntity> result = this.rulerepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public String getRuleName(Long id) {
		
		return this.rulerepository.findNameById(id);
	}
}
