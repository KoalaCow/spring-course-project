package com.example.demo.utils;

import com.example.demo.beans.Item;
import com.example.demo.beans.ItemType;
import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    public static final Long ID = 1l;
    public static final Timestamp CREATED_DATE = Timestamp.valueOf("2020-12-12 10:18:00");
    public static final Timestamp LAST_MODIFIED_DATE = Timestamp.valueOf("2020-12-12 10:18:00");
    public static final String NAME = "Item Name";
    public static final ItemType ITEM_TYPE = ItemType.SPORTS;
    public static final BigDecimal PRICE = BigDecimal.valueOf(10.10);

    public static ItemOutputDto getItemOutputDto(ItemType itemType) {
        return new ItemOutputDto(
                ID,
                CREATED_DATE,
                LAST_MODIFIED_DATE,
                NAME,
                itemType,
                PRICE
        );
    }

    public static List<ItemOutputDto> getItemOutputDtoList(ItemType itemType) {
        List<ItemOutputDto> itemList = new ArrayList<>();
        itemList.add(getItemOutputDto(itemType));
        return itemList;
    }

    public static List<Item> getItemList(ItemType itemType) {
        List<Item> itemList = new ArrayList<>();
        itemList.add(getItem(itemType));
        return itemList;
    }

    public static ItemInputDto getItemInputDto(ItemType itemType) {
        return new ItemInputDto(
                NAME,
                itemType,
                PRICE
        );
    }

    public static Item getItem(ItemType itemType) {
        return new Item(
                ID,
                CREATED_DATE,
                LAST_MODIFIED_DATE,
                NAME,
                itemType,
                PRICE
        );
    }
}
