package iss.nus.day27t3demo.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    public void findAndUpdate(Person person){
        Query query = new Query(Criteria.where("_id").is(person.getPersonId()));
        Update updateOps = new Update()
                                .set("name", person.getName())
                                .inc("age", 1);

        UpdateResult result = template.updateFirst(query, updateOps, Person.class);

        System.out.printf("Documents updated: %d\n", result.getModifiedCount());
    }

    public Person updatePerson(Person p){
        Person updPerson = template.save(p);
        return updPerson;
    }

}
