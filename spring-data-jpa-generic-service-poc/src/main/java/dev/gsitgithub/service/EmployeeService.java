package dev.gsitgithub.service;

import java.util.List;

import dev.gsitgithub.entity.Employee;
import dev.gsitgithub.generic.repo.specification.Filter;
import dev.gsitgithub.generic.service.GenericService;

public interface EmployeeService extends GenericService<Employee, Long> {
	List<Employee> search(Filter filter );
}
