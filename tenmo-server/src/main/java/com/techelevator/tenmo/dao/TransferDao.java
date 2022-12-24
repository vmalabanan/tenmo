package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao
{
    Transfer makeOrRequestTransfer(Transfer transfer, int id);

    List<Transfer> getAllTransfers(int id);


}
