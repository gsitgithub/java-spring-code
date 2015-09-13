package dev.gsitgithub.dao;

import java.util.List;

import dev.gsitgithub.entity.Employee;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDaoImpl implements EmployeeDAO  {
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@PreAuthorize("hasRole('ROLE_RIGHT_USER')")
	public void addEmployee(Employee employee) {
		//System.out.println(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities());
		this.sessionFactory.getCurrentSession().save(employee);
	}

	@SuppressWarnings("unchecked")
	public List<Employee> getAllEmployees() {
		return this.sessionFactory.getCurrentSession().createQuery("from Employee").list();
	}

	@PreAuthorize("hasRole('ROLE_RIGHT_ADMIN')")
	public void deleteEmployee(Integer employeeId) {
		Employee employee = (Employee) sessionFactory.getCurrentSession().load(
				Employee.class, employeeId);
        if (null != employee) {
        	this.sessionFactory.getCurrentSession().delete(employee);
        }
	}
}
