package com.example.demo.services;

import com.example.demo.beans.Item;
import com.example.demo.constants.ErrorMessageConstants;
import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.mappers.ItemMapper;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements ItemService, AdminService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemOutputDto saveItem(ItemInputDto itemInputDto) {
        return itemMapper.itemToItemOutputDto(itemRepository.save(itemMapper.itemInputDtoToItem(itemInputDto)));
    }

    @Override
    public void updateItem(Long id, ItemInputDto itemInputDto) throws InvalidEntityException {
        itemRepository.findById(id).orElseThrow(() ->
                new InvalidEntityException(ErrorMessageConstants.FAILED_UPDATE_NON_EXISTENT_ID));
        Item itemToUpdate = itemMapper.itemInputDtoToItem(itemInputDto);
        itemToUpdate.setId(id);
        itemRepository.saveAndFlush(itemToUpdate);
    }

    @Override
    public long countItems() {
        return itemRepository.count();
    }

    @Override
    public ItemOutputDto getItemById(Long id) throws InvalidEntityException {
        return itemMapper.itemToItemOutputDto(itemRepository.findById(id).orElseThrow(() ->
                new InvalidEntityException(ErrorMessageConstants.ITEM_NOT_FOUND)));
    }

    @Override
    public List<ItemOutputDto> getAllItems() {
        return itemMapper.itemListToItemOutputDtoList(itemRepository.findAll());
    }

    @Override
    public void deleteItem(Long id) throws InvalidEntityException {
        itemRepository.findById(id).orElseThrow(() ->
                new InvalidEntityException(ErrorMessageConstants.FAILED_DELETE_NON_EXISTENT_ID));
        itemRepository.deleteById(id);
    }
}
