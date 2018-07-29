package dev.gsitgithub.service;

import static dev.gsitgithub.generic.api.specification.SpecificationBuilder.selectFrom;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.gsitgithub.dao.repo.PersonRepository;
import dev.gsitgithub.entity.Person;
import dev.gsitgithub.generic.api.specification.Filter;
import dev.gsitgithub.generic.service.GenericServiceImpl;

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
