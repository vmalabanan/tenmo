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
    public Transfer handleTransfer(@RequestBody Transfer transfer, Principal principal) {
        // get id of logged-in user
        int id = userDao.getIdByUsername(principal.getName());
        // handle the transfer and return the Transfer object
        return transferDao.handleTransfer(transfer, id);
    }

    @GetMapping
    public List<Transfer> getAllTransfers(Principal principal) {
        int id = userDao.getIdByUsername(principal.getName());

        return transferDao.getAllTransfers(id);
    }

    @RequestMapping("/pending")
    @GetMapping
    public List<Transfer> getPendingTransfers(Principal principal) {
        int id = userDao.getIdByUsername(principal.getName());

        return transferDao.getPendingTransfers(id);
    }
}
