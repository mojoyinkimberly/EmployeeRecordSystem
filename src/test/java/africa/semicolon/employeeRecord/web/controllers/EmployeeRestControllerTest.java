package africa.semicolon.employeeRecord.web.controllers;

import africa.semicolon.employeeRecord.data.dtos.EmployeeDto;
import africa.semicolon.employeeRecord.data.models.Employee;
import africa.semicolon.employeeRecord.data.repositories.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/db/insert.sql")
class EmployeeRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeRepository employeeRepository;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Add employee api test")
    void addEmployeeTest() throws Exception {
        Employee employee = new Employee();
        employee.setAddress("Alabama");
        employee.setLastName("Henry");
        employee.setFirstName("Mac");
        employee.setEmail("mac@gmail.com");
        employee.setPhoneNumber("091234567");

        String requestBody = objectMapper.writeValueAsString(employee);
        mockMvc.perform(post("/api/employees/")
                .contentType("application/json").content(requestBody))
                .andExpect(status().is(201)).andDo(print());

    }

    @Test
    @DisplayName("Get all employees api test")
    void getAllEmployeesTest() throws Exception {
        mockMvc.perform(get("/api/employees/")
                .contentType("application/json"))
                .andExpect(status().is(200)).andDo(print());
    }

    @Test
    @DisplayName("Get employee by Id api test")
    void getEmployeeByIdTest() throws Exception {
        mockMvc.perform(get("/api/employees/31")
                .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("Partial update employee api test")
    void updateEmployeeRecordTest() throws Exception {
        Employee employee = employeeRepository.findById(30L).orElse(null);
        assertThat(employee).isNotNull();

        mockMvc.perform(patch("/api/employees/30")
                .contentType("application/json-patch+json")
                .content(Files.readAllBytes(Path.of("/home/toska/Desktop/dev/employeeRecord/src/main/resources/updatedata.json"))))
                .andExpect(status().isAccepted()).andDo(print());
    }

    @Test
    @DisplayName("Absolute update employee api test")
    void testUpdateEmployeeRecord() throws Exception {
        Employee employee = employeeRepository.findById(31L).orElse(null);
        assertThat(employee).isNotNull();

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setPhoneNumber("07032654444");
        employeeDto.setEmail("alicia@gmail.com");
        employeeDto.setAddress("New York");
        employeeDto.setFirstName("Alicia");
        employeeDto.setLastName("Emily");

        String requestBody = objectMapper.writeValueAsString(employeeDto);

        mockMvc.perform(put("/api/employees/31")
                .contentType("application/json").content(requestBody))
                .andExpect(status().isAccepted()).andDo(print());
    }

    @Test
    @DisplayName("Delete employee api test")
    void removeEmployeeRecord() throws Exception {
        Employee queryEmployee = employeeRepository.findById(32L).orElse(null);
        assertThat(queryEmployee).isNotNull();

        mockMvc.perform(delete("/api/employees/32")
                .contentType("application/json")).andExpect(status().isOk())
                .andDo(print());
    }
}