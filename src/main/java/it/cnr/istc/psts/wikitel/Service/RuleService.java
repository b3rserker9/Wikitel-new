package it.cnr.istc.psts.wikitel.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.cnr.istc.psts.Websocket.Sending;
import it.cnr.istc.psts.wikitel.Mongodb.SuggestionM;
import it.cnr.istc.psts.wikitel.Mongodb.SuggestionMongo;
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

		
		public List<Long> rulesprecondition(RuleEntity r, List<Long> l){
			if(r.getPreconditions().isEmpty())return l;
			else {
				for(RuleEntity rule : r.getPreconditions()) {
					l.add(rule.getId());
					rulesprecondition(rule,l);
				}
			}
			return l;
		}
		
		
		@Transactional
	    public void delete(Long m, Long r) {
			ModelEntity model = this.modelservice.getModel(m);
			RuleEntity rule_first = this.getRule(r);
			
			List<Long> list_start = new ArrayList<>();
			List<Long> list = rulesprecondition(rule_first ,list_start);
			list.add( rule_first.getId());
			Collections.sort(list,Collections.reverseOrder());
			for(Long num : list) {
				System.out.println(num);
				RuleEntity rule = this.getRule(num);
				System.out.println(list);
				System.out.println(rule.getName());
				
				List<Long> effect = new ArrayList<>();
				
				for(RuleEntity e: rule.getEffects()) {
					System.out.println(e);
					RuleEntity ef = this.getRule(e.getId());
					System.out.println(ef);
					effect.add(e.getId());
				}
				System.out.println(effect);
				for(Long e: effect) {
					System.out.println(e);
					RuleEntity ef = this.getRule(e);
					System.out.println(ef);
					ef.removePrecondition(rule);
					
					SuggestionM sm =  this.modelservice.getsuggestion(ef.getSuggestions());
					   SuggestionMongo sugmongo = new SuggestionMongo();

			            sugmongo.setPage(rule.getName());

			            sugmongo.setScore((double) 0.5);
			            sugmongo.setScore2((double) 0.5);
			            sm.getSuggestion().add(sugmongo);
			            this.modelservice.savesm(sm);
					this.saverule(ef);
				}
				rule.getPreconditions().clear();
				rule.getEffects().clear();
				model.getRules().remove(rule);
			
			
			
			
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
			this.rulerepository.deleteById(rule.getId());
			};
			
			this.modelservice.save(model);
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
