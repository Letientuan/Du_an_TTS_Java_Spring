package com.example.Du_An_TTS_Test.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
   public Cloudinary cloudinary (){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "doshfjjxp",
                "api_key", "652485648818796",
                "api_secret", "y9VUyAzDndJxhC5TbB9GF_07YhI"
        ));
    }
}
