package dev.gsitgithub.service;

import java.util.List;

import javax.transaction.Transactional;

import dev.gsitgithub.dao.repo.PersonRepository;
import dev.gsitgithub.generic.repo.specification.Filter;
import static dev.gsitgithub.generic.repo.specification.SpecificationBuilder.*;
import dev.gsitgithub.generic.service.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.gsitgithub.entity.Person;

@Service
@Transactional
public class PersonServiceImpl extends GenericServiceImpl<Person, Long> implements PersonService {
	private PersonRepository personRepository;
	
	@Autowired
	public PersonServiceImpl(PersonRepository personRepository) {
		super(personRepository);
		this.personRepository = personRepository;
	}

	@Override
	public List<Person> search(Filter filter) {
		return selectFrom(personRepository).leftJoin("address").where(filter).findAll();
	}
	
	@Override
	public List<Person> search(String filter) {
		return selectFrom(personRepository).leftJoin("address").where(filter).findAll();
	}
}
