package africa.semicolon.employeeRecord.data.repositories;

import africa.semicolon.employeeRecord.data.models.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Sql("/db/insert.sql")
class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Employee can be created test")
    void registerEmployeeInDatabaseTest(){
        Employee employee = new Employee();
        employee.setEmail("toska@gmail.com");
        employee.setAddress("Semicolon");
        employee.setFirstName("Emma");
        employee.setLastName("Toska");
        employee.setPhoneNumber("09012345789");
        employeeRepository.save(employee);
        assertThat(employeeRepository.findAll()).isNotNull();
        log.info("The employee details::{}", employee);
        assertThat(employee.getId()).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo("Emma");
    }

    @Test
    @DisplayName("Test that employee can be read by Id")
    void testThatEmployeeCanBeRetrievedById(){
        Employee employee = employeeRepository.findById(10L).orElse(null);
        assertThat(employee.getFirstName()).isEqualTo("Gideon");
        log.info("Employee details:: {}", employee);

    }

    @Test
    @DisplayName("Employee details can be updated test")
    void employeeDetailsCanBeUpdatedTest(){
        Employee employee = employeeRepository.findById(10L).orElse(null);
        assertThat(employee).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo("Gideon");
        employee.setAddress("Semicolon Yaba");
        employee.setFirstName("Ogidi");
        employeeRepository.save(employee);
        assertThat(employee.getFirstName()).isEqualTo("Ogidi");
        assertThat(employee.getAddress()).isEqualTo("Semicolon Yaba");
        log.info("Employee details :: {}", employee);
    }

    @Test
    @DisplayName("Employee can be removed from database")
    void employeeCanBeRemovedTest(){
        //given
        Employee employee = new Employee();
        employee.setEmail("toska@gmail.com");
        employee.setAddress("Semicolon");
        employee.setFirstName("Emma");
        employee.setLastName("Toska");
        employee.setPhoneNumber("09012345789");
        employeeRepository.save(employee);

        assertThat(employee.getLastName()).isEqualTo("Toska");

        employeeRepository.delete(employee);
        employeeRepository.deleteById(30L);
        Employee retrievedEmployee = employeeRepository.findById(30L).orElse(null);
        assertThat(retrievedEmployee).isNull();
        log.info("records of employees in db:: {}", employeeRepository.findAll());
        assertThat(employeeRepository.findAll().size()).isEqualTo(2);
    }
}