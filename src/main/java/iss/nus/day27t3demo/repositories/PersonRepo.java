package iss.nus.day27t3demo.repositories;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import iss.nus.day27t3demo.models.Person;

@Repository
public class PersonRepo {

    @Autowired
    private MongoTemplate template;

    // Insertion -----------------------------------------
    public Person insertPerson(Person person){
        Person newPerson = template.insert(person);
        return newPerson;
    }

    public Person savePerson(Person person){
        Person newPerson = template.save(person);
        return newPerson;
    }
    
    public List<Person> getAllPersons(){
        return template.findAll(Person.class);
    }

    // Pagenation -----------------------------------------
    public List<Person> getPersonsPaged(int pageNumber, int pageSize){
        Query query = new Query();
        query.skip(pageNumber * pageSize);
        query.limit(pageSize);
        return template.find(query, Person.class);
    }

    // Deletion --------------------------------------------
    public void findAndDeletePerson(ObjectId id){
        Query query = new Query(Criteria.where("_id").is(id));
        DeleteResult result = template.remove(query, "persons");

        System.out.printf("Deleted document: %d\n", result.getDeletedCount());
    }

    public void deletePerson(Person p){
        template.remove(p);
    }

    // Update -----------------------------------------------
    public void findAndUpdate(ObjectId id, Person person){
        Query query = new Query(Criteria.where("_id").is(person.getPersonId()));
        Update updateOps = new Update()
                                .set("name", person.getName())
                                .inc("age", 1)
                                .set("hobbies", person.getHobbies())
                                .set("gender", person.getGender());

        UpdateResult result = template.updateFirst(query, updateOps, Person.class);

        System.out.printf("Documents updated: %d\n", result.getModifiedCount());
    }

    public Person updatePerson(Person p){
        Person updPerson = template.save(p);
        return updPerson;
    }

// Dunno if this works yet
    public void findAndUpdate2(Person person){
        Query query = new Query(Criteria.where("name").is(person.getName()));
        Update updateOps = new Update().set("name", person.getName())
                                    .set("age", person.getAge());

    UpdateResult result = template.updateMulti(query, updateOps, "persons");

    }
//TEXTSEARCH
    public List<Person> findPersonWithText(String text){
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matchingAny(text).caseSensitive(false);
        TextQuery textQuery =  TextQuery.queryText(textCriteria);

        List<Person> persons = template.find(textQuery, Person.class, "persons");

        System.out.println("Slide 19: " + persons.toString());
        return persons;
    }

    // public List<Document> findPersonWithTextScore(String text, String fieldname){
    //     TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matchingAny(text).caseSensitive(false);
    //     TextQuery textQuery =  TextQuery.queryText(textCriteria).includeScore(fieldname).sortByScore();

    //     List<Document>  = 

        
    // }
    public List<Document> getMoviesByRating(String key, String rating){
    MatchOperation matchOps = Aggregation.match(Criteria.where(key).is(rating));

    Aggregation pipeline = Aggregation.newAggregation(matchOps);

    AggregationResults<Document> results = template.aggregate(pipeline, "movies",Document.class);
    List<Document> docs = results.getMappedResults();

    System.out.println("Movies with PG rating " + docs.toString());

    return docs;
    }

    public List<Document> getMoviesMatchAndProject(String key, String value, String param1, String param2, String param3){
        MatchOperation matchOps = Aggregation.match(Criteria.where(key).is(value));
        ProjectionOperation projectOps = Aggregation.project(param1,param2,param3);

        Aggregation pipeline = Aggregation.newAggregation(matchOps, projectOps);
    
        AggregationResults<Document> results = template.aggregate(pipeline, "movies",Document.class);
        List<Document> docs = results.getMappedResults();
        
        System.out.println("Project movies with matching year" + docs.toString());
    
        return docs;
        }

}
