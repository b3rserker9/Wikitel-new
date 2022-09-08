package it.cnr.istc.psts.wikitel.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.cnr.istc.psts.wikitel.MongoRepository.RuleMongoRepository;
import it.cnr.istc.psts.wikitel.MongoRepository.SuggestionMRepository;
import it.cnr.istc.psts.wikitel.Mongodb.RuleMongo;
import it.cnr.istc.psts.wikitel.Mongodb.SuggestionM;
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
import net.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer.ForReturnTypeName;

@Service
public class ModelService {

	@Autowired
	private ModelRepository modelRepository;
	
	 @Autowired
	 private WikiSuggestionRepository wikisuggestionrepository;
	 
	 @Autowired
	 private RuleRepository rulerepository;
	 
	 @Autowired
	 private RuleMongoRepository rulemongorep;
	 
	 @Autowired
	 private LessonService lessonservice;
	 
	 @Autowired
	 private UserService userservice;
	 
	 @Autowired
	 private SuggestionMRepository suggestionm;
	
	 
		@Transactional
	    public SuggestionM savesm(SuggestionM sm) {
	        return this.suggestionm.save(sm);
	    }
		
		 @Transactional
			public SuggestionM getsuggestion(String id) {
				Optional<SuggestionM> result  = this.suggestionm.findById(id);
				return result.orElse(null);
			}
	 
	 @Transactional
		public RuleMongo getrulemongoname(String id) {
			Optional<RuleMongo> result  = this.rulemongorep.findByTitle(id);
			return result.orElse(null);
		}
	 
	 @Transactional
	 public List<RuleMongo> getmongotopics(String id) {
			List<RuleMongo> result  =  this.rulemongorep.findtopics(id);
			return result;
		}
	 
	 @Transactional
		public RuleMongo getrulemongo(String id) {
			Optional<RuleMongo> result  = this.rulemongorep.findById(id);
			return result.orElse(null);
		}
		
	
	@Transactional
    public ModelEntity save(ModelEntity model) {
        return this.modelRepository.save(model);
    }
	
	@Transactional
    public void delete(Long id, UserEntity user) {
		ModelEntity m = this.getModel(id);
		m.getTeachers().stream().forEach(t -> t.getModels().remove(m)); 
		for(LessonEntity l : user.getTeaching_lessons()) {
			if(l.getModel().getId()==id) {
				l.setModel(null);
				this.lessonservice.delete(l);
				
			}
		}
		userservice.saveUser(user);
         this.modelRepository.deleteById(id);
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
    public String getText(Long id) {
        return this.rulerepository.findNameBytext(id);
    }
	
	@Transactional
    public String getFile(Long id) {
        return this.rulerepository.findNameByfile(id);
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
