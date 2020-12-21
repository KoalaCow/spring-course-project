package com.example.demo.clr.bootstrap;

import com.example.demo.beans.Item;
import com.example.demo.clr.utils.ItemGeneratorUtil;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Order(1)
@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {
    private final ItemRepository itemRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Item> itemList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            itemList.add(ItemGeneratorUtil.generate());
        }
        itemRepository.saveAll(itemList);
    }

}
