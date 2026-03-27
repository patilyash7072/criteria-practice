package com.dss.criteriapractice.model;


import com.dss.criteriapractice.dto.Employee;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_employee")
@SequenceGenerator(name = "emp_seq", sequenceName = "emp_seq", allocationSize = 1)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;


    @ManyToOne
    @JoinColumn(name = "dept_id")
    private DepartmentEntity department;

    public Employee toDTO() {
        return Employee.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .department(department.toDTO())
                .build();
    }

    public static EmployeeEntity fromDTO(Employee employee) {

        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .department(DepartmentEntity.toEntity(employee.getDepartment()))
                .build();

        if (employee.getId() != null) {
            employeeEntity.setId(employee.getId());
        }

        return employeeEntity;
    }
}
