package com.example.demo.services;

import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.exceptions.InvalidOperationException;

import java.util.List;

public interface ItemService {
    ItemOutputDto saveItem(ItemInputDto itemInputDto) throws InvalidOperationException;
    void updateItem(Long id, ItemInputDto itemInputDto) throws InvalidEntityException, InvalidOperationException;
    long countItems();
    ItemOutputDto getItemById(Long id) throws InvalidEntityException, InvalidOperationException;
    List<ItemOutputDto> getAllItems();
}
