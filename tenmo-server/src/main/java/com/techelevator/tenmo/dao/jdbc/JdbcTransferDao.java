package com.techelevator.tenmo.dao.jdbc;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao
{

    private final JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    @Autowired
    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }

    @Override
    public Transfer makeTransfer(Transfer transfer, int id)
    {
        // if transfer is not valid, set transferStatusId = 3 (rejected) and return the transfer.
        // Transaction will not be added to the transfer table
        if (!isTransferValid(transfer, id)) {
            transfer.setTransferStatusId(3);
            return transfer;
        }

        // finish building the transfer object
        buildTransferObject(transfer, id);

        String sql = "BEGIN TRANSACTION; " +
                "UPDATE account SET balance = balance - ? WHERE account_id = ?; " +
                "UPDATE account SET balance = balance + ? WHERE account_id = ?; " +
                "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?); " +
                "COMMIT;";

       jdbcTemplate.update(sql,
                           transfer.getAmount(), transfer.getAccountFrom(),
                           transfer.getAmount(), transfer.getAccountTo(),
                           transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

        // TODO: Decide if we should set transferId attribute for the Transfer object once the above SQL query is run.
        //  We'd need to run a separate SQL query for this because we can't do a returning clause from within a transaction.
        //  One argument against setting transferId is that we don't do anything with this info.
        //  But we do need some way to alert the client if the transaction didn't go through.
        //  For example, if transferId == null, throw an exception

        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfers(int id) {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT t.transfer_id " +
                ", tu.username AS username_from " +
                ", tu.user_id AS user_id_from " +
                ", t.account_from " +
                ", tu2.username AS username_to " +
                ", tu2.user_id AS user_id_to " +
                ", t.account_to " +
                ", tt.transfer_type_desc " +
                ", t.transfer_type_id " +
                ", ts.transfer_status_desc " +
                ", t.transfer_status_id " +
                ", t.amount " +
                "FROM transfer AS t " +
                "JOIN account AS a " +
                "ON t.account_from = a.account_id " +
                "JOIN account AS a2 " +
                "ON t.account_to = a2.account_id " +
                "JOIN tenmo_user AS tu " +
                "ON a.user_id = tu.user_id " +
                "JOIN tenmo_user AS tu2 " +
                "ON a2.user_id = tu2.user_id " +
                "JOIN transfer_type AS tt " +
                "ON t.transfer_type_id = tt.transfer_type_id " +
                "JOIN transfer_status AS ts " +
                "ON t.transfer_status_id = ts.transfer_status_id " +
                "WHERE a.user_id = ? OR a2.user_id = ?; ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }

        return transfers;
    }

    // helper function
    private boolean isTransferValid(Transfer transfer, int id) {
        // check if:
        // userIdFrom != userIdTo,
        // amount > 0,
        // amount <= amount in userIdFrom's account,
        // userIdTo is valid
        return (id != transfer.getUserIdTo()) &&
                (transfer.getAmount().compareTo(BigDecimal.ZERO) > 0) &&
                (accountDao.getBalance(id).compareTo(transfer.getAmount()) >= 0) &&
                (isUserIdToValid(transfer.getUserIdTo()));
    }

    // helper function to determine whether there is a matching accountId for userIdTo
    private boolean isUserIdToValid(int userIdTo) {
        return accountDao.getAccountId(userIdTo) != null;
    }

    // helper function to finish building the Transfer object, since
    // Transfer object from the client only contains userIdTo and amount
    private void buildTransferObject(Transfer transfer, int id) {
        Integer accountFrom = accountDao.getAccountId(id);
        Integer accountTo = accountDao.getAccountId(transfer.getUserIdTo());
        transfer.setUserIdFrom(id);
        transfer.setTransferTypeId(2); // 2 is for Send
        transfer.setTransferStatusId(2); // 2 is for Approved
        transfer.setAccountFrom(accountFrom);
        transfer.setAccountTo(accountTo);
    }

    // helper function
    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_from")); // Do I need to set this?
        transfer.setAccountTo(results.getInt("account_to")); // Do I need to set this? The client shouldn't see this info anyway
        transfer.setAmount(results.getBigDecimal("amount"));
        transfer.setTransferTypeDesc(results.getString("transfer_type_desc"));
        transfer.setTransferStatusDesc(results.getString("transfer_status_desc"));
        transfer.setUserIdFrom(results.getInt("user_id_from"));
        transfer.setUserIdTo(results.getInt("user_id_to"));
        transfer.setUsernameFrom(results.getString("username_from"));
        transfer.setUsernameTo(results.getString("username_to"));

        return transfer;
    }

}
