package com.dss.criteriapractice.controller;

import com.dss.criteriapractice.dto.Department;
import com.dss.criteriapractice.service.DepartmentService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;

@Path("/dept")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GET
    public List<Department> getAllDepartments() {
        return departmentService.findAll();
    }

    @GET
    @Path("{id}")
    public Department getDepartmentById(@PathParam("id") Long id) {
        return departmentService.findById(id);
    }

    @POST
    public Department create(Department department) {
        return departmentService.save(department);
    }

    @PUT
    public Department update(Department department) {
        return departmentService.update(department);
    }

    @DELETE
    public void delete(Department department) {
        departmentService.deleteById(department.getId());
    }

}
