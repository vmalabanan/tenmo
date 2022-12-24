package com.techelevator.dao;

import com.techelevator.tenmo.dao.jdbc.JdbcAccountDao;
import com.techelevator.tenmo.models.Account;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcAccountDaoTests extends BaseDaoTests {
    private static final Account ACCOUNT_1 = new Account(2001, 1001, BigDecimal.valueOf(550.00));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, BigDecimal.valueOf(600.00));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getBalance_returns_correct_balance_for_userId() {
        //arrange
        BigDecimal expectedBalance = BigDecimal.valueOf(550.00);

        //act
        BigDecimal actualBalance = sut.getBalance(ACCOUNT_1.getUserId());

        //assert
        String message = "Because getBalance should return the correct balance for user id.";
        assertEquals(expectedBalance, actualBalance, message);

    }

    @Test
    public void getAccountId_returns_correct_accountId() {
        //arrange

        //act

        //assert
    }




}
