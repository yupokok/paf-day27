package iss.nus.day27t3demo;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import iss.nus.day27t3demo.models.Person;
import iss.nus.day27t3demo.repositories.PersonRepo;

@SpringBootApplication
public class Day27T3demoApplication implements CommandLineRunner {

	@Autowired
	PersonRepo personRepo;

	public static void main(String[] args) {
		SpringApplication.run(Day27T3demoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Person createdPerson1 = personRepo.insertPerson(new Person("Kimberly Wee", 24, "F", Arrays.asList("Hiking", "Reading")));

		// Person createdPerson3 = personRepo.insertPerson(new Person("Mahika Ravi", 24, "F", Arrays.asList("Design", "Cooking")));

		// Person createdPerson4 = personRepo.insertPerson(new Person("Keryna Chandra", 23, "F", Arrays.asList("Theatre", "Reading")));
		// Person createdPerson5 = personRepo.insertPerson(new Person("Pujaa Kasi", 23, "F", Arrays.asList("Dance", "Design")));

		// Person createdPerson2 = personRepo.insertPerson(new Person("Russell Chong", 23, "M", Arrays.asList("Shopping", "Reading")));
		

		List<Person> persons = personRepo.getAllPersons();
		persons.forEach(System.out::println);

		// ObjectId _oid = new ObjectId("65a8a591ca0c5a3a7b41fc37");
		// Person updatePerson = new Person("65a8a591ca0c5a3a7b41fc37", "Kim Wee", 24, "F", Arrays.asList("Hiking", "Mushroom Foraging"));

		// personRepo.findAndUpdate(_oid, updatePerson);


		// personRepo.findPersonWithText("Dance");

		// personRepo.getMoviesByRating("Rated", "PG");

		personRepo.getMoviesMatchAndProject("Year","2009","Title","Year","Rated");

	}

}

