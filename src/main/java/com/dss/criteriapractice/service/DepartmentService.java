package com.dss.criteriapractice.service;

import com.dss.criteriapractice.dao.DepartmentRepository;
import com.dss.criteriapractice.dto.Department;
import com.dss.criteriapractice.model.DepartmentEntity;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService extends CrudService<DepartmentEntity, Long, Department> {

    public DepartmentService(DepartmentRepository departmentRepository) {
        super(departmentRepository, DepartmentEntity::toDTO, DepartmentEntity::toEntity);
    }
}
