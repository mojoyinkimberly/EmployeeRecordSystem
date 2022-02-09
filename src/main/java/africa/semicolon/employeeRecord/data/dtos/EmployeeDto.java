package africa.semicolon.employeeRecord.data.dtos;

import lombok.Data;

@Data
public class EmployeeDto {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
}
