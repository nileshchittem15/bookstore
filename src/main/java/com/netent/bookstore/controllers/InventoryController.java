package com.netent.bookstore.controllers;

import com.netent.bookstore.constants.URLConstants;
import com.netent.bookstore.exception.BookStoreException;
import com.netent.bookstore.model.request.StockRequest;
import com.netent.bookstore.model.response.ServiceResponse;
import com.netent.bookstore.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = URLConstants.INVENTORY)
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PutMapping
    public ServiceResponse addBookStock(@Valid @RequestBody StockRequest stockRequest){

        try{
            inventoryService.addBookStock(stockRequest);
            return ServiceResponse.getSuccessResponse();
        }catch (BookStoreException e){
            return ServiceResponse.getErrorResponse(e);
        }

    }
}
