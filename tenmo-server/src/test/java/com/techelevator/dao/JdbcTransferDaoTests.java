package com.techelevator.dao;

import com.techelevator.tenmo.dao.jdbc.JdbcAccountDao;
import com.techelevator.tenmo.dao.jdbc.JdbcTransferDao;
import com.techelevator.tenmo.models.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTransferDaoTests extends BaseDaoTests {

//    private static final Transfer TRANSFER_1 = new Transfer

    private JdbcTransferDao sut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void handleTransfer_returns_() {

    }

    @Test
    public void createTransfer_returns_correct() {

    }

    @Test
    public void getAllTransfers_returns_all_transfers() {

    }

    @Test
    public void getPendingTransfers_returns_pending_transfers() {

    }

    @Test
    public void getTransferById_returns_transfer_using_Id() {

    }
}
