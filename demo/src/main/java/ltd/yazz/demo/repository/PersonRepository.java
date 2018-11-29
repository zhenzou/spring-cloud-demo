package ltd.yazz.demo.repository;

import ltd.yazz.demo.entity.Person;
import org.springframework.stereotype.Repository;

/**
 * Just for test
 */
@Repository
public class PersonRepository {

    public Person findOne(long id) {
        return new Person("September", "Holmes");
    }
}
