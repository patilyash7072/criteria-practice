package com.dss.criteriapractice.model;

import com.dss.criteriapractice.dto.Department;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_dept")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "dept_seq", sequenceName = "tbl_dept_seq", allocationSize = 1)
public class DepartmentEntity extends BaseEntity {

    private String name;

    public Department toDTO() {
        return Department.builder().id(id).name(name).build();
    }

    public static DepartmentEntity toEntity(Department department) {
        DepartmentEntity entity = DepartmentEntity.builder()
                .name(department.getName())
                .build();

        if (department.getId() != null) {
            entity.setId(department.getId());
        }

        return entity;
    }
}
