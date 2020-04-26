package com.netent.bookstore.controllers;


import com.netent.bookstore.constants.URLConstants;
import com.netent.bookstore.exception.BookStoreException;
import com.netent.bookstore.model.Book;
import com.netent.bookstore.model.request.SearchRequest;
import com.netent.bookstore.model.response.ServiceResponse;
import com.netent.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = URLConstants.BOOK)
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ServiceResponse addBook(@Valid @RequestBody Book book){

        try{
            bookService.addBook(book);
            return new ServiceResponse(book);
        }catch (BookStoreException e){
            return ServiceResponse.getErrorResponse(e);
        }

    }

    @GetMapping
    public ServiceResponse searchBook(SearchRequest searchRequest){

        String errorMessage = searchRequest.validate();
        if(!StringUtils.isEmpty(errorMessage)){
            return ServiceResponse.getBadRequestResponse(errorMessage);
        }

        try{
            List<Book> bookList = bookService.searchBook(searchRequest);
            return new ServiceResponse(bookList);
        }catch (BookStoreException e){
            return ServiceResponse.getErrorResponse(e);
        }

    }

    @GetMapping(value = URLConstants.MEDIA_POST)
    public ServiceResponse getMediaPosts(@RequestParam String isbn){

        try{
            List<String> mediaPostTitles = bookService.getMediaPosts(isbn);
            return new ServiceResponse(mediaPostTitles);
        }catch (BookStoreException e){
            return ServiceResponse.getErrorResponse(e);
        }

    }

}
