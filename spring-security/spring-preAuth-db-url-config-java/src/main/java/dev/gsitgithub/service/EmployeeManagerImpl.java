package dev.gsitgithub.service;

import java.util.List;

import dev.gsitgithub.dao.EmployeeDAO;
import dev.gsitgithub.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeManagerImpl implements EmployeeManager {
	
	@Autowired
    private EmployeeDAO employeeDAO;

	@Transactional
	public void addEmployee(Employee employee) {
		employeeDAO.addEmployee(employee);
	}

	@Transactional
	public List<Employee> getAllEmployees() {
		return employeeDAO.getAllEmployees();
	}

	@Transactional
	public void deleteEmployee(Integer employeeId) {
		employeeDAO.deleteEmployee(employeeId);
	}

	public void setEmployeeDAO(EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}
}
