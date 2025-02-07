package by.koronatech.office.api.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor

public class GetEmployeeDTO {
    private Long id;
    private String name;
    private BigDecimal salary;
    private String departmentName;
    private Boolean isManager;
}
