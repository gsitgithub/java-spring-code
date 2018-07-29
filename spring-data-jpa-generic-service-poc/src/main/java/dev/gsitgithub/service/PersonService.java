package dev.gsitgithub.service;

import java.util.List;

import dev.gsitgithub.entity.Person;
import dev.gsitgithub.generic.api.GenericService;
import dev.gsitgithub.generic.api.specification.Filter;

public interface PersonService extends GenericService<Person, Long> {
	List<Person> search(Filter filter );

	List<Person> search(String filter);
}
