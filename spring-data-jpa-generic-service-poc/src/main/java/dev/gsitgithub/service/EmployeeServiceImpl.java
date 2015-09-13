package dev.gsitgithub.service;

import javax.transaction.Transactional;

import dev.gsitgithub.dao.repo.EmployeeRepository;
import dev.gsitgithub.generic.service.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.gsitgithub.entity.Employee;

@Service
@Transactional
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, Long> implements EmployeeService {
	private EmployeeRepository employeeRepository;
	
	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		super(employeeRepository);
		this.employeeRepository = employeeRepository;
	}
	
}
