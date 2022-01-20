package it.cnr.istc.psts.wikitel.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.mapper.Mapper;
import org.apache.el.lang.FunctionMapperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cnr.istc.pst.oratio.Bound;
import it.cnr.istc.pst.oratio.GraphListener;
import it.cnr.istc.pst.oratio.Rational;
import it.cnr.istc.pst.oratio.Solver;
import it.cnr.istc.pst.oratio.SolverException;
import it.cnr.istc.pst.oratio.StateListener;
import it.cnr.istc.pst.oratio.timelines.ExecutorListener;
import it.cnr.istc.pst.oratio.timelines.TimelinesExecutor;

import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.RuleEntity;
import it.cnr.istc.psts.wikitel.db.UserEntity;
import it.cnr.istc.psts.WikitelNewApplication;
import it.cnr.istc.psts.wikitel.db.LessonEntity;



public class LessonManager implements StateListener, GraphListener, ExecutorListener  {

	   static final Logger LOG = LoggerFactory.getLogger(LessonManager.class);
	   private LessonEntity lesson;
	   private static  ObjectMapper pippo = new ObjectMapper();
	   
	 
	    private final Solver solver = new Solver();
	    private final TimelinesExecutor executor = new TimelinesExecutor(solver);
	    
	    public LessonManager(final LessonEntity lesson) {
	        this.lesson = lesson;
	        
	 
	 
	        solver.addStateListener(this);
	        solver.addGraphListener(this);
	        executor.addExecutorListener(this);
	    }
	    
	    public void Solve() {
	    	final StringBuilder sb = new StringBuilder();
	    	System.out.println(lesson.getName());
	    	System.out.println("prova: "+lesson.getFollowed_by().size());
	        to_string(sb, lesson);
	        
	        final File lesson_file = new File(lesson.getName() + ".rddl");
	        try {
	            if (lesson_file.createNewFile()) {
	                System.out.println("File created: " + lesson_file.getName());
	            } else {
	                System.out.println("File already exists.");
	            }
	            final FileWriter writer = new FileWriter(lesson_file, false);
	            writer.append(sb);
	            writer.close();
	        } catch (final IOException e) {
	            LOG.error("Cannot create lesson problem file", e);
	        }

	        LOG.info("Reading lesson \"{}\" planning problem..", lesson.getName());
	        try {// we load the planning problem..
	            solver.read(sb.toString());
	        } catch (SolverException e) {
	            LOG.error("cannot read the given problem..", e);
	        }
	        LOG.info("Solving lesson \"{}\" planning problem..", lesson.getName());
	        try { // we solve the planning problem..
	            solver.solve();
	        } catch (SolverException e) {
	            LOG.error("cannot solve the given problem..", e);
	        }
	       // setState(LessonState.Stopped);
	    }

