package com.example.Du_An_TTS_Test.Dto;

import com.example.Du_An_TTS_Test.Entity.Products;

import java.util.List;

public class daoProducts {
    public Boolean saveProduct(Products product) {
        if (product == null) {
            return false;
        }

        String validationError = validateProduct(product);
        if (validationError != null) {
            return false;
        }


        return true;
    }

    private String validateProduct(Products product) {
        if (!isValidName(product.getName())) {
            return "Tên sản phẩm không hợp lệ.";
        }
        if (!isValidPrice(product.getPrice())) {
            return "Giá sản phẩm không hợp lệ.";
        }
        if (!isValidStockQuantity(product.getStock_quantity())) {
            return "Số lượng sản phẩm không hợp lệ.";
        }


        return null;
    }

    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 255;
    }

    private boolean isValidPrice(Double price) {
        return price != null && price > 0;
    }

    private boolean isValidStockQuantity(Integer stock_quantity) {
        return stock_quantity != null && stock_quantity >= 0;
    }


}
