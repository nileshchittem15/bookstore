package com.netent.bookstore.service;

import com.netent.bookstore.constants.ErrorMessages;
import com.netent.bookstore.exception.BookStoreException;
import com.netent.bookstore.model.request.StockRequest;
import com.netent.bookstore.repository.BookInventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private BookInventoryRepository bookInventoryRepository;

    /**
     * updates the quantity of the book given bookId\
     * Note: quantity can be positive or negative depending on transaction. Final book stock quantity cannot be less than 0
     * @param stockRequest
     */
    public void addBookStock(StockRequest stockRequest){
        try {
            bookInventoryRepository.updateQuantity(stockRequest.getBookId(), stockRequest.getQuantity(), new Timestamp(System.currentTimeMillis()));
        }catch (DataAccessException e){
            logger.error(ErrorMessages.DB_EXCEPTION, e);
            throw new BookStoreException(ErrorMessages.DB_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Refills the stock of book by 1 asynchronously
     * @param bookId
     */
    @Async
    public void refillBookStock(String bookId){
        addBookStock(new StockRequest(bookId,1));
    }
}
