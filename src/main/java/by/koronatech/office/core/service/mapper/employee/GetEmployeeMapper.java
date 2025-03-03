package by.koronatech.office.core.service.mapper.employee;

import by.koronatech.office.api.dto.employee.GetEmployeeDTO;
import by.koronatech.office.core.entity.Employee;
import by.koronatech.office.core.service.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface GetEmployeeMapper extends BaseMapper<Employee, GetEmployeeDTO> {

    @Override
    @Mapping(source = "department.name", target = "departmentName")
    GetEmployeeDTO toDto(Employee employee);
}
