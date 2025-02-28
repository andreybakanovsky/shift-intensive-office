package by.koronatech.office.api.controller;

import java.net.URI;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import by.koronatech.office.api.dto.EmployeeDTO;
import by.koronatech.office.api.dto.GetEmployeeDTO;
import by.koronatech.office.core.service.EmployeeService;

@RestController
@RequestMapping("/departments/{departmentId}/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<Page<GetEmployeeDTO>> getDepartmentEmployees(
            @PathVariable long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.getDepartmentEmployees(departmentId, page, size));
    }

    @PostMapping
    public ResponseEntity<GetEmployeeDTO> createEmployee(@PathVariable Long departmentId,
                                                         @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(departmentId, employeeDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetEmployeeDTO> updateEmployee(@PathVariable Long departmentId,
                                                         @PathVariable Long id,
                                                         @RequestBody EmployeeDTO employeeDTO) {
        GetEmployeeDTO employee = employeeService.updateEmployee(departmentId, id, employeeDTO);

        if (employeeDTO.getDepartmentId().equals(departmentId)) {
            return ResponseEntity.status(HttpStatus.OK).body(employee);
        } else {
            // In the case of transfer the employee to another department we need the redirect
            URI newDepartmentPath = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/departments/{newDepartmentId}/employees/{id}")
                    .buildAndExpand(employeeDTO.getDepartmentId(), id)
                    .toUri();
            return ResponseEntity.status(HttpStatus.SEE_OTHER) // 303
                    .location(newDepartmentPath)
                    .body(employee);
        }
    }

    @PatchMapping("/{id}/appoint-manager")
    public ResponseEntity<String> appointAsManager(@PathVariable long departmentId, @PathVariable long id) {
        employeeService.appointAsManager(departmentId, id);
        return ResponseEntity.ok("Employee with id= " + id + " was assigned as a manager.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable long id, @PathVariable String departmentId) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
