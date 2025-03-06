package by.koronatech.office.api.dto.employee;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UpdateEmployeeDTO {
    private String name;
    private BigDecimal salary;
    private Boolean isManager;
    private Long departmentId;
}
