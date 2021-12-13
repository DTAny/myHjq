package com.example.myhjq;

import com.example.myhjq.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

@SpringBootTest
class MyHjqApplicationTests {
    @Autowired
    JdbcTemplate template;

    LoginService loginService = new LoginService();

    @Test
    void contextLoads() throws SQLException {
        System.out.println(loginService.getAllManager(template));
        System.out.println(loginService.checkManager("123456","123456", template));
    }

}
