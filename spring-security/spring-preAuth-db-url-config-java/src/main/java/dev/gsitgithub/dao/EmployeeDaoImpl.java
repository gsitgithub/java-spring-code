package dev.gsitgithub.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.gsitgithub.entity.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDAO  {
	
	@Autowired
    private SessionFactory sessionFactory;
	
	public void addEmployee(Employee employee) {
		this.sessionFactory.getCurrentSession().save(employee);
	}

	@SuppressWarnings("unchecked")
	public List<Employee> getAllEmployees() {
		return this.sessionFactory.getCurrentSession().createQuery("from Employee").list();
	}

	public void deleteEmployee(Integer employeeId) {
		Employee employee = (Employee) sessionFactory.getCurrentSession().load(
				Employee.class, employeeId);
        if (null != employee) {
        	this.sessionFactory.getCurrentSession().delete(employee);
        }
	}
}
