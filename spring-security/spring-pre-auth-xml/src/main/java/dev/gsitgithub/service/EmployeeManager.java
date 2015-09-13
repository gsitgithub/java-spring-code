package dev.gsitgithub.service;

import java.util.List;

import dev.gsitgithub.entity.Employee;

public interface EmployeeManager {
	public void addEmployee(Employee employee);
    public List<Employee> getAllEmployees();
    public void deleteEmployee(Integer employeeId);
}
