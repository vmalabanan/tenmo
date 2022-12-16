package com.techelevator.tenmo.dao.jdbc;

import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int id) {

        BigDecimal balance = null;

        String sql = "SELECT balance " +
                "FROM account " +
                "WHERE user_id = ?;";

        balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);

        return balance;
    }

}
