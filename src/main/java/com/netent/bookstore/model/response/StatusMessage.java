package com.netent.bookstore.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.netent.bookstore.constants.ResponseMessages;
import com.netent.bookstore.exception.BookStoreException;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusMessage {

    private String status;
    private String errorMessage;


    public StatusMessage(BookStoreException e){
        this.status = ResponseMessages.FAILURE;
        this.errorMessage = e.getErrorMessage();
    }

    public StatusMessage(){
        this.status = ResponseMessages.SUCCESS;
    }

    public StatusMessage(String errorMessage){
        this.status = ResponseMessages.FAILURE;
        this.errorMessage = errorMessage;
    }

}
