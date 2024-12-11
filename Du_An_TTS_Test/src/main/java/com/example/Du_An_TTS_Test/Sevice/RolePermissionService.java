//package com.example.Du_An_TTS_Test.Sevice;
//
//import com.example.Du_An_TTS_Test.Entity.Permission;
//import com.example.Du_An_TTS_Test.Entity.Role;
//import com.example.Du_An_TTS_Test.Repository.PermissionRepo;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//public class RolePermissionService {
//
//    private PermissionRepo permissionRepo;
//
//    private final Map<String, Role> roles = new HashMap<>();
//
//    public RolePermissionService() {
//
//        loadRolesFromFile();
//    }
//
//    private void loadRolesFromFile() {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<Permission> rolePermissions = permissionRepo.findAll();
//            ObjectMapper objectMapper1 = new ObjectMapper();
//            File file = new File("roles_permissions.json");
//            Map<String, Map<String, Set<String>>> roleData = objectMapper.readValue(file, Map.class);
//
//            roleData.forEach((roleName, rolePermissions) -> {
//                Set<Permission> permissions = rolePermissions.get("permissions").stream()
//                        .map(Permission::new)
//                        .collect(Collectors.toSet());
//
//                Role role = new Role(roleName, permissions);
//                roles.put(roleName, role);
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Role getRole(String roleName) {
//        return roles.get(roleName);
//    }
//}
