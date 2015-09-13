package dev.gsitgithub.dao;

import java.util.List;

import dev.gsitgithub.entity.Employee;

public interface EmployeeDAO 
{
    public void addEmployee(Employee employee);
    public List<Employee> getAllEmployees();
    public void deleteEmployee(Integer employeeId);
}