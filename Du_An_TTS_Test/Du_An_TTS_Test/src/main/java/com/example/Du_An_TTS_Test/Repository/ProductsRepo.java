package com.example.Du_An_TTS_Test.Repository;

import com.example.Du_An_TTS_Test.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepo extends JpaRepository<Product,Integer> {
 Product findByName(String Name);

}
