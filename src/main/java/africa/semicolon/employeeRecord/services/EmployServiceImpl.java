package africa.semicolon.employeeRecord.services;

import africa.semicolon.employeeRecord.data.dtos.EmployeeDto;
import africa.semicolon.employeeRecord.data.models.Employee;
import africa.semicolon.employeeRecord.data.repositories.EmployeeRepository;
import africa.semicolon.employeeRecord.web.exceptions.EmployeeRecordException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(EmployeeDto employeeDto) {
        if(employeeDto == null){
            throw new IllegalArgumentException("Argument can not be null");
        }
        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setAddress(employeeDto.getAddress());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Long employeeId, JsonPatch employeePatch) throws EmployeeRecordException, JsonPatchException, JsonProcessingException {
        Optional<Employee> employeeQuery = employeeRepository.findById(employeeId);
        if(employeeQuery.isEmpty()){
            throw new EmployeeRecordException("Employee can not be null");
        }
        Employee targetEmployee = employeeQuery.get();

        try{
            targetEmployee = applyPatchToEmployee(employeePatch, targetEmployee);
            return saveOrUpdate(targetEmployee);
        }catch (JsonPatchException | JsonProcessingException ex){
            throw new EmployeeRecordException("Update Failed");
        }

    }

    @Override
    public Employee updateEmployee(Long employeeId, EmployeeDto employeeDto) throws EmployeeRecordException {
        Employee retrievedEmployee = employeeRepository.findById(employeeId).orElse(null);
        if(retrievedEmployee == null) throw new EmployeeRecordException("Employee does not exist");

        //update retrievedEmployee details
        retrievedEmployee.setEmail(employeeDto.getEmail());
        retrievedEmployee.setAddress(employeeDto.getAddress());
        retrievedEmployee.setLastName(employeeDto.getLastName());
        retrievedEmployee.setFirstName(employeeDto.getFirstName());
        retrievedEmployee.setPhoneNumber(employeeDto.getPhoneNumber());

        return employeeRepository.save(retrievedEmployee);
    }

    @Override
    public void removeEmployeeById(long employeeId) throws EmployeeRecordException {
        Optional<Employee> queryEmployee = employeeRepository.findById(employeeId);
        if (queryEmployee.isEmpty()) throw new EmployeeRecordException("Employee does not exist in the database");
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public Employee findEmployeeById(long employeeId) throws EmployeeRecordException {
        Optional<Employee> queryEmployee = employeeRepository.findById(employeeId);
        if(queryEmployee.isEmpty()) throw new EmployeeRecordException("Employee with id "+ employeeId + "is not present");

        return queryEmployee.get();
    }

    private Employee applyPatchToEmployee(JsonPatch employeePatch, Employee targetEmployee) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = employeePatch
                .apply(objectMapper.convertValue(targetEmployee, JsonNode.class));
        return objectMapper.treeToValue(patched, Employee.class);
    }

    private Employee saveOrUpdate(Employee employee) throws EmployeeRecordException {
        if(employee == null) throw new EmployeeRecordException("Employee can't be null");
        return employeeRepository.save(employee);
    }
}
