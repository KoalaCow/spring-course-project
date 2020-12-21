package com.example.demo.web.controllers;

import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.exceptions.InvalidOperationException;
import com.example.demo.services.SportsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sports")
@RequiredArgsConstructor
public class SportsController {
    private final SportsServiceImpl sportsService;

    @PostMapping("items")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemOutputDto saveItem(@RequestBody ItemInputDto itemInputDto) throws InvalidOperationException {
        return sportsService.saveItem(itemInputDto);
    }

    @PutMapping("items/{id}")
    public void updateItem(@PathVariable Long id, @RequestBody ItemInputDto itemInputDto) throws InvalidEntityException, InvalidOperationException {
        sportsService.updateItem(id, itemInputDto);
    }

    @GetMapping("count")
    public Long countItems() {
        return sportsService.countItems();
    }

    @GetMapping("items/{id}")
    public ItemOutputDto getItemById(@PathVariable Long id) throws InvalidEntityException, InvalidOperationException {
        return sportsService.getItemById(id);
    }

    @GetMapping("items")
    public List<ItemOutputDto> getAllItems() {
        return sportsService.getAllItems();
    }
}
