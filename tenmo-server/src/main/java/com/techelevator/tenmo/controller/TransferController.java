package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.models.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Transfer createTransfer(@RequestBody Transfer transfer, Principal principal) {
        // get id of logged-in user
        int id = userDao.getIdByUsername(principal.getName());
        // handle the transfer
        // throw exception if transfer is null (i.e., when currentUser balance < transfer amount for Sends originating from current user)
        // or else return the Transfer object
        Transfer newTransfer = transferDao.createTransfer(transfer, id);
        if (newTransfer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Available balance is less than transfer amount");
        } else {
            return newTransfer;
        }
    }

    @PutMapping
    public Transfer editTransfer(@RequestBody Transfer transfer, Principal principal) {
        // get id of logged-in user
        int id = userDao.getIdByUsername(principal.getName());
        // handle the transfer
        // throw exception if transfer is null (i.e., when currentUser balance < transfer amount for Requests for $ from currentUser )
        // or else return the Transfer object
        Transfer newTransfer = transferDao.editTransfer(transfer, id);
        if (newTransfer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Available balance is less than transfer amount");
        } else {
            return newTransfer;
        }
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
