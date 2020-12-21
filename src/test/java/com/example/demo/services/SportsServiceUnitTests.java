package com.example.demo.services;

import com.example.demo.beans.Item;
import com.example.demo.beans.ItemType;
import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.exceptions.InvalidOperationException;
import com.example.demo.mappers.ItemMapper;
import com.example.demo.repository.ItemRepository;
import com.example.demo.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SportsServiceUnitTests {
    @Mock
    ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private SportsServiceImpl sportsService;

    @Test
    public void saveItemTest() throws InvalidOperationException {
        Item item = TestUtils.getItem(ItemType.SPORTS);
        ItemOutputDto expected = TestUtils.getItemOutputDto(ItemType.SPORTS);

        when(itemMapper.itemInputDtoToItem(any())).thenReturn(item);
        when(itemRepository.save(any())).thenReturn(item);
        when(itemMapper.itemToItemOutputDto(any())).thenReturn(expected);

        ItemOutputDto actual = sportsService.saveItem(TestUtils.getItemInputDto(ItemType.SPORTS));

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(value = ItemType.class,
            names = {"FOOD", "ELECTRICITY", "OTHER"})
    public void saveItemFailureTest(ItemType itemType) {
        ItemInputDto itemInputDto = TestUtils.getItemInputDto(itemType);
        assertThrows(InvalidOperationException.class, () -> sportsService.saveItem(itemInputDto));
    }

    @Test
    public void updateItemTest() {
        Item item = TestUtils.getItem(ItemType.SPORTS);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemMapper.itemInputDtoToItem(any())).thenReturn(item);
        when(itemRepository.saveAndFlush(any())).thenReturn(item);

        assertDoesNotThrow(() ->
                sportsService.updateItem(TestUtils.ID, TestUtils.getItemInputDto(ItemType.SPORTS)));
    }

    @Test
    public void updateItemInvalidEntityTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidEntityException.class,
                () -> sportsService.updateItem(TestUtils.ID,
                        TestUtils.getItemInputDto(ItemType.SPORTS)));
    }

    @ParameterizedTest
    @EnumSource(value = ItemType.class,
            names = {"FOOD", "ELECTRICITY", "OTHER"})
    public void updateItemInvalidOperationTest(ItemType itemType) {
        assertThrows(InvalidOperationException.class,
                () -> sportsService.updateItem(TestUtils.ID,
                        TestUtils.getItemInputDto(itemType)));
    }

    @Test
    public void countItemsTest() {
        Long expected = 6L;
        when(itemRepository.countByItemType(ItemType.SPORTS)).thenReturn(expected);

        Long actual = sportsService.countItems();

        assertEquals(expected, actual);
    }

    @Test
    public void getItemByIdTest() throws InvalidEntityException, InvalidOperationException {
        ItemOutputDto expected = TestUtils.getItemOutputDto(ItemType.SPORTS);
        Item item = TestUtils.getItem(ItemType.SPORTS);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemMapper.itemToItemOutputDto(any())).thenReturn(expected);

        ItemOutputDto actual = sportsService.getItemById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void getItemByIdInvalidEntityTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidEntityException.class,
                () -> sportsService.getItemById(TestUtils.ID));
    }

    @ParameterizedTest
    @EnumSource(value = ItemType.class,
            names = {"FOOD", "ELECTRICITY", "OTHER"})
    public void getItemByIdInvalidOperationTest(ItemType itemType) {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(TestUtils.getItem(itemType)));

        assertThrows(InvalidOperationException.class,
                () -> sportsService.getItemById(TestUtils.ID));
    }

    @ParameterizedTest
    @EnumSource(value = ItemType.class,
            names = {"FOOD", "ELECTRICITY", "OTHER"})
    public void updateWrongItemInvalidOperationTest(ItemType itemType) {
        Item item = TestUtils.getItem(itemType);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(InvalidOperationException.class,
                () -> sportsService.updateItem(TestUtils.ID,
                        TestUtils.getItemInputDto(ItemType.SPORTS)));
    }

    @Test
    public void getAllItemsTest(){
        List<ItemOutputDto> expected = TestUtils.getItemOutputDtoList(ItemType.SPORTS);

        when(itemRepository.findByItemType(ItemType.SPORTS))
                .thenReturn(TestUtils.getItemList(ItemType.SPORTS));
        when(itemMapper.itemListToItemOutputDtoList(any())).thenReturn(expected);

        List<ItemOutputDto> actual = sportsService.getAllItems();

        assertEquals(expected, actual);
    }
}
