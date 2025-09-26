package com.estore.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserServiceFeignClient {
    @GetMapping("/api/users")
    Object getAllUsers();

    @GetMapping("/api/users/{id}")
    Object getUserById(@PathVariable("id") Long id);

    @PostMapping("/api/users")
    Object createUser(@RequestBody Object userDto);

    @PutMapping("/api/users/{id}")
    Object updateUser(@PathVariable("id") Long id, @RequestBody Object userDto);

    @DeleteMapping("/api/users/{id}")
    Object deleteUser(@PathVariable("id") Long id);
}
