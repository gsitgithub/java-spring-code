package dev.gsitgithub.service;

import java.util.List;

import dev.gsitgithub.entity.Person;
import dev.gsitgithub.generic.repo.specification.Filter;
import dev.gsitgithub.generic.service.GenericService;

public interface PersonService extends GenericService<Person, Long> {
	List<Person> search(Filter filter );

	List<Person> search(String filter);
}
