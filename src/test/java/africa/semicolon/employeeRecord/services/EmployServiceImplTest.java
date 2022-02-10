package africa.semicolon.employeeRecord.services;

import africa.semicolon.employeeRecord.data.dtos.EmployeeDto;
import africa.semicolon.employeeRecord.data.models.Employee;
import africa.semicolon.employeeRecord.data.repositories.EmployeeRepository;
import africa.semicolon.employeeRecord.web.exceptions.EmployeeRecordException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@Sql("/db/insert.sql")
class EmployServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;
    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Test that employee can be created by Employee Service")
    void createEmployeeTest() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setAddress("Bariga");
        employeeDto.setEmail("dammy@gmail.com");
        employeeDto.setFirstName("Dami");
        employeeDto.setLastName("Adeola");
        employeeDto.setPhoneNumber("0703245677");

        Employee employee = employeeService.createEmployee(employeeDto);

        assertThat(employee).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo("Dami");
        assertThat(employeeRepository.findAll().size()).isEqualTo(4);
        log.info("Employee record:: {}", employee);

    }

    @Test
    @DisplayName("All employees details can be retrieved")
    void findAll() {
        //given that there is repo
        //when
        List<Employee> employees = employeeService.findAll();
        //assert
        assertThat(employeeRepository.findAll().size()).isEqualTo(3);
        log.info("All employees details:: {}", employees);

    }

    @Test
    @DisplayName("Employee record can be updated")
    void updateEmployeeDetailsTest() throws IOException, JsonPatchException, EmployeeRecordException {
        //given that there is repo
        //when
        String jsonString = "[\n" +
                "  { \"op\": \"replace\", \"path\": \"/address\", \"value\": \"festac\" },\n" +
                "  { \"op\": \"replace\", \"path\": \"/lastName\", \"value\": \"debb\" },\n" +
                "  { \"op\": \"replace\", \"path\": \"/email\", \"value\": \"wisdom@gmail.com\" }\n" +
                "]";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonPatch patch = JsonPatch.fromJson(jsonNode);

        Employee updatedEmployee = employeeService.updateEmployee(10L, patch);
        assertThat(updatedEmployee.getAddress()).isEqualTo("festac");
        assertThat(updatedEmployee.getLastName()).isEqualTo("debb");
        log.info("The details of updated employee::{}", updatedEmployee);
    }
    @Test
    @DisplayName("Absolute update of employee details")
    void absoluteUpdateEmployeeDetailsTest() throws EmployeeRecordException {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setLastName("Sarimakin");
        employeeDto.setFirstName("Joy");
        employeeDto.setAddress("Fagun");
        employeeDto.setEmail("joy@gmail.com");
        employeeDto.setPhoneNumber("09012324565");

        Employee updatedEmployee = employeeService.updateEmployee(10L, employeeDto);

        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Joy");
        assertThat(updatedEmployee.getAddress()).isEqualTo("Fagun");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Sarimakin");
        log.info("The details of employee:: {}", updatedEmployee);
    }

    @Test
    @DisplayName("Remove employee from database test")
    void employeeCanBeRemovedTest() throws EmployeeRecordException {
        //given
        Employee retrievedEmployee = employeeRepository.findById(31L).orElse(null);
        assertThat(retrievedEmployee).isNotNull();
        //when
        employeeService.removeEmployeeById(31L);
        Employee queryEmployee = employeeRepository.findById(31L).orElse(null);
        //assert
        assertThat(queryEmployee).isNull();

    }

    @Test
    @DisplayName("Find employee by Id Test")
    void findEmployeeByIdTest() throws EmployeeRecordException {
        Employee employee = employeeService.findEmployeeById(31L);
        assertThat(employee).isNotNull();

    }
}