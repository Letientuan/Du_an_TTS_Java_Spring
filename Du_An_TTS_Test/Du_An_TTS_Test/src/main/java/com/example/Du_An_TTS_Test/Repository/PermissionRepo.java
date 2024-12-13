package com.example.Du_An_TTS_Test.Repository;

import com.example.Du_An_TTS_Test.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepo extends JpaRepository<Permission,String> {


}
