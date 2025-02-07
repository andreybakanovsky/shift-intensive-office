package by.koronatech.office.api.controller;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.api.dto.GetEmployeeDTO;
import by.koronatech.office.core.service.EmployeeService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/departments/{departmentId}/employees")
@Slf4j
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<Page<GetEmployeeDTO>> getDepartmentEmployees(
            @PathVariable long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")[0]).ascending());

        Page<GetEmployeeDTO> employees = employeeService.getDepartmentEmployees(departmentId, pageable);
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@PathVariable Long departmentId,
                                            @RequestBody EmployeeDTO employeeDTO) {
        employeeDTO.setDepartmentId(departmentId);
        try {
            GetEmployeeDTO newEmployee = employeeService.createEmployee(employeeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long departmentId,
                                            @PathVariable Long id,
                                            @RequestBody EmployeeDTO employeeDTO) {
        try {
            GetEmployeeDTO employee = employeeService.updateEmployee(departmentId, id, employeeDTO);
            // в случае перевода сотрудника в другой отдел
            if (employeeDTO.getDepartmentId() != null &&
                    !employeeDTO.getDepartmentId().equals(departmentId)) {
                URI newDepartmentPath = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/departments/{newDepartmentId}/employees/{id}")
                        .buildAndExpand(employeeDTO.getDepartmentId(), id)
                        .toUri();
                return ResponseEntity.status(HttpStatus.SEE_OTHER) // 303
                        .location(newDepartmentPath)
                        .body(employee);
            } else return ResponseEntity.status(HttpStatus.OK).body(employee);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/assign-manager")
    public ResponseEntity<String> assignManager(@PathVariable long departmentId,
                                                @PathVariable long id) {
        try {
            employeeService.assignManager(departmentId, id);
            return ResponseEntity.ok("Сотрудник c id= " + id + " назначен менеджером.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable long departmentId,
                                               @PathVariable long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
