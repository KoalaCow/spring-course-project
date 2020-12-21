package com.example.demo.services;

import com.example.demo.beans.Item;
import com.example.demo.beans.ItemType;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.mappers.ItemMapper;
import com.example.demo.repository.ItemRepository;
import com.example.demo.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceUnitTests {
    @Mock
    ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    public void saveItemTest() {
        Item item = TestUtils.getItem(TestUtils.ITEM_TYPE);
        ItemOutputDto expected = TestUtils.getItemOutputDto(TestUtils.ITEM_TYPE);

        when(itemMapper.itemInputDtoToItem(any())).thenReturn(item);
        when(itemRepository.save(any())).thenReturn(item);
        when(itemMapper.itemToItemOutputDto(any())).thenReturn(expected);

        ItemOutputDto actual = adminService.saveItem(TestUtils.getItemInputDto(TestUtils.ITEM_TYPE));

        assertEquals(expected, actual);
    }

    @Test
    public void updateItemTest() {
        Item item = TestUtils.getItem(TestUtils.ITEM_TYPE);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemMapper.itemInputDtoToItem(any())).thenReturn(item);
        when(itemRepository.saveAndFlush(any())).thenReturn(item);

        assertDoesNotThrow(() ->
                adminService.updateItem(TestUtils.ID, TestUtils.getItemInputDto(TestUtils.ITEM_TYPE)));
    }

    @Test
    public void updateItemFailureTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidEntityException.class,
                () -> adminService.updateItem(TestUtils.ID,
                        TestUtils.getItemInputDto(TestUtils.ITEM_TYPE)));
    }

    @Test
    public void countItemsTest() {
        Long expected = 6L;
        when(itemRepository.count()).thenReturn(expected);

        Long actual = adminService.countItems();

        assertEquals(expected, actual);
    }

    @Test
    public void getItemByIdTest() throws InvalidEntityException {
        ItemOutputDto expected = TestUtils.getItemOutputDto(TestUtils.ITEM_TYPE);
        Item item = TestUtils.getItem(TestUtils.ITEM_TYPE);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemMapper.itemToItemOutputDto(any())).thenReturn(expected);

        ItemOutputDto actual = adminService.getItemById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void getItemByIdFailureTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidEntityException.class,
                () -> adminService.getItemById(TestUtils.ID));
    }

    @Test
    public void getAllItemsTest(){
        List<ItemOutputDto> expected = TestUtils.getItemOutputDtoList(TestUtils.ITEM_TYPE);

        when(itemRepository.findAll()).thenReturn(TestUtils.getItemList(TestUtils.ITEM_TYPE));
        when(itemMapper.itemListToItemOutputDtoList(any())).thenReturn(expected);

        List<ItemOutputDto> actual = adminService.getAllItems();

        assertEquals(expected, actual);
    }

    @Test
    public void deleteItemTest(){
        Item item = TestUtils.getItem(TestUtils.ITEM_TYPE);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).deleteById(any());

        assertDoesNotThrow(() -> adminService.deleteItem(TestUtils.ID));
    }

    @Test
    public void deleteItemFailureTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidEntityException.class,
                () -> adminService.deleteItem(TestUtils.ID));
    }
}
