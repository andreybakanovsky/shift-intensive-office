package by.koronatech.office.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import by.koronatech.office.core.entity.Department;
import by.koronatech.office.core.entity.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByDepartmentId(long departmentId, Pageable pageable);

    Optional<Employee> findByDepartmentAndIsManagerTrue(Department department);
}

