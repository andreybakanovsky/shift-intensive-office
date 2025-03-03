package by.koronatech.office.api.controller;

import by.koronatech.office.api.dto.employee.CreateEmployeeDTO;
import by.koronatech.office.api.dto.employee.GetEmployeeDTO;
import by.koronatech.office.api.dto.employee.UpdateEmployeeDTO;
import by.koronatech.office.core.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/departments/{departmentId}/employees")
    public ResponseEntity<Page<GetEmployeeDTO>> getDepartmentEmployees(
            @PathVariable long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.getDepartmentEmployees(departmentId, page, size));
    }

    @PostMapping("/departments/{departmentId}/employees")
    public ResponseEntity<GetEmployeeDTO> create(@PathVariable Long departmentId,
                                                 @RequestBody CreateEmployeeDTO createEmployeeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.create(departmentId, createEmployeeDTO));
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<GetEmployeeDTO> update(@PathVariable Long id,
                                                 @RequestBody UpdateEmployeeDTO updateEmployeeDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(employeeService.update(id, updateEmployeeDTO));
    }

    @PatchMapping("/departments/{departmentId}/employees/{id}/appoint-manager")
    public ResponseEntity<String> appointAsManager(@PathVariable long departmentId, @PathVariable long id) {
        employeeService.appointAsManager(departmentId, id);
        return ResponseEntity.ok("Employee with id= " + id + " was assigned as a manager.");
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
