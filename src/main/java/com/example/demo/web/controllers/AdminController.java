package com.example.demo.web.controllers;

import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.services.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminServiceImpl adminService;

    @PostMapping("items")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemOutputDto saveItem(@Valid @RequestBody ItemInputDto itemInputDto) {
        return adminService.saveItem(itemInputDto);
    }

    @PutMapping("items/{id}")
    public void updateItem(@PathVariable Long id, @Valid @RequestBody ItemInputDto itemInputDto) throws InvalidEntityException {
        adminService.updateItem(id, itemInputDto);
    }

    @GetMapping("count")
    public Long countItems() {
        return adminService.countItems();
    }

    @GetMapping("items/{id}")
    public ItemOutputDto getItemById(@PathVariable Long id) throws InvalidEntityException {
        return adminService.getItemById(id);
    }

    @GetMapping("items")
    public List<ItemOutputDto> getAllItems() {
        return adminService.getAllItems();
    }

    @DeleteMapping("items/{id}")
    public void deleteItem(@PathVariable Long id) throws InvalidEntityException {
        adminService.deleteItem(id);
    }
}
