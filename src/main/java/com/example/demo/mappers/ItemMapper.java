package com.example.demo.mappers;

import com.example.demo.beans.Item;
import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item itemInputDtoToItem(ItemInputDto itemInputDto);
    ItemInputDto itemToItemInputDto(Item item);
    ItemOutputDto itemToItemOutputDto(Item item);
    List<ItemOutputDto> itemListToItemOutputDtoList(List<Item> itemList);
    ItemInputDto itemOutputDtoToItemInputDto(ItemOutputDto itemOutputDto);
}
