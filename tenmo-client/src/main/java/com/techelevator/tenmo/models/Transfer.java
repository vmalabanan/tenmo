package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private int transferTypeId;
    private String transferTypeDesc;
    private int transferStatusId;
    private String transferStatusDesc;
    private BigDecimal amount;
    private User userFrom;
    private User userTo;
    // the date/time of the latest activity on a transaction (e.g., if a transfer was pending and then approved, this will represent the approved date/time)
    // this is set on the server side when a transfer is entered into the DB. Note: tried using LocalDateTime instead of String but this resulted in conversion errors
    private String transferDateTime;

    // String message to accompany the transaction
    private String transferMessage;

    // Note: Since client should not know the account number of the user they're sending money to
    // and server will determine the client's account number,
    // Transfer won't keep track of account numbers

    public Transfer() {
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }


    public String getTransferDateTime() {
        return transferDateTime;
    }

    public void setTransferDateTime(String transferDateTime) {
        this.transferDateTime = transferDateTime;
    }

    public String getTransferMessage() {
        return transferMessage;
    }

    public void setTransferMessage(String transferMessage) {
        this.transferMessage = transferMessage;
    }
}
