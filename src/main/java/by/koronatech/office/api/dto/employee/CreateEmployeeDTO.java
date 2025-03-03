package by.koronatech.office.api.dto.employee;

import by.koronatech.office.core.entity.Department;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class CreateEmployeeDTO {
    private String name;
    private BigDecimal salary;
    private Boolean isManager;
    private Department department; // the reason is the department name requirement in the task
}
