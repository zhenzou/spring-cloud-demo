package ltd.yazz.demo.service;

import ltd.yazz.demo.entity.Person;
import ltd.yazz.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EchoService {

    @Autowired
    private PersonRepository personRepository;

    @Cacheable(cacheNames = "echo")
    public String sayHi(String name) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Person person = personRepository.findOne(1);
        return String.format("Hi! %s,I am %s", name, person.getFirstName());
    }
}
