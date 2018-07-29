package dev.gsitgithub.service;

import java.util.List;

import dev.gsitgithub.entity.Employee;
import dev.gsitgithub.generic.api.GenericService;
import dev.gsitgithub.generic.api.specification.Filter;

public interface EmployeeService extends GenericService<Employee, Long> {
	List<Employee> search(Filter filter );
}
