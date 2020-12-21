package com.example.demo.clr.utils;

import com.example.demo.beans.Item;
import com.example.demo.beans.ItemType;

import java.math.BigDecimal;

public class ItemGeneratorUtil {
    private static int COUNT = 1;

    public static Item generate() {
        ItemType itemType = generateItemType();

        return generateByType(itemType);
    }

    public static Item generateByType(ItemType itemType) {
        return Item.builder()
                .itemType(itemType)
                .name(String.format(itemType.toString().toLowerCase() + " item %d", COUNT++))
                .price(generatePrice())
                .build();
    }

    private static ItemType generateItemType() {
        int val = (int) (Math.random() * ItemType.values().length);
        return ItemType.values()[val];
    }

    private static BigDecimal generatePrice() {
        double price = Math.random() * 101;
        return BigDecimal.valueOf(price);
    }
}
