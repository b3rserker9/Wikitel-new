package it.cnr.istc.psts.wikitel.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.cnr.istc.psts.wikitel.Repository.LessonsRepository;
import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.UserEntity;


@Service
public class LessonService {

	@Autowired
	private LessonsRepository lessonsrepository;
	
	
	@Transactional
    public LessonEntity save(LessonEntity lesson) {
        return this.lessonsrepository.save(lesson);
    }
	
	@Transactional
    public void delete(LessonEntity lesson) {
		for(UserEntity u : lesson.getFollowed_by()) {
			u.getFollowing_lessons().remove(lesson);
		}
		lesson.getFollowed_by().clear();
		lesson.getTeacher().getTeaching_lessons().remove(lesson);
		lesson.setTeacher(null);
		lesson.getGoals().clear();
		lesson.setModel(null);
        this.lessonsrepository.deleteById(lesson.getId());
    }
	
	@Transactional
	public List<LessonEntity> getlesson(UserEntity teacher) {
		List<LessonEntity> result = this.lessonsrepository.findByTeacher(teacher);
		return result;
	}
	
	@Transactional
	public List<LessonEntity> all() {
		return (List<LessonEntity>) lessonsrepository.findAll();
	}
	
	@Transactional
	public List<LessonEntity> getlessonbymodel(ModelEntity m) {
		List<LessonEntity> result = this.lessonsrepository.findByModel(m);
		return result;
	}
	
	@Transactional
	public LessonEntity lezionePerId(Long id) {
		Optional<LessonEntity> result = lessonsrepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public LessonEntity lezionePerNome(String name) {
		Optional<LessonEntity> result = lessonsrepository.findByName(name);
		return result.orElse(null);
	}
	@Transactional
	public List<LessonEntity> lezionePerDocente(Long id) {
		
		return this.lessonsrepository.findByTeachersonly(id);
	}
	
}
