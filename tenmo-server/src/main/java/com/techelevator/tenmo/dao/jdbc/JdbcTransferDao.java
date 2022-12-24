package com.techelevator.tenmo.dao.jdbc;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.models.Avatar;
import com.techelevator.tenmo.models.Color;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
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
    public Transfer makeOrRequestTransfer(Transfer transfer, int id)
    {
        // if transfer is not valid, set transferStatusId = 3 (rejected) and return the transfer.
        // Transaction will not be added to the transfer table
        if (!isTransferValid(transfer, id)) {
            transfer.setTransferStatusId(3);
            return transfer;
        }

        // finish building the transfer object
        buildTransferObject(transfer, id);

        // update transfer table in DB
        runSqlToUpdateTransferTable(transfer);

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

        // Note: this SQL query joins all the tables in our tenmo database:
        // transfer_type, transfer_status, transfer, account, tenmo_user, avatar, and avatar_color.
        // There are *two* each of the tenmo_user, account, avatar, and avatar_color tables, each set representing a transfer account_from and account_to
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
                ", t.date_time " +
                ", t.message " +
                ", av.avatar_id AS avatar_id_from " +
                ", av.avatar_desc AS avatar_desc_from " +
                ", av.avatar_line_1 AS avatar_line_1_from " +
                ", av.avatar_line_2 AS avatar_line_2_from " +
                ", av.avatar_line_3 AS avatar_line_3_from " +
                ", av.avatar_line_4 AS avatar_line_4_from " +
                ", av.avatar_line_5 AS avatar_line_5_from " +
                ", av2.avatar_id AS avatar_id_to " +
                ", av2.avatar_desc AS avatar_desc_to " +
                ", av2.avatar_line_1 AS avatar_line_1_to " +
                ", av2.avatar_line_2 AS avatar_line_2_to " +
                ", av2.avatar_line_3 AS avatar_line_3_to " +
                ", av2.avatar_line_4 AS avatar_line_4_to " +
                ", av2.avatar_line_5 AS avatar_line_5_to " +
                ", ac.avatar_color_id AS avatar_color_id_from " +
                ", ac.avatar_color_desc AS avatar_color_desc_from " +
                ", ac2.avatar_color_id AS avatar_color_id_to " +
                ", ac2.avatar_color_desc AS avatar_color_desc_to " +
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
                "JOIN avatar as av " +
                "ON tu.avatar_id = av.avatar_id " +
                "JOIN avatar as av2 " +
                "ON tu2.avatar_id = av2.avatar_id " +
                "JOIN avatar_color as ac " +
                "ON tu.avatar_color_id = ac.avatar_color_id " +
                "JOIN avatar_color as ac2 " +
                "ON tu2.avatar_color_id = ac2.avatar_color_id " +
                "WHERE a.user_id = ? OR a2.user_id = ?;"; // gets all transactions in which the logged-in user was either the Sender or Receiver of the $$
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }

        return transfers;
    }

    // helper function
    private boolean isTransferValid(Transfer transfer, int id) {
        if (transfer.getTransferTypeId() == 1) { // 1 is for requesting
            // check if:
            // userIdFrom != userIdTo, -- should this validation be done on client side?
            // amount > 0, -- should this validation be done on client side?
            // userIdFrom is valid
            return (id != transfer.getUserFrom().getId()) &&
                    (transfer.getAmount().compareTo(BigDecimal.ZERO) > 0) &&
                    (isUserIdValid(transfer.getUserFrom().getId()));
        } else {
            // check if:
            // userIdFrom != userIdTo,  -- should this validation be done on client side?
            // amount > 0,  -- should this validation be done on client side?
            // amount <= amount in userIdFrom's account,
            // userIdTo is valid
            return (id != transfer.getUserTo().getId()) &&
                    (transfer.getAmount().compareTo(BigDecimal.ZERO) > 0) &&
                    (accountDao.getBalance(id).compareTo(transfer.getAmount()) >= 0) &&
                    (isUserIdValid(transfer.getUserTo().getId()));
        }

    }

    // helper function to determine whether there is a matching accountId for userIdTo
    private boolean isUserIdValid(int userId) {
        return accountDao.getAccountId(userId) != null;
    }

    // helper function to finish building the Transfer object, since
    // Transfer object from the client only contains userTo/userFrom, amount, and transferType
    private void buildTransferObject(Transfer transfer, int id) {
        Integer accountTo;
        Integer accountFrom;
        User user = new User();
        user.setId(id);

        if (transfer.getTransferTypeId() == 1) { // if the transfer is a Request
            accountTo = accountDao.getAccountId(id);
            accountFrom = accountDao.getAccountId(transfer.getUserFrom().getId());
            transfer.setTransferStatusId(1); // 1 is for Pending

            transfer.setUserTo(user);

        } else { // if the transfer is a Send
            accountFrom = accountDao.getAccountId(id);
            accountTo = accountDao.getAccountId(transfer.getUserTo().getId());
            transfer.setTransferStatusId(2); // 2 is for Approved

            transfer.setUserFrom(user);

        }
        transfer.setAccountFrom(accountFrom);
        transfer.setAccountTo(accountTo);

    }

    // helper function to run SQL queries to update Transfer table in DB
    private void runSqlToUpdateTransferTable(Transfer transfer) {
        String sql;

        // if transfer is a Request
        if (transfer.getTransferTypeId() == 1) {
            sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount, date_time, message) " +
                    "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP(0), ?);"; // set date_time to current date and time;

            jdbcTemplate.update(sql,
                    transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount(), transfer.getTransferMessage());
        }

        // otherwise (i.e., if transfer is a Send)
        else {
            sql = "BEGIN TRANSACTION; " +
                    "UPDATE account SET balance = balance - ? WHERE account_id = ?; " +
                    "UPDATE account SET balance = balance + ? WHERE account_id = ?; " +
                    "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount, date_time, message) " +
                    "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP(0), ?);" + // set date_time to current date and time
                    "COMMIT;";

            jdbcTemplate.update(sql,
                    transfer.getAmount(), transfer.getAccountFrom(),
                    transfer.getAmount(), transfer.getAccountTo(),
                    transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount(), transfer.getTransferMessage());
        }
    }


    // helper function
    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        User userFrom = new User();
        User userTo = new User();
        Avatar avatarUserFrom = new Avatar();
        Avatar avatarUserTo = new Avatar();
        Color colorUserFrom = new Color();
        Color colorUserTo = new Color();

        // set general transfer details
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_from")); // Do we need to set this?
        transfer.setAccountTo(results.getInt("account_to")); // Do we need to set this? The client shouldn't see this info anyway
        transfer.setAmount(results.getBigDecimal("amount"));
        transfer.setTransferTypeDesc(results.getString("transfer_type_desc"));
        transfer.setTransferStatusDesc(results.getString("transfer_status_desc"));
        transfer.setTransferMessage(results.getString("message"));
        transfer.setTransferDateTime(String.valueOf(results.getTimestamp("date_time").toLocalDateTime()));

        // set userFrom details, including avatar and avatar color
        userFrom.setId(results.getInt("user_id_from"));
        userFrom.setUsername(results.getString("username_from"));

        avatarUserFrom.setAvatarId(results.getInt("avatar_id_from"));
        avatarUserFrom.setAvatarDesc(results.getString("avatar_desc_from"));
        avatarUserFrom.setAvatarLine1(results.getString("avatar_line_1_from"));
        avatarUserFrom.setAvatarLine2(results.getString("avatar_line_2_from"));
        avatarUserFrom.setAvatarLine3(results.getString("avatar_line_3_from"));
        avatarUserFrom.setAvatarLine4(results.getString("avatar_line_4_from"));
        avatarUserFrom.setAvatarLine5(results.getString("avatar_line_5_from"));

        colorUserFrom.setColorId(results.getInt("avatar_color_id_from"));
        colorUserFrom.setColorDesc(results.getString("avatar_color_desc_from"));

        avatarUserFrom.setColor(colorUserFrom);
        userFrom.setAvatar(avatarUserFrom);

        // set userTo details, including avatar and avatar color
        userTo.setId(results.getInt("user_id_to"));
        userTo.setUsername(results.getString("username_to"));

        avatarUserTo.setAvatarId(results.getInt("avatar_id_to"));
        avatarUserTo.setAvatarDesc(results.getString("avatar_desc_to"));
        avatarUserTo.setAvatarLine1(results.getString("avatar_line_1_to"));
        avatarUserTo.setAvatarLine2(results.getString("avatar_line_2_to"));
        avatarUserTo.setAvatarLine3(results.getString("avatar_line_3_to"));
        avatarUserTo.setAvatarLine4(results.getString("avatar_line_4_to"));
        avatarUserTo.setAvatarLine5(results.getString("avatar_line_5_to"));

        colorUserTo.setColorId(results.getInt("avatar_color_id_to"));
        colorUserTo.setColorDesc(results.getString("avatar_color_desc_to"));

        avatarUserTo.setColor(colorUserTo);
        userTo.setAvatar(avatarUserTo);

        // set UserFrom and userTo in the transfer object
        transfer.setUserFrom(userFrom);
        transfer.setUserTo(userTo);

        return transfer;
    }

}
