package by.koronatech.office.api.dto.employee;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class GetEmployeeDTO {
    private Long id;
    private String name;
    private BigDecimal salary;
    private String departmentName;
    private Boolean isManager;
}
