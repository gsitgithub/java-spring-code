package dev.gsitgithub.dao.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import dev.gsitgithub.entity.Employee;
import dev.gsitgithub.entity.Person;
import dev.gsitgithub.generic.repo.GenericRepository;

public interface PersonRepository extends GenericRepository<Person, Long>
//extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Object> 
{


}