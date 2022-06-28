/**
 * 
 */
package it.cnr.istc.psts.wikitel.Mongodb;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;

import it.cnr.istc.psts.wikitel.db.RuleEntity;
import it.cnr.istc.psts.wikitel.db.RuleSuggestionRelationEntity;
import it.cnr.istc.psts.wikitel.db.SuggestionEntity;

/**
 * @author aliyo
 *
 */
@Controller
public class MongoDb {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public void saveWikipediaPage(RuleEntity r) {
        try {
        	
            JSONObject toMongoJSON = toMongoJSON(r);
          

            //BasicDBObject newDocument = BasicDBObject.parse(toMongoJSON.toString());//new BasicDBObject(o.toString());
            
            //cursor.updateOne(query, newDocument);
            Document doc = Document.parse(toMongoJSON.toString());
            System.out.println("Sto scrivendo json");
            mongoTemplate.insert(doc, "Rule");
        } catch (Exception e) {
            System.out.println("save mongo page error : "+e.toString());
        }

    }
	
	 public JSONObject toMongoJSON(RuleEntity r) {
	        JSONObject js = null;
	        js=new JSONObject();
	        js.put("_id", r.getName());
	        js.put("title",r.getName());
	        JSONArray array = new JSONArray();
	        for(RuleSuggestionRelationEntity s : r.getSuggestions())
	        	array.put(s);
	        
	        js.put("suggestion", array);
	        
	      
	        
	        return js;
	    }

}
