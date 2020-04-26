package com.netent.bookstore.service;

import com.netent.bookstore.constants.ErrorMessages;
import com.netent.bookstore.exception.BookStoreException;
import com.netent.bookstore.model.Book;
import com.netent.bookstore.model.BookInventory;
import com.netent.bookstore.model.request.SearchRequest;
import com.netent.bookstore.repository.BookInventoryRepository;
import com.netent.bookstore.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookInventoryRepository bookInventoryRepository;

    @Autowired
    private MediaService mediaService;

    /**
     * Adds new book and also Adds book details in inventory with 0 quantity
     * Note: Rolls back transaction if book inventory addition fails
     * @param book
     * @return
     */
    public Book addBook(Book book){

        try{
            bookRepository.save(book);
            BookInventory bookInventory = new BookInventory(book.getId(),book.getPrice(),0);
            bookInventoryRepository.save(bookInventory);
            return book;
        }catch (DataIntegrityViolationException e){
            logger.error(ErrorMessages.DUPLICATE_BOOK_EXCEPTION+ book.getIsbn(), e);
            throw new BookStoreException(ErrorMessages.DUPLICATE_BOOK_EXCEPTION+ book.getIsbn(),
                HttpStatus.BAD_REQUEST);
        }catch (DataAccessException e){
            bookRepository.deleteBookById(book.getId());
            logger.error(ErrorMessages.DB_EXCEPTION, e);
            throw new BookStoreException(ErrorMessages.DB_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Find all books based on search request
     * @param searchRequest
     * @return
     */
    public List<Book> searchBook(SearchRequest searchRequest){

        List<Book> bookList = new ArrayList<>();

        try{
            if(!StringUtils.isEmpty(searchRequest.getIsbn())){
                Book book = bookRepository.findBookByIsbn(searchRequest.getIsbn());
                if(book!=null){
                    bookList.add(book);
                }
            }else if(!StringUtils.isEmpty(searchRequest.getTitle())){
                bookList = bookRepository.findBooksByTitleContainingIgnoreCase(searchRequest.getTitle());
            }else if(!StringUtils.isEmpty(searchRequest.getAuthor())){
                bookList = bookRepository.findBooksByAuthorContainingIgnoreCase(searchRequest.getAuthor());
            }
        }catch (DataAccessException e){
            logger.error(ErrorMessages.DB_EXCEPTION, e);
            throw new BookStoreException(ErrorMessages.DB_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return bookList;
    }

    /**
     * Finds all media post titles given book isbn
     * @param isbn
     * @return
     */
    public List<String> getMediaPosts(String isbn){

        List<String> mediaPostTitles;

        try{
            Book book = bookRepository.findBookByIsbn(isbn);
            if(book==null){
                logger.error(ErrorMessages.NO_BOOK_FOUND_WITH_ISBN_EXCEPTION + book.getIsbn());
                throw new BookStoreException(ErrorMessages.NO_BOOK_FOUND_WITH_ISBN_EXCEPTION +isbn, HttpStatus.BAD_REQUEST);
            }
            mediaPostTitles = mediaService.getMediaPostsTitles(book.getTitle());
        }catch (DataAccessException e){
            logger.error(ErrorMessages.DB_EXCEPTION, e);
            throw new BookStoreException(ErrorMessages.DB_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (BookStoreException e){
            throw e;
        }


        return mediaPostTitles;
    }
}
