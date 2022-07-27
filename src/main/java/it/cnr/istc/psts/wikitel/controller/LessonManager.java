package it.cnr.istc.psts.wikitel.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cnr.istc.pst.oratio.Atom;
import it.cnr.istc.pst.oratio.Bound;
import it.cnr.istc.pst.oratio.GraphListener;
import it.cnr.istc.pst.oratio.Predicate;
import it.cnr.istc.pst.oratio.Rational;
import it.cnr.istc.pst.oratio.Solver;
import it.cnr.istc.pst.oratio.Item.ArithItem;
import it.cnr.istc.pst.oratio.SolverException;
import it.cnr.istc.pst.oratio.StateListener;
import it.cnr.istc.pst.oratio.Type;
import it.cnr.istc.pst.oratio.timelines.ExecutorException;
import it.cnr.istc.pst.oratio.timelines.ExecutorListener;
import it.cnr.istc.pst.oratio.timelines.Timeline;
import it.cnr.istc.pst.oratio.timelines.TimelinesExecutor;
import it.cnr.istc.pst.oratio.utils.Flaw;
import it.cnr.istc.pst.oratio.utils.Resolver;
import it.cnr.istc.psts.wikitel.db.LessonEntity;
import it.cnr.istc.psts.wikitel.db.ModelEntity;
import it.cnr.istc.psts.wikitel.db.RuleEntity;
import it.cnr.istc.psts.wikitel.db.TextRuleEntity;
import it.cnr.istc.psts.wikitel.db.UserEntity;
import it.cnr.istc.psts.wikitel.db.WebRuleEntity;
import it.cnr.istc.psts.wikitel.db.WikiRuleEntity;
import it.cnr.psts.wikitel.API.Lesson.LessonState;
import it.cnr.psts.wikitel.API.Message;
import it.cnr.psts.wikitel.API.Message.Stimulus;
import it.cnr.istc.psts.Websocket.Sending;
import it.cnr.istc.psts.wikitel.Mongodb.RuleMongo;
import it.cnr.istc.psts.wikitel.Service.*;


	    
	    public class LessonManager implements StateListener, GraphListener, ExecutorListener  {
	    	
	    	@Autowired
	    	ModelService modelservice;
	    	
	    	@Autowired
	    	UserService userservice;
	    	
	    	@Autowired
	    	private Sending send;
	    	
	    	
	    	   static final Logger LOG = LoggerFactory.getLogger(LessonManager.class);
	    	   private LessonEntity lesson;
	    	   private ScheduledFuture<?> scheduled_feature;
	    	   private final Solver solver = new Solver();
	    	    private final Map<Long, Atom> c_atoms = new HashMap<>();
	    	    private final TimelinesExecutor executor = new TimelinesExecutor(solver);
	    	    private final Set<String> topics = new HashSet<>();
	    	    private LessonState state = LessonState.Stopped;
	    	    private final Map<Long, Collection<Stimulus>> stimuli = new HashMap<>();
	    	    private Rational current_time = new Rational();
	    	    private final Map<Long, Flaw> flaws = new HashMap<>();
	    	    private Flaw current_flaw = null;
	    	    private final Map<Long, Resolver> resolvers = new HashMap<>();
	    	    private Resolver current_resolver = null;
	    	    Stimulus st = null;
	    	    
	    	    
	    	    
	    	    public LessonManager(final LessonEntity lesson, final Sending send, final ModelService modelservice, final UserService userservice ) {
	    	        this.lesson = lesson;
	    	        this.send=send;
	    	        this.modelservice = modelservice;
	    	        this.userservice = userservice;
	    	        for (final UserEntity student : lesson.getFollowed_by())
	    	            stimuli.put(student.getId(), new ArrayList<>());
	    	 
	    	 
	    	        solver.addStateListener(this);
	    	        solver.addGraphListener(this);
	    	        executor.addExecutorListener(this);
	    	    }
	    	    
	    	    public void Solve() {
	    	    	final StringBuilder sb = new StringBuilder();
	    	    	System.out.println(lesson.getName());
	    	    	System.out.println("prova: "+ lesson.getFollowed_by().size());
	    	        to_string(sb, lesson);
	    	        
	    	        final File lesson_file = new File(System.getProperty("user.dir")+"\\riddle\\" + lesson.getId() + ".rddl");
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
	    	        setState(LessonState.Stopped);
	    	    }
	    	    
	    	    public Solver getSolver() {
	    	        return solver;
	    	    }
	    	    
	    	    public void play() {
	    	    	scheduled_feature = Starter.EXECUTOR.scheduleAtFixedRate(() -> {
	    	            try {
	    	                executor.tick();
	    	            } catch (ExecutorException e) {
	    	                LOG.error("cannot execute the given solution..", e);
	    	                scheduled_feature.cancel(false);
	    	            }
	    	        }, 1, 1, TimeUnit.SECONDS);
	    	    	setState(LessonState.Running);
	    	    }
	    	    
	    	    public void pause() {
	    	        scheduled_feature.cancel(false);
	    	        setState(LessonState.Paused);
	    	    }

	    	    public void stop() {
	    	        scheduled_feature.cancel(false);
	    	        setState(LessonState.Stopped);
	    	    }
	    	    
	    	    public LessonState getState() {
	    	        return state;
	    	    }


	    	    private void setState(final LessonState state) {
	    	        this.state = state;
	    	        try {
	    	            final String wsc = UserController.ONLINE.get(lesson.getTeacher().getId());
	    	       
	    	            if (wsc != null) {
	    	            	send.notify(Starter.mapper.writeValueAsString(new Message.LessonStateUpdate(lesson.getId(), state)), wsc);
	    	            }
	    	            for (final UserEntity student : lesson.getFollowed_by()) {
	    	                final String student_wsc = UserController.ONLINE.get(student.getId());
	    	                if (student_wsc != null) {
	    	                	send.notify(Starter.mapper.writeValueAsString(new Message.LessonStateUpdate(lesson.getId(), state)), student_wsc);
	    	                }
	    	            }
	    	        } catch (final JsonProcessingException e) {
	    	            LOG.error("cannot notify lesson state update..", e);
	    	        }
	    	    }
	    	    
	    	    /**
	    	     * @return the stimuli
	    	     */
	    	    public Collection<Stimulus> getStimuli(final long user_id) {
	    	        if (!stimuli.containsKey(user_id) || stimuli.get(user_id).isEmpty())
	    	            return null;
	    	        return stimuli.get(user_id);
	    	    }
	    	    
	    	    public LessonEntity getlesson() {
	    	    	return lesson;
	    	    }
	    	    
	    	    @Override
	    	    public void endAtoms(long[] atoms) {
	    	        for (int i = 0; i < atoms.length; i++) {
	    	            final Atom atom = c_atoms.get(atoms[i]);
	    	            if (atom.getType().getName().equals("Use"))
	    	                continue;
	    	            final long rule_id = Long.parseLong(atom.getType().getName().substring(3));
	    	            final RuleEntity rule_entity = modelservice.getRule(rule_id);

	    	            try {
	    	                final long student_id = atom.get("u").getName().equals("u")
	    	                        ? lesson.getFollowed_by().iterator().next().getId()
	    	                        : Long.parseLong(atom.get("u").getName().substring(2));
	    	                final UserEntity student_entity = this.userservice.getUserId(student_id);
	    	                student_entity.getLearnt_topics().add(rule_entity);
	    	            } catch (NumberFormatException | NoSuchFieldException e) {
	    	                LOG.error("Cannot find atom's user..", e);
	    	            }
	    	        }
	    	    }

	    		@Override
	    		public void endingAtoms(long[] arg0) {
	    			// TODO Auto-generated method stub
	    			
	    		}

	    		@Override
	    		public void startAtoms(long[] atoms) {
	    			final long current_time = System.currentTimeMillis();
	    			for (int i = 0; i < atoms.length; i++) {
	    	            final Atom atom = c_atoms.get(atoms[i]);
	    	            if (atom.getType().getName().equals("Use"))
	    	                continue;
	    	            final long rule_id = Long.parseLong(atom.getType().getName().substring(3));
	    	            final RuleEntity rule_entity = this.modelservice.getRule(rule_id);

	    	            
	    	            if (rule_entity instanceof TextRuleEntity)
	    	                st = new Message.Stimulus.TextStimulus(lesson.getId(), rule_id, current_time, false,
	    	                        rule_entity.getName());
	    	            else if (rule_entity instanceof WebRuleEntity)
	    	                st = new Message.Stimulus.URLStimulus(lesson.getId(), rule_id, current_time, false,
	    	                        rule_entity.getName(), ((WebRuleEntity) rule_entity).getUrl());
	    	            else if (rule_entity instanceof WikiRuleEntity)
	    	                st = new Message.Stimulus.URLStimulus(lesson.getId(), rule_id, current_time, false,
	    	                        rule_entity.getName(), ((WikiRuleEntity) rule_entity).getUrl());

	    	            try {
	    	                final long student_id = atom.get("u").getName().equals("u")
	    	                        ? lesson.getFollowed_by().iterator().next().getId()
	    	                        : Long.parseLong(atom.get("u").getName().substring(2));
	    	                final UserEntity student_entity = userservice.getUserId(student_id);
	    	                stimuli.get(student_entity.getId()).add(st);

	    	               final String wsc = UserController.ONLINE.get(student_id);
	    	                if (wsc != null) {
	    	                	send.notify(Starter.mapper.writeValueAsString(st), wsc);
	    	                }
	    	                
	    	            } catch (NumberFormatException | NoSuchFieldException e) {
	    	                LOG.error("Cannot find atom's user..", e);
	    	            } catch (JsonProcessingException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    	        }
	    			
	    		}

	    		@Override
	    		public void startingAtoms(long[] arg0) {
	    			// TODO Auto-generated method stub
	    			
	    		}

	    		   @Override
	    		    public void tick(final Rational time) {
	    			   
	    		        current_time = time;
	    		 
	    				
	    		        final String wsc = UserController.ONLINE.get(lesson.getTeacher().getId());
	    		        if (wsc != null)
	    		            try {
	    		            	send.notify(Starter.mapper.writeValueAsString(new Tick(lesson.getId(), current_time)), wsc);
	    		            } catch (final JsonProcessingException e) {
	    		                LOG.error("Cannot write tick message..", e);
	    		            }
	    		        try {
	    		            if (((ArithItem) solver.get("horizon")).getValue().leq(current_time)) {
	    		                LOG.info("Nothing more to execute..");
	    		                scheduled_feature.cancel(false);
	    		            }
	    		        } catch (final NoSuchFieldException e) {
	    		            LOG.error("Cannot find horizon..", e);
	    		        }
	    		    }

	    		   @Override
	    		    public synchronized void causalLinkAdded(final long flaw, final long resolver) {
	    		        resolvers.get(resolver).preconditions.add(flaws.get(flaw));
	    		        try {
	    		            final String ws = UserController.ONLINE.get(lesson.getTeacher().getId());
	    		            if (ws != null) {
	    		            	send.notify(Starter.mapper.writeValueAsString(new CausalLinkAdded(lesson.getId(), flaw, resolver)), ws);
	    		            }
	    				       
	    		        } catch (final JsonProcessingException e) {
	    		            LOG.error("Cannot serialize", e);
	    		        }
	    		    }

	    		   @Override
	    		    public synchronized void currentFlaw(final long id) {
	    		        if (current_flaw != null)
	    		            current_flaw.current = false;
	    		        final Flaw flaw = flaws.get(id);
	    		        current_flaw = flaw;
	    		        current_flaw.current = true;
	    		        try {
	    		            final String ws = UserController.ONLINE.get(lesson.getTeacher().getId());
	    		            if (ws != null) {
	    		            send.notify(Starter.mapper.writeValueAsString(new CurrentFlaw(lesson.getId(), id)), ws);
	    		            }
	    		        } catch (final JsonProcessingException e) {
	    		            LOG.error("Cannot serialize", e);
	    		        }
	    		    }

	    		   @Override
	    		    public synchronized void currentResolver(final long id) {
	    		        if (current_resolver != null)
	    		            current_resolver.current = false;
	    		        final Resolver resolver = resolvers.get(id);
	    		        current_resolver = resolver;
	    		        current_resolver.current = true;
	    		        try {
	    		            final String ws = UserController.ONLINE.get(lesson.getTeacher().getId());
	    		            if (ws != null) {
	    		            	send.notify(Starter.mapper.writeValueAsString(new CurrentResolver(lesson.getId(), id)), ws);
	    		            }
	    				       
	    		        } catch (final JsonProcessingException e) {
	    		            LOG.error("Cannot serialize", e);
	    		        }
	    		    }

	    		  @Override
	    		    public synchronized void flawCostChanged(final long id, final Rational cost) {
	    		        final Flaw flaw = flaws.get(id);
	    		        flaw.cost = cost;
	    		        try {
	    		            final String ws = UserController.ONLINE.get(lesson.getTeacher().getId());
	    		            if (ws != null) {
	    		            	send.notify(Starter.mapper.writeValueAsString(new FlawCostChanged(lesson.getId(), id, cost)), ws);
	    		            }
	    		        } catch (final JsonProcessingException e) {
	    		            LOG.error("Cannot serialize", e);
	    		        }
	    		  }
	    		    

	    		    @Override
	    		    public synchronized void flawPositionChanged(final long id, final Bound position) {
	    		        final Flaw flaw = flaws.get(id);
	    		        flaw.position = position;
	    		        try {
	    		            final String ws = UserController.ONLINE.get(lesson.getTeacher().getId());
	    		
	    		            if (ws != null) {
	    		            	send.notify(Starter.mapper.writeValueAsString(new FlawPositionChanged(lesson.getId(), id, position)), ws);
	    		            }
	    		        } catch (final JsonProcessingException e) {
	    		            LOG.error("Cannot serialize", e);
	    		        }
	    		    }

	    	    @Override
	    	    public synchronized void flawCreated(final long id, final long[] causes, final String label, final State state,
	    	            final Bound position) {
	    	        final Flaw c_flaw = new Flaw(id,
	    	                Arrays.stream(causes).mapToObj(r_id -> resolvers.get(r_id)).toArray(Resolver[]::new), label, state,
	    	                position);
	    	        Stream.of(c_flaw.causes).forEach(c -> c.preconditions.add(c_flaw));
	    	        flaws.put(id, c_flaw);
	    	        try {
	    	            final String ws = UserController.ONLINE.get(lesson.getTeacher().getId());
	    	            if (ws != null)	      {
	    	            	send.notify(Starter.mapper.writeValueAsString(new FlawCreated(lesson.getId(), id, causes, label, (byte) state.ordinal(), position)), ws);
	    	            }
	    	            } catch (final JsonProcessingException e) {
	    	            LOG.error("Cannot serialize", e);
	    	        }
	    	    }


	    		   @Override
	    		    public synchronized void flawStateChanged(final long id, final State state) {
	    		        final Flaw flaw = flaws.get(id);
	    		        flaw.state = state;
	    		        try {
	    		            final String ws = UserController.ONLINE.get(lesson.getTeacher().getId());
	    		            if (ws != null) {
	    		            	send.notify(Starter.mapper.writeValueAsString(new FlawStateChanged(lesson.getId(), id, (byte) state.ordinal())), ws);
	    		            }
	    		        } catch (final JsonProcessingException e) {
	    		            LOG.error("Cannot serialize", e);
	    		        }
	    		    }

	    		   @Override
	    		    public synchronized void resolverCreated(final long id, final long effect, final Rational cost, final String label,
	    		            final State state) {
	    		        final Resolver resolver = new Resolver(id, flaws.get(effect), label, state, cost);
	    		        resolvers.put(id, resolver);
	    		        try {
	    		            final String ws = UserController.ONLINE.get(lesson.getTeacher().getId());
	    		            if (ws != null) {
	    		            	send.notify(Starter.mapper.writeValueAsString(new ResolverCreated(lesson.getId(), id, effect, cost, label, (byte) state.ordinal())), ws);	
	    		            }
	    		        } catch (final JsonProcessingException e) {
	    		            LOG.error("Cannot serialize", e);
	    		        }
	    		    }

	    		    @Override
	    		    public synchronized void resolverStateChanged(final long id, final State state) {
	    		        final Resolver resolver = resolvers.get(id);
	    		        resolver.state = state;
	    		        try {
	    		            final String ws = UserController.ONLINE.get(lesson.getTeacher().getId());
	    		   
	    		            if (ws != null) {
	    		            	send.notify(Starter.mapper.writeValueAsString(new ResolverStateChanged(lesson.getId(), id, (byte) state.ordinal())), ws);
	    		            }
	    				       
	    		        } catch (final JsonProcessingException e) {
	    		            LOG.error("Cannot serialize", e);
	    		        }
	    		    }

	    		 @Override
	    		    public void inconsistentProblem() {
	    		        LOG.info("Lesson \"{}\" inconsistent planning problem..", lesson.getName());
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
	    		    public void startedSolving() {
	    		        LOG.info("Started solving lesson \"{}\" planning problem..", lesson.getName());
	    		    }

	    		    @Override
	    		    public void stateChanged() {
	    		    }

	    		    @Override
	    		    public void solutionFound() {
	    		        LOG.info("Lesson \"{}\" planning problem solution found..", lesson.getName());
	    		    
	    		        c_atoms.clear();

	    		        for (final Type t : solver.getTypes().values())
	    		            for (final Predicate p : t.getPredicates().values())
	    		                p.getInstances().stream().map(atm -> (Atom) atm)
	    		                        .filter(atm -> (atm.getState() == Atom.AtomState.Active))
	    		                        .forEach(atm -> c_atoms.put(atm.getSigma(), atm));
	    		        
	    	
	    		    
	    		        
	    		        
	    		    }

	    		
	    		 private static void to_string(final StringBuilder sb, final LessonEntity lesson_entity) {
	    		        to_string(sb, lesson_entity.getModel());
	    		       
	    		        
	    		        sb.append("\n\n");
	    		        sb.append("Lesson l_").append(lesson_entity.getId()).append(" = new Lesson();\n");
	    		        for (final UserEntity student : lesson_entity.getFollowed_by()) {
	    		            sb.append("User u_").append(student.getId()).append(" = new User(").append(student.getId());
	    		            try {
	    		            	ObjectMapper mapper = new ObjectMapper();
	    		        		List<String> profile = mapper.readValue(student.getProfile(), new TypeReference<List<String>>(){});
	    		        		Json_reader interests = pageController.json("/json/user_model.json",true);
	    		                for (Interests interest : interests.getInterests()) {
	    		                	Boolean i=false;
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
	    		        for (final JsonNode interest : Starter.USER_MODEL.get("interests"))
	    		            sb.append("  bool ").append(to_id(interest.get("id").asText())).append(";\n");
	    		        sb.append("  ReusableResource busy_time = new ReusableResource(1.0);\n\n");
	    		        sb.append("  User(int id");
	    		        for (final JsonNode interest : Starter.USER_MODEL.get("interests"))
	    		            sb.append(", bool ").append(to_id(interest.get("id").asText()));
	    		        sb.append(") : id(id)");
	    		        for (final JsonNode interest : Starter.USER_MODEL.get("interests"))
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
	    		 
	    		 public static MessageHeaders createHeaders(String sessionId) {
	    				SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
	    				headerAccessor.setSessionId(sessionId);
	    				headerAccessor.setLeaveMutable(true);
	    				return headerAccessor.getMessageHeaders();
	    			}
	    		 
	    		  public Resolver getCurrentResolver() {
	    		        return current_resolver;
	    		    }

	    		    public Rational getCurrentTime() {
	    		        return current_time;
	    		    }

	    		 
	    		  /**
	    		     * @return the topics
	    		     */
	    		    public Set<String> getTopics() {
	    		        return Collections.unmodifiableSet(topics);
	    		    }

	    		 
	    		  static class Log extends it.cnr.istc.pst.oratio.utils.Message.Log {

	    		        public final long lesson;

	    		        Log(final long lesson, final String log) {
	    		            super(log);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class StartedSolving extends it.cnr.istc.pst.oratio.utils.Message.StartedSolving {

	    		        public final long lesson;

	    		        StartedSolving(final long lesson) {
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class SolutionFound extends it.cnr.istc.pst.oratio.utils.Message.SolutionFound {

	    		        public final long lesson;

	    		        SolutionFound(final long lesson) {
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class InconsistentProblem extends it.cnr.istc.pst.oratio.utils.Message.InconsistentProblem {

	    		        public final long lesson;

	    		        InconsistentProblem(final long lesson) {
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class Graph extends it.cnr.istc.pst.oratio.utils.Message.Graph {

	    		        public final long lesson;

	    		        Graph(final long lesson, final Collection<Flaw> flaws, final Collection<Resolver> resolvers) {
	    		            super(flaws, resolvers);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class FlawCreated extends it.cnr.istc.pst.oratio.utils.Message.FlawCreated {

	    		        public final long lesson;

	    		        FlawCreated(final long lesson, final long id, final long[] causes, final String label, final byte state,
	    		                final Bound position) {
	    		            super(id, causes, label, state, position);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class FlawStateChanged extends it.cnr.istc.pst.oratio.utils.Message.FlawStateChanged {

	    		        public final long lesson;

	    		        FlawStateChanged(final long lesson, final long id, final byte state) {
	    		            super(id, state);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class FlawCostChanged extends it.cnr.istc.pst.oratio.utils.Message.FlawCostChanged {

	    		        public final long lesson;

	    		        FlawCostChanged(final long lesson, final long id, final Rational cost) {
	    		            super(id, cost);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class FlawPositionChanged extends it.cnr.istc.pst.oratio.utils.Message.FlawPositionChanged {

	    		        public final long lesson;

	    		        FlawPositionChanged(final long lesson, final long id, final Bound position) {
	    		            super(id, position);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class CurrentFlaw extends it.cnr.istc.pst.oratio.utils.Message.CurrentFlaw {

	    		        public final long lesson;

	    		        CurrentFlaw(final long lesson, final long id) {
	    		            super(id);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class ResolverCreated extends it.cnr.istc.pst.oratio.utils.Message.ResolverCreated {

	    		        public final long lesson;

	    		        ResolverCreated(final long lesson, final long id, final long effect, final Rational cost, final String label,
	    		                final byte state) {
	    		            super(id, effect, cost, label, state);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class ResolverStateChanged extends it.cnr.istc.pst.oratio.utils.Message.ResolverStateChanged {

	    		        public final long lesson;

	    		        ResolverStateChanged(final long lesson, final long id, final byte state) {
	    		            super(id, state);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class CurrentResolver extends it.cnr.istc.pst.oratio.utils.Message.CurrentResolver {

	    		        public final long lesson;

	    		        CurrentResolver(final long lesson, final long id) {
	    		            super(id);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class CausalLinkAdded extends it.cnr.istc.pst.oratio.utils.Message.CausalLinkAdded {

	    		        public final long lesson;

	    		        CausalLinkAdded(final long lesson, final long flaw, final long resolver) {
	    		            super(flaw, resolver);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class Timelines extends it.cnr.istc.pst.oratio.utils.Message.Timelines {

	    		        public final long lesson;

	    		        Timelines(final long lesson, final Collection<Timeline<?>> timelines) {
	    		            super(timelines);
	    		            this.lesson = lesson;
	    		        }
	    		    }

	    		    static class Tick extends it.cnr.istc.pst.oratio.utils.Message.Tick {

	    		        public final long lesson;

	    		        Tick(final long lesson, final Rational current_time) {
	    		            super(current_time);
	    		            this.lesson = lesson;
	    		        }
	    		    }
	    }