package com.example.Du_An_TTS_Test.Dto;

import com.example.Du_An_TTS_Test.Entity.Products;

import java.util.List;

public class daoProducts {
    public String saveProduct(Products product) {
        if (product == null) {
            return "Sản phẩm không được để trống.";
        }

        String validationError = validateProduct(product);
        if (validationError != null) {
            return validationError;
        }


        return "Sản phẩm đã được lưu thành công.";
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
        if (!isValidCreatedAt(product.getCreated_at())) {
            return "Ngày tạo không hợp lệ.";
        }
        if (!isValidUpdatedAt(product.getUpdated_at())) {
            return "Ngày cập nhật không hợp lệ.";
        }
        if (!isValidCreatedBy(product.getCreated_by())) {
            return "ID người tạo không hợp lệ.";
        }
        if (!isValidView(product.getView())) {
            return "Số lượt xem không hợp lệ.";
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

    private boolean isValidCreatedAt(String created_at) {
        return created_at != null && created_at.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isValidUpdatedAt(String updated_at) {
        return updated_at == null || updated_at.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isValidCreatedBy(Integer created_by) {
        return created_by != null && created_by > 0;
    }

    private boolean isValidView(Number view) {
        return view != null && view.intValue() >= 0;
    }

    public List<Products> getAllProducts() {
        return null;
    }

    public Products getProductById(int id) {
        return null;
    }
}
