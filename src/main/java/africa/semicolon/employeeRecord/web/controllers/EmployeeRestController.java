package africa.semicolon.employeeRecord.web.controllers;

import africa.semicolon.employeeRecord.data.dtos.EmployeeDto;
import africa.semicolon.employeeRecord.data.models.Employee;
import africa.semicolon.employeeRecord.services.EmployeeService;
import africa.semicolon.employeeRecord.web.exceptions.EmployeeRecordException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("")
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeDto employeeDto){
        try{
            Employee employee = employeeService.createEmployee(employeeDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(employee);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllEmployees(){
        List<Employee> employees = employeeService.findAll();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long employeeId){
        try {
            Employee employee = employeeService.findEmployeeById(employeeId);
            return ResponseEntity.ok().body(employee);
        } catch (EmployeeRecordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{employeeId}")
    public ResponseEntity<?> updateEmployeeRecord(@PathVariable Long employeeId, @RequestBody JsonPatch employeePatch){
        try {
            Employee employee = employeeService.updateEmployee(employeeId, employeePatch);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(employee);
        } catch (JsonPatchException | EmployeeRecordException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<?> updateEmployeeRecord(@PathVariable Long employeeId, @RequestBody EmployeeDto employeeDto){
        try{
            Employee employee = employeeService.updateEmployee(employeeId, employeeDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(employee);
        } catch (EmployeeRecordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> removeEmployeeRecord(@PathVariable Long employeeId){
        try{
            employeeService.removeEmployeeById(employeeId);
            return ResponseEntity.ok().body("Removed successfully");
        } catch (EmployeeRecordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
