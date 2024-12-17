package com.shopify.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateProductDto {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
    private String name;

    @Min(value = 1, message = "Price must be greater than 0")
    private double price;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;

}
