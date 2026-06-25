package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Order;
import org.yearup.models.User;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("orders")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrderController
{
    private OrderService orderService;
    private UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService)
    {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)//this here in insomnia returns 201 instead of 200 cuz a new order record was
    public Order checkout(Principal principal)// in the database
    {
        // get the currently logged in username
        String userName = principal.getName();
        // find database user by username
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        // call the order service to handle checkout
        return orderService.checkout(userId);
    }
}// only one method and endpoint here making it pretty simple tbh.
//no request body here since checkout needs cart,profile,addresss is already in database linked to user
//the job here is to
//figure out who is logged in ->give them their userID to the OrderService->return whatever service gives back