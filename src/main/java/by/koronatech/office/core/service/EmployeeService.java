package by.koronatech.office.core.service;

import by.koronatech.office.api.dto.employee.CreateEmployeeDTO;
import by.koronatech.office.api.dto.employee.GetEmployeeDTO;
import by.koronatech.office.api.dto.employee.UpdateEmployeeDTO;
import by.koronatech.office.core.entity.Department;
import by.koronatech.office.core.entity.Employee;
import by.koronatech.office.core.repository.DepartmentRepository;
import by.koronatech.office.core.repository.EmployeeRepository;
import by.koronatech.office.core.service.mapper.employee.CreateEmployeeMapper;
import by.koronatech.office.core.service.mapper.employee.GetEmployeeMapper;
import by.koronatech.office.core.service.mapper.employee.UpdateEmployeeMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final GetEmployeeMapper getEmployeeMapper;
    private final CreateEmployeeMapper createEmployeeMapper;
    private final UpdateEmployeeMapper updateEmployeeMapper;

    public Page<GetEmployeeDTO> getDepartmentEmployees(long departmentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> departmentEmployees = employeeRepository.findByDepartmentId(departmentId, pageable);

        return getEmployeeMapper.toPage(departmentEmployees);
    }

    public GetEmployeeDTO create(Long departmentId, @Valid CreateEmployeeDTO createEmployeeDTO) {
        Department department = findDepartment(departmentId);
        createEmployeeDTO.setDepartment(department);

        if (createEmployeeDTO.getIsManager() != null && createEmployeeDTO.getIsManager())
            dismissManager(department);

        Employee newEmployee = createEmployeeMapper.toEntity(createEmployeeDTO);
        employeeRepository.save(newEmployee);

        return getEmployeeMapper.toDto(newEmployee);
    }

    @Transactional
    public GetEmployeeDTO update(long employeeId, @Valid UpdateEmployeeDTO updateEmployeeDTO) {
        Employee employee = findEmployee(employeeId);

        if(updateEmployeeDTO.getDepartmentId() != null &&
                !updateEmployeeDTO.getDepartmentId().equals(employee.getDepartment().getId())) {
            Department department = findDepartment(updateEmployeeDTO.getDepartmentId());
            employee.setDepartment(department);
        }

        Employee saveEmployee = updateEmployeeMapper.merge(employee, updateEmployeeDTO);

        if (updateEmployeeDTO.getIsManager() != null) {
            if (updateEmployeeDTO.getIsManager()) dismissManager(saveEmployee.getDepartment());
            employee.setIsManager(updateEmployeeDTO.getIsManager());
        }

        return getEmployeeMapper.toDto(employeeRepository.save(saveEmployee));
    }

    private void dismissManager(Department department) {
        Optional<Employee> currentManager = employeeRepository.findByDepartmentAndIsManagerTrue(department);
        currentManager.ifPresent(manager -> {
            manager.setIsManager(false);
            employeeRepository.save(manager);
        });
    }

    public void appointAsManager(long departmentId, long employeeId) {
        Department department = findDepartment(departmentId);
        Employee newManager = findEmployee(employeeId);

        verifyDepartmentMember(newManager, departmentId);
        reappointmentEmployeeCheck(newManager);
        dismissManager(department);

        newManager.setIsManager(true);
        employeeRepository.save(newManager);
    }

    public void deleteEmployee(long id) {
        Employee employee = findEmployee(id);
        employeeRepository.delete(employee);
    }

    private void reappointmentEmployeeCheck(Employee employee) {
        if (employee.isManager()) {
            throw new IllegalStateException("Warning: This employee has already been appointed as a manager before!");
        }
    }

    private void verifyDepartmentMember(Employee employee, long departmentId) {
        if (!employee.getDepartment().getId().equals(departmentId)) {
            throw new IllegalStateException("Warning: The specialist is not part of the specified department!");
        }
    }

    private Department findDepartment(long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Department with ID= " + departmentId + " is not found."));
    }

    private Employee findEmployee(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee with ID= " + id + " is not found."));
    }
}
