package com.netent.bookstore.model.request;

import com.netent.bookstore.constants.ErrorMessages;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class SearchRequest {

    private String isbn;

    private String title;

    private String author;


    public String validate(){
        if(StringUtils.isEmpty(this.isbn) && StringUtils.isEmpty(this.title) && StringUtils.isEmpty(this.author)){
            return ErrorMessages.INVALID_SEARCH_REQUEST;
        }
        return null;
    }
}
