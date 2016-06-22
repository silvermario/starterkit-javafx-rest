package pl.spring.demo.service;

import java.util.Collection;

import pl.spring.demo.model.Greeting;

public interface GreetingService {

    Collection<Greeting> findAll();

    Greeting findOne(Long id);

    Greeting create(Greeting greeting);

    Greeting update(Greeting greeting);

    void delete(Long id);

}
