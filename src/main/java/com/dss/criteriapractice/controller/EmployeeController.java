package com.dss.criteriapractice.controller;

import com.dss.criteriapractice.dto.Employee;
import com.dss.criteriapractice.service.EmployeeService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GET
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GET
    @Path("/{id}")
    public Employee getEmployeeById(@PathParam("id") Long id) {
        return employeeService.findById(id);
    }

    @POST
    public Employee createEmployee(Employee employee) {
        return employeeService.save(employee);
    }

    @PUT
    public Employee updateEmployee(Employee employee) {
        return employeeService.update(employee);
    }

    @DELETE
    @Path("/{id}")
    public void deleteEmployee(@PathParam("id") Long id) {
        employeeService.deleteById(id);
    }

    @GET
    @Path("/search/{searchText}")
    public List<Employee> searchEmployees(@PathParam("searchText") String searchText) {
        return employeeService.search(searchText);
    }
}
