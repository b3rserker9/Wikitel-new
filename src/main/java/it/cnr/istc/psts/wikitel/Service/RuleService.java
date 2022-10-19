package it.cnr.istc.psts.wikitel.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.cnr.istc.psts.Websocket.Sending;
import it.cnr.istc.psts.wikitel.Repository.RuleRepository;
import it.cnr.istc.psts.wikitel.controller.LessonManager;
import it.cnr.istc.psts.wikitel.controller.MainController;
import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.RuleEntity;
import it.cnr.istc.psts.wikitel.db.UserEntity;

@Service
public class RuleService {
	
	 @Autowired
	 private RuleRepository rulerepository;
	 
	 @Autowired
	 private ModelService modelservice;
	 
	 @Autowired
	 private LessonService lessonservice;
	 
	 @Autowired
	 private UserService userservice;
	 @Autowired
	 private RuleService ruleservice;
	 
	 @Autowired
		private Sending send;
		@Transactional
	    public RuleEntity saverule(RuleEntity rule) {
	        return this.rulerepository.save(rule);
	    }

		
		@Transactional
	    public void delete(Long m, Long r) {
			ModelEntity model = this.modelservice.getModel(m);
			RuleEntity rule = this.getRule(r);
			model.getRules().remove(rule);
			for(RuleEntity ru : model.getRules()) {
				if(ru.getPreconditions().contains(rule)) {
					ru.removePrecondition(rule);
				}
			}
			this.modelservice.save(model);
			List<UserEntity> user = new ArrayList<>();
			for(LessonEntity l : this.lessonservice.getlessonbymodel(model)) {
				if(l.getGoals().contains(rule)) {
				l.getGoals().remove(rule);
				lessonservice.save(l);
				
				user.addAll(l.getFollowed_by());
				l.getFollowed_by().clear();
				for(UserEntity u : user) {	
					System.out.println("PROVAA");
					File lesson_file = new File(System.getProperty("user.dir")+"//riddle//" + l.getId() + u.getId() + ".rddl");
					lesson_file.delete();
					l.getFollowed_by().add(u);
				LessonManager manager = new LessonManager(l,send,modelservice,userservice,ruleservice);
				String n = String.valueOf(l.getId()) + String.valueOf(u.getId());
				MainController.LESSONS.put(n,manager);
				manager.Solve();
				l.getFollowed_by().clear();
				}
				l.getFollowed_by().addAll(user);
				}
			}
			
			rulerepository.deleteById(r);
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
