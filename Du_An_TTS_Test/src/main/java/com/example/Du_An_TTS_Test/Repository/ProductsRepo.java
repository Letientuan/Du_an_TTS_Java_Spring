package com.example.Du_An_TTS_Test.Repository;

import com.example.Du_An_TTS_Test.Entity.Products;
import com.example.Du_An_TTS_Test.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepo extends JpaRepository<Products,Integer> {
}
