package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.models.Transfer;

import java.util.List;

public interface TransferDao
{
    Transfer createTransfer(Transfer transfer, int id);

    Transfer editTransfer(Transfer transfer, int id);

    List<Transfer> getAllTransfers(int id);

    List<Transfer> getPendingTransfers(int id);

    Transfer getTransferById(int transferId);


}
