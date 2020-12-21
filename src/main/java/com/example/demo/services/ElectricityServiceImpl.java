package com.example.demo.services;

import com.example.demo.beans.Item;
import com.example.demo.beans.ItemType;
import com.example.demo.constants.ErrorMessageConstants;
import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.exceptions.InvalidOperationException;
import com.example.demo.mappers.ItemMapper;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectricityServiceImpl implements ItemService, ElectricityService{
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemOutputDto saveItem(ItemInputDto itemInputDto) throws InvalidOperationException {
        if (itemInputDto.getItemType() != ItemType.ELECTRICITY)
            throw new InvalidOperationException(ErrorMessageConstants.FAILED_SAVE_ITEM_OUTSIDE_DOMAIN);
        return itemMapper.itemToItemOutputDto(itemRepository.save(itemMapper.itemInputDtoToItem(itemInputDto)));
    }

    @Override
    public void updateItem(Long id, ItemInputDto itemInputDto) throws InvalidEntityException, InvalidOperationException {
        if (itemInputDto.getItemType() != ItemType.ELECTRICITY)
            throw new InvalidOperationException(ErrorMessageConstants.FAILED_UPDATE_ITEM_OUTSIDE_DOMAIN);

        Item existingItem = itemRepository.findById(id).orElseThrow(() ->
                new InvalidEntityException(ErrorMessageConstants.FAILED_UPDATE_NON_EXISTENT_ID));
        if (existingItem.getItemType() != ItemType.ELECTRICITY)
            throw new InvalidOperationException(ErrorMessageConstants.FAILED_UPDATE_ITEM_OUTSIDE_DOMAIN);

        Item itemToUpdate = itemMapper.itemInputDtoToItem(itemInputDto);
        itemToUpdate.setId(id);
        itemRepository.saveAndFlush(itemToUpdate);
    }

    @Override
    public long countItems() {
        return itemRepository.countByItemType(ItemType.ELECTRICITY);
    }

    @Override
    public ItemOutputDto getItemById(Long id) throws InvalidEntityException, InvalidOperationException {
        Item existingItem = itemRepository.findById(id).orElseThrow(() ->
                new InvalidEntityException(ErrorMessageConstants.ITEM_NOT_FOUND));
        if (existingItem.getItemType() != ItemType.ELECTRICITY)
            throw new InvalidOperationException(ErrorMessageConstants.FAILED_GET_ITEM_OUTSIDE_DOMAIN);
        return itemMapper.itemToItemOutputDto(existingItem);
    }

    @Override
    public List<ItemOutputDto> getAllItems() {
        return itemMapper.itemListToItemOutputDtoList(itemRepository.findByItemType(ItemType.ELECTRICITY));
    }
}
