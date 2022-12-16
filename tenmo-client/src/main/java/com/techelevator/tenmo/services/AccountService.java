package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public class AccountService extends AuthenticatedApiService<Account> {

    public BigDecimal getCurrentBalance() {

        BigDecimal balance = null;

        try
        {
            var url = baseUrl + "balance";
            var entity = makeAuthEntity();
            ResponseEntity<BigDecimal> response = restTemplate.exchange(url, HttpMethod.GET, entity, BigDecimal.class);

            balance = response.getBody();
        }
        catch(Exception ex)
        {
            BasicLogger.log(ex.getMessage());
        }

        return balance;
    }

}

