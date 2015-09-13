package dev.gsitgithub.controller;

import java.security.Principal;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dev.gsitgithub.entity.Employee;
import dev.gsitgithub.services.EmployeeService;

@Controller
public class EditEmployeeController {
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	@Autowired
	private EmployeeService employeeService;
	
	public void setEmployeeManager(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String defaultPage() {
    	return "index";
	}
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public String authPage(ModelMap map, Principal principal) {
/*		Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String[] user1 = (String[])SecurityContextHolder.getContext().getAuthentication().getCredentials();
		LOG.info(((User)user).getUsername());
		LOG.info(Arrays.toString(user1));*/
		if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
			return "redirect:/list";
		return "login";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_HOME','ROLE_EMPLOYEE')")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String listEmployees(ModelMap map) {
		map.addAttribute("employee", new Employee());
		map.addAttribute("employeeList", employeeService.findAll());
		return "editEmployeeList";
	}

	@PreAuthorize("hasAnyRole('ROLE_HOME','ROLE_EMPLOYEE')")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addEmployee(
			@ModelAttribute(value = "employee") Employee employee,
			BindingResult result) {
		employeeService.create(employee);
		return "redirect:/list";
	}

	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
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
