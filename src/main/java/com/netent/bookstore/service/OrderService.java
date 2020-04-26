package com.netent.bookstore.service;

import com.netent.bookstore.constants.ErrorMessages;
import com.netent.bookstore.constants.ResponseMessages;
import com.netent.bookstore.exception.BookStoreException;
import com.netent.bookstore.model.BookInventory;
import com.netent.bookstore.model.BookOrder;
import com.netent.bookstore.repository.BookInventoryRepository;
import com.netent.bookstore.repository.BookOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private BookInventoryRepository bookInventoryRepository;

    @Autowired
    private BookOrderRepository bookOrderRepository;

    @Autowired
    private InventoryService inventoryService;

    /**
     * Adds an book order given book id and quantity. Updates the inventory according to the order quantity
     * Note:
     * 1. order is successful only if given quantity is less than the quantity present in inventory
     * 2. After the order if quantity becomes 0, it gets refilled asynchronously
     * @param bookOrder
     * @return
     */
    public BookOrder orderBook(BookOrder bookOrder){

        try{
            BookInventory bookInventory = bookInventoryRepository.findBookInventoryByBookId(bookOrder.getBookId());

            if(bookInventory==null){
                logger.error(ErrorMessages.NO_BOOK_FOUND_WITH_ID_EXCEPTION + bookOrder.getBookId());
                throw new BookStoreException(ErrorMessages.NO_BOOK_FOUND_WITH_ID_EXCEPTION +bookOrder.getBookId(), HttpStatus.BAD_REQUEST);
            }

            bookOrder.setPrice(bookInventory.getPrice());
            bookOrder.setTotalPrice(bookInventory.getPrice()* bookOrder.getQuantity());
            bookOrder.setStatus(ResponseMessages.FAILURE);

            if(bookInventory.getQuantity()>0 && bookInventory.getQuantity()>=bookOrder.getQuantity()){
                bookInventoryRepository.updateQuantity(bookOrder.getBookId(), -1*bookOrder.getQuantity(), new Timestamp(System.currentTimeMillis()));
                bookOrder.setStatus(ResponseMessages.SUCCESS);

                if(bookInventory.getQuantity()==bookOrder.getQuantity()){
                    inventoryService.refillBookStock(bookOrder.getBookId());
                }

            }else{
                logger.error(ErrorMessages.INSUFFICIENT_BOOK_QUNATITY_EXCEPTION);
                throw new BookStoreException(ErrorMessages.INSUFFICIENT_BOOK_QUNATITY_EXCEPTION,
                    HttpStatus.BAD_REQUEST);
            }
        }catch (DataIntegrityViolationException e){
            logger.error(ErrorMessages.INSUFFICIENT_BOOK_QUNATITY_EXCEPTION, e);
            throw new BookStoreException(ErrorMessages.INSUFFICIENT_BOOK_QUNATITY_EXCEPTION,
                HttpStatus.CONFLICT);
        }catch (DataAccessException e){
            logger.error(ErrorMessages.DB_EXCEPTION, e);
            throw new BookStoreException(ErrorMessages.DB_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            if(!StringUtils.isEmpty(bookOrder.getStatus())){
                bookOrderRepository.save(bookOrder);
            }
        }

        return bookOrder;

    }
}
