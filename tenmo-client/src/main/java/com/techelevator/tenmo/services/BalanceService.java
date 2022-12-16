package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public class BalanceService extends AuthenticatedApiService<Account> {

    public BigDecimal getCurrentBalance() {

        BigDecimal balance;

        try
        {
            var url = baseUrl + "balance";
            var entity = makeAuthEntity();
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, entity, Account.class);
            balance = response.getBody().getBalance();
        }
        catch(Exception ex)
        {
            balance = null;
            BasicLogger.log(ex.getMessage());
        }

        return balance;
    }

}

