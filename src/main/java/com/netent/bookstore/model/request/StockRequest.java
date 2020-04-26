package com.netent.bookstore.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class StockRequest {

    @NotEmpty
    private String bookId;

    @Min(1)
    @NotNull
    private int quantity;
}
