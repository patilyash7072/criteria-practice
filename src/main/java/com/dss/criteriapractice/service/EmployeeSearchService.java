package com.dss.criteriapractice.service;

import com.dss.criteriapractice.dto.Employee;
import com.dss.criteriapractice.model.EmployeeEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeSearchService extends SearchService<EmployeeEntity, Employee> {
    public EmployeeSearchService(EntityManager em) {
        super(em, EmployeeEntity.class, EmployeeEntity::toDTO);
        setExcludedFields();
    }

    private void setExcludedFields() {
        List<String> excludedFields = new ArrayList<>();
//        excludedFields.add("firstName");
        super.setExcludedFields(excludedFields);
    }
}
