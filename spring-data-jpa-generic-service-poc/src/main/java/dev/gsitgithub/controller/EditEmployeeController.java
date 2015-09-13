package dev.gsitgithub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dev.gsitgithub.entity.Employee;
import dev.gsitgithub.service.EmployeeService;

@Controller
public class EditEmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	public void setEmployeeManager(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String defaultPage(ModelMap map) {
		return "redirect:/list";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_RIGHT_HOME','ROLE_RIGHT_EMPLOYEE')")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String listEmployees(ModelMap map) {
		map.addAttribute("employee", new Employee());
		map.addAttribute("employeeList", employeeService.findAll());
		return "editEmployeeList";
	}

	@PreAuthorize("hasAnyRole('ROLE_RIGHT_HOME','ROLE_RIGHT_EMPLOYEE')")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addEmployee(
			@ModelAttribute(value = "employee") Employee employee,
			BindingResult result) {
		employeeService.create(employee);
		return "redirect:/list";
	}

	@PreAuthorize("hasRole('ROLE_RIGHT_EMPLOYEE')")
	@RequestMapping("/delete/{employeeId}")
	public String deleteEmplyee(@PathVariable("employeeId") Integer employeeId) {
		Employee e = new Employee();
		e.setId(employeeId);
		employeeService.delete(e);
		return "redirect:/list";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap model) {
		return "login";
	}

	@RequestMapping(value = "/accessdenied", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		model.addAttribute("error", "true");
		return "denied";
	}

	@RequestMapping(value = "/logoutpage", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		return "logout";
	}
}
