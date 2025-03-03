package by.koronatech.office.core.service.mapper.employee;

import by.koronatech.office.api.dto.employee.UpdateEmployeeDTO;
import by.koronatech.office.core.entity.Employee;
import by.koronatech.office.core.service.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface UpdateEmployeeMapper extends BaseMapper<Employee, UpdateEmployeeDTO> {
}
