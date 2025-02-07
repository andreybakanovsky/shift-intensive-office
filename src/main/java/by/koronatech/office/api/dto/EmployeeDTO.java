package by.koronatech.office.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor

public class EmployeeDTO {
    private String name;
    private BigDecimal salary;
    private Long departmentId;
    private Boolean isManager;
}
