package by.koronatech.office.core.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import by.koronatech.office.api.dto.GetDepartmentDTO;
import by.koronatech.office.core.entity.Department;
import by.koronatech.office.core.repository.DepartmentRepository;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public Page<GetDepartmentDTO> getDepartments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departmentPage = departmentRepository.findAll(pageable);

        return departmentPage.map(department ->
                new GetDepartmentDTO(department.getId(), department.getName())
        );
    }
}
