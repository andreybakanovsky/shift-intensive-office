package by.koronatech.office.api.controller;

import by.koronatech.office.api.dto.GetDepartmentDTO;
import by.koronatech.office.core.entity.Department;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import by.koronatech.office.core.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/departments")
@Slf4j
@AllArgsConstructor
public class DepartmentController {
    private final DepartmentRepository departmentRepository;

    @GetMapping
    public ResponseEntity<Page<GetDepartmentDTO>> getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departmentPage = departmentRepository.findAll(pageable);

        Page<GetDepartmentDTO> dtoPage = departmentPage.map(department ->
                new GetDepartmentDTO(department.getId(), department.getName())
        );

        return ResponseEntity.ok(dtoPage);
    }
}
