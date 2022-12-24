package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.models.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private TransferDao transferDao;
    private UserDao userDao;

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
        return transferDao.makeOrRequestTransfer(transfer, id);
    }

    @GetMapping
    public List<Transfer> getAllTransfers(Principal principal) {
        int id = userDao.findIdByUsername(principal.getName());

        return transferDao.getAllTransfers(id);
    }
}
