package it.cnr.istc.psts.wikitel.Service;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import it.cnr.istc.psts.wikitel.controller.LessonManager;
import it.cnr.istc.psts.wikitel.controller.MainController;
import it.cnr.istc.psts.wikitel.db.LessonEntity;


@Service
@Configurable
public class Starter implements 
  ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	public LessonService lessonservice;
	
	@Autowired 
	private LessonManager manager;

    public static int counter;

    @Override public void onApplicationEvent(ContextRefreshedEvent event) {
    	
    		List<LessonEntity> lesson = this.lessonservice.all();
    		for(LessonEntity l : lesson) {
    			System.out.println("added lesson:  " + l.getName());
    			 manager = new LessonManager(l);
    			MainController.LESSONS.put(l.getId(),manager);
    			manager.Solve();
    		}
    	}

}
