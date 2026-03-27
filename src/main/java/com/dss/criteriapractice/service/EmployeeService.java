package com.dss.criteriapractice.service;

import com.dss.criteriapractice.dao.EmployeeRepository;
import com.dss.criteriapractice.dto.Employee;
import com.dss.criteriapractice.model.EmployeeEntity;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmployeeService extends CrudService<EmployeeEntity, Long, Employee> {

    @Autowired
    private final EmployeeSearchService employeeSearchService;

    @Autowired
    private final EmployeeFilterService employeeFilterService;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeSearchService employeeSearchService, EmployeeFilterService employeeFilterService) {
        super(employeeRepository, EmployeeEntity::toDTO, EmployeeEntity::fromDTO);
        this.employeeSearchService = employeeSearchService;
        this.employeeFilterService = employeeFilterService;
    }

    public List<Employee> search(String search) {
        return employeeSearchService.search(search);
    }

    public List<Employee> filter(Employee employee) {
        return employeeFilterService.filter(employee);
    }

}
