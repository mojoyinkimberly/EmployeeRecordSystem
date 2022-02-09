package africa.semicolon.employeeRecord;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class EmployeeRecordApplicationTest {

    @Autowired
    DataSource dataSource;

    @Test
    void applicationCanConnectToDatabaseTest(){
        assertThat(dataSource).isNotNull();
        Connection connection;
        try{
            connection = dataSource.getConnection();
            assertThat(connection).isNotNull();
            log.info("Connection -> {}", connection.getSchema());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}