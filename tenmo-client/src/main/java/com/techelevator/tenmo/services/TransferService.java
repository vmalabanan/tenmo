package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class TransferService extends AuthenticatedApiService<Transfer>
{
    public Transfer makeOrRequestTransfer(Transfer transfer)
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


    public List<Transfer> getAllTransfers() {
        List<Transfer> transfers;

        try
        {
            var url = baseUrl + "transfer";
            var entity = makeAuthEntity();
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            transfers = Arrays.asList(Objects.requireNonNull(response.getBody()));
        }
        catch(Exception ex)
        {
            transfers = null;
            BasicLogger.log(ex.getMessage());

        }
        return transfers;
    }

}
