package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private TransferDao transferDao;
    private UserDao userDao;
//    private AccountDao accountDao; // should this be here or in JdbcTransferDao?

    @Autowired
    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @PostMapping
    public Transfer makeTransfer(@RequestBody Transfer transfer, Principal principal) {
        // get id of logged-in user
        int id = userDao.findIdByUsername(principal.getName());

        // make the transfer and return the Transfer object
        return transferDao.makeTransfer(transfer, id);
    }
}
