package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;



public class TransferService extends AuthenticatedApiService<Transfer>
{
    public Transfer makeTransfer(Transfer transfer)
    {
        Transfer newTransfer;

        try
        {
            var url = baseUrl + "transfer";
            var entity = makeAuthEntity(transfer);
            ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.POST, entity, Transfer.class);
            newTransfer = response.getBody();
        }
        catch(Exception ex)
        {
            newTransfer = null;
            BasicLogger.log(ex.getMessage());
        }

        return newTransfer;
    }


}
