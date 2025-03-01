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
public class UpdateEmployeeDTO {
    private String name;
    private BigDecimal salary;
    private Boolean isManager;
    private Long departmentId;
}
