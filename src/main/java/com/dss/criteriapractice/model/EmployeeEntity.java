package com.dss.criteriapractice.model;


import com.dss.criteriapractice.dto.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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


    public Employee toDTO() {
        return Employee.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    public static EmployeeEntity fromDTO(Employee employee) {

        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .build();

        if (employee.getId() != null) {
            employeeEntity.setId(employee.getId());
        }

        return employeeEntity;
    }
}
