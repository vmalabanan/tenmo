package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.models.Transfer;

import java.util.List;

public interface TransferDao
{
    Transfer handleTransfer(Transfer transfer, int id);

    List<Transfer> getAllTransfers(int id);

    List<Transfer> getPendingTransfers(int id);

    Transfer getTransferById(int transferId);


}
