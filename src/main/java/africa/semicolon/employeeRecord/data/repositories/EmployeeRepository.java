package africa.semicolon.employeeRecord.data.repositories;

import africa.semicolon.employeeRecord.data.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
