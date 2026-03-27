package com.dss.criteriapractice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
}
