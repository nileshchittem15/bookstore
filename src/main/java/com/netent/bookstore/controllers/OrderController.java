package com.netent.bookstore.controllers;

import com.netent.bookstore.constants.URLConstants;
import com.netent.bookstore.exception.BookStoreException;
import com.netent.bookstore.model.BookOrder;
import com.netent.bookstore.model.response.ServiceResponse;
import com.netent.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = URLConstants.ORDER)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ServiceResponse orderBook(@Valid @RequestBody BookOrder order){

        try{
            BookOrder bookOrder = orderService.orderBook(order);
            return new ServiceResponse(bookOrder);
        }catch (BookStoreException e){
            return ServiceResponse.getErrorResponse(e);
        }

    }

}
