package dev.gsitgithub.service;

import static dev.gsitgithub.generic.api.specification.SpecificationBuilder.selectFrom;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.gsitgithub.dao.repo.EmployeeRepository;
import dev.gsitgithub.entity.Employee;
import dev.gsitgithub.generic.api.specification.Filter;
import dev.gsitgithub.generic.service.GenericServiceImpl;

@Service
@Transactional
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, Long> implements EmployeeService {
	private EmployeeRepository employeeRepository;
	
	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		super(employeeRepository);
		this.employeeRepository = employeeRepository;
	}

	@Override
	public List<Employee> search(Filter filter) {
		selectFrom(employeeRepository).where(filter).findAll();
		return null;
	}
	
}
