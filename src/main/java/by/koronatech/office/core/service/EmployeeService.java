package by.koronatech.office.core.service;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.api.dto.GetEmployeeDTO;
import by.koronatech.office.core.entity.Department;
import by.koronatech.office.core.entity.Employee;
import by.koronatech.office.core.repository.DepartmentRepository;
import by.koronatech.office.core.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class EmployeeService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public Page<GetEmployeeDTO> getDepartmentEmployees(long departmentId, Pageable pageable) {
        Page<Employee> departmentEmployees = employeeRepository.findByDepartmentId(departmentId, pageable);

        return departmentEmployees.map(employee -> new GetEmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getSalary(),
                employee.getDepartment().getName(),
                employee.isManager()
        ));
    }

    public GetEmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Department department = findDepartment(employeeDTO.getDepartmentId());
        if (employeeDTO.getIsManager()) {
            dismissManager(department);
        }

        Employee newEmployee = new Employee();
        newEmployee.setDepartment(department);
        newEmployee.setName(employeeDTO.getName());
        newEmployee.setSalary(employeeDTO.getSalary());
        newEmployee.setIsManager(employeeDTO.getIsManager());
        employeeRepository.save(newEmployee);

        return new GetEmployeeDTO(
                newEmployee.getId(),
                newEmployee.getName(),
                newEmployee.getSalary(),
                newEmployee.getDepartment().getName(),
                newEmployee.getIsManager());
    }

    public GetEmployeeDTO updateEmployee(long departmentId, long id, EmployeeDTO employeeDTO) {
        Employee employee = findEmployee(id);
        Department newDepartment = null;

        if (employeeDTO.getName() != null && !employeeDTO.getName().isEmpty())
            employee.setName(employeeDTO.getName());
        if (employeeDTO.getSalary() != null) employee.setSalary(employeeDTO.getSalary());

        if (employeeDTO.getDepartmentId() != null &&
                employeeDTO.getDepartmentId() != departmentId) {
            newDepartment = findDepartment(employeeDTO.getDepartmentId());
            employee.setDepartment(newDepartment);
        }

        if (employeeDTO.getIsManager() != null) {
            if (employeeDTO.getIsManager()) dismissManager(newDepartment);
            employee.setIsManager(employeeDTO.getIsManager());
        }

        employeeRepository.save(employee);
        return new GetEmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getSalary(),
                employee.getDepartment().getName(),
                employee.getIsManager());
    }

    private void dismissManager(Department department) {
        Optional<Employee> currentManager = employeeRepository.findByDepartmentAndIsManagerTrue(department);
        currentManager.ifPresent(manager -> {
            manager.setIsManager(false);
            employeeRepository.save(manager);
        });
    }

    public void assignManager(long departmentId, long employeeId) {
        Department department = findDepartment(departmentId);
        Employee newManager = findEmployee(employeeId);

        verifyDepartmentMember(newManager, departmentId);
        reassignmentEmployeeCheck(newManager);
        dismissManager(department);

        newManager.setIsManager(true);
        employeeRepository.save(newManager);
    }

    public void deleteEmployee(long id) {
        Employee employee = findEmployee(id);

        employeeRepository.delete(employee);
    }

    private void reassignmentEmployeeCheck(Employee employee) {
        if (employee.isManager()) {
            throw new IllegalStateException("Внимание: данный сотрудник ранее уже был назначен менеджером!");
        }
    }

    private void verifyDepartmentMember(Employee employee, long departmentId) {
        if (!employee.getDepartment().getId().equals(departmentId)) {
            throw new IllegalStateException("Внимание: специалист не принадлежит указанному отделу!");
        }
    }


    private Department findDepartment(long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Отдел с ID= " + departmentId + " не найден"));
    }

    private Employee findEmployee(long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с ID= " + id + " не найден"));
    }
}
