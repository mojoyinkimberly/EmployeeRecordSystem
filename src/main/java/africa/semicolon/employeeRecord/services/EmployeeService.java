package africa.semicolon.employeeRecord.services;

import africa.semicolon.employeeRecord.data.dtos.EmployeeDto;
import africa.semicolon.employeeRecord.data.models.Employee;
import africa.semicolon.employeeRecord.web.exceptions.EmployeeRecordException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(EmployeeDto employeeDto);
    List<Employee> findAll();
    Employee updateEmployee(Long employeeId, JsonPatch patch) throws EmployeeRecordException, JsonPatchException, JsonProcessingException;
    Employee updateEmployee(Long employeeId, EmployeeDto employeeDto) throws EmployeeRecordException;

    void removeEmployeeById(long employeeId) throws EmployeeRecordException;

    Employee findEmployeeById(long employeeId) throws EmployeeRecordException;
}
