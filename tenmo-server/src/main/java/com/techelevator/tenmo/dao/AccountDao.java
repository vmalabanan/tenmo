package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import com.techelevator.tenmo.model.Account;

public interface AccountDao {
    BigDecimal getBalance(int id);
}