		@Override
		public void endAtoms(long[] arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endingAtoms(long[] arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startAtoms(long[] arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startingAtoms(long[] arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void tick(Rational arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void causalLinkAdded(long arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void currentFlaw(long arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void currentResolver(long arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void flawCostChanged(long arg0, Rational arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void flawCreated(long arg0, long[] arg1, String arg2, State arg3, Bound arg4) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void flawPositionChanged(long arg0, Bound arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void flawStateChanged(long arg0, State arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void resolverCreated(long arg0, long arg1, Rational arg2, String arg3, State arg4) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void resolverStateChanged(long arg0, State arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inconsistentProblem() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void log(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void read(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void read(String[] arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void solutionFound() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startedSolving() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void stateChanged() {
			// TODO Auto-generated method stub
			
		}
		
		 private static void to_string(final StringBuilder sb, final LessonEntity lesson_entity) {
		        to_string(sb, lesson_entity.getModel());
		        System.out.println(lesson_entity.getFollowed_by().size());
		        System.out.println("goals "+lesson_entity.getGoals().size());
		        
		        sb.append("\n\n");
		        sb.append("Lesson l_").append(lesson_entity.getId()).append(" = new Lesson();\n");
		        for (final UserEntity student : lesson_entity.getFollowed_by()) {
		            sb.append("User u_").append(student.getId()).append(" = new User(").append(student.getId());
		            try {
		            	ObjectMapper mapper = new ObjectMapper();
		        		List<String> profile = mapper.readValue(student.getProfile(), new TypeReference<List<String>>(){});
		        		Json_reader interests = pageController.json("/json/user_model.json",true);
		            	System.out.println("ciao " +profile.get(0));
		                for (Interests interest : interests.getInterests()) {
		                	Boolean i=false;
		                	System.out.println(interest.getId());
		                	System.out.println("profile" + profile);
		                	if(profile.contains(interest.getId())) {
		                		i=true;
		                	}
		                    sb.append(", ").append(i);
		                    
		                }
		                sb.append(");\n");
		            } catch (final JsonProcessingException e) {
		                LOG.error("Cannot parse profile", e);
		            }
		        }

		        sb.append("\nUser u;\n");
		        for (final RuleEntity goal : lesson_entity.getGoals()) {
		            sb.append("\n{\n");
		            sb.append("  goal st").append(goal.getId()).append(" = new l_").append(lesson_entity.getId()).append(".St_")
		                    .append(goal.getId()).append("(u:u);\n");
		            sb.append("} or {\n");
		            for (String topic : goal.getTopics())
		                sb.append("  !u.").append(to_id(topic)).append(";\n");
		            sb.append("}\n");
		        }
		    }
		 
		 private static void to_string(final StringBuilder sb, final ModelEntity model_entity) {
		        sb.append("class User {\n\n");
		        sb.append("  int id;\n");
		        for (final JsonNode interest : WikitelNewApplication.USER_MODEL.get("interests"))
		            sb.append("  bool ").append(to_id(interest.get("id").asText())).append(";\n");
		        sb.append("  ReusableResource busy_time = new ReusableResource(1.0);\n\n");
		        sb.append("  User(int id");
		        for (final JsonNode interest : WikitelNewApplication.USER_MODEL.get("interests"))
		            sb.append(", bool ").append(to_id(interest.get("id").asText()));
		        sb.append(") : id(id)");
		        for (final JsonNode interest : WikitelNewApplication.USER_MODEL.get("interests"))
		            sb.append(", ").append(to_id(interest.get("id").asText())).append('(')
		                    .append(to_id(interest.get("id").asText())).append(')');
		        sb.append(" {}\n");
		        sb.append("}\n\n");

		        sb.append("class Lesson {\n");
		        model_entity.getRules().forEach(rule -> to_string(sb, rule));
		        sb.append("}");
		    }
		 
		 private static void to_string(final StringBuilder sb, final RuleEntity rule_entity) {
		        sb.append("\n  predicate ").append("St_").append(rule_entity.getId()).append("(User u) : Interval {\n");
		        sb.append("    duration >= ").append(rule_entity.getLength()).append(".0;\n");
		        sb.append("    fact bt = new u.busy_time.Use(start:start, duration:duration, end:end, amount:1.0);\n");

		        for (final RuleEntity pre : rule_entity.getPreconditions()) {
		            sb.append("\n    {\n");
		            sb.append("      goal st").append(pre.getId()).append(" = new St_").append(pre.getId()).append("(u:u);\n");
		            if (rule_entity.isTopDown())
		                sb.append("      st").append(pre.getId()).append(".start >= end;\n");
		            else
		                sb.append("      st").append(pre.getId()).append(".end <= start;\n");
		            sb.append("    } or {\n");
		            for (String topic : pre.getTopics())
		                sb.append("      !u.").append(to_id(topic)).append(";\n");
		            sb.append("    }\n");
		        }
		        sb.append("  }\n");
		    }
		 
		 private static String to_id(final String c_id) {
		        return Normalizer.normalize(c_id.replace("Categoria:", "c_"), Normalizer.Form.NFD).replaceAll("%27|\\p{M}", "")
		                .toLowerCase();
		    }
}
