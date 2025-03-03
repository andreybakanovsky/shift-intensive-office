package by.koronatech.office.core.service.mapper.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import by.koronatech.office.api.dto.employee.CreateEmployeeDTO;
import by.koronatech.office.core.entity.Employee;
import by.koronatech.office.core.service.mapper.BaseMapper;

@Mapper(config = BaseMapper.class)
public interface CreateEmployeeMapper extends BaseMapper<Employee, CreateEmployeeDTO> {

    @Override
    @Mapping(source = "departmentId", target = "department.id")
    Employee toEntity(CreateEmployeeDTO createEmployeeDTO);
}
