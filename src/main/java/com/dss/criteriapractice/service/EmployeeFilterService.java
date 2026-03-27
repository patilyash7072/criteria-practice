package com.dss.criteriapractice.service;

import com.dss.criteriapractice.dto.Employee;
import com.dss.criteriapractice.model.EmployeeEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class EmployeeFilterService extends FilterService<EmployeeEntity, Employee> {
    public EmployeeFilterService(EntityManager em) {
        super(em, EmployeeEntity.class, EmployeeEntity::toDTO);
    }
}
