package dev.gsitgithub.dao.repo;


import dev.gsitgithub.entity.Person;
import dev.gsitgithub.generic.api.GenericRepository;

public interface PersonRepository extends GenericRepository<Person, Long>
//extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Object> 
{


}