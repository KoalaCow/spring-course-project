package com.example.demo.clr.services;

import com.example.demo.beans.ItemType;
import com.example.demo.clr.utils.ItemGeneratorUtil;
import com.example.demo.clr.utils.TablePrinter;
import com.example.demo.clr.utils.TestArtUtils;
import com.example.demo.clr.utils.TestUtils;
import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.exceptions.InvalidOperationException;
import com.example.demo.mappers.ItemMapper;
import com.example.demo.services.SportsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Order(7)
@Component
@RequiredArgsConstructor
public class SportServiceTesting implements CommandLineRunner {

    private final SportsServiceImpl sportsService;
    private final ItemMapper itemMapper;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(TestArtUtils.SPORTS_SERVICE_BANNER);

        TestUtils.printTestInfo("Sports Service Test - Get All");
        List<ItemOutputDto> allItems = sportsService.getAllItems();
        TablePrinter.print(allItems);

        TestUtils.printTestInfo("Sports Service Test - Count");
        long count = sportsService.countItems();
        TablePrinter.print("Current number of sports items: " + count);

        ItemInputDto itemInputDto = itemMapper.itemToItemInputDto(ItemGeneratorUtil.generateByType(ItemType.SPORTS));

        TestUtils.printTestInfo("Sports Service Test - Save Item");
        ItemOutputDto createdItemOutputDto = sportsService.saveItem(itemInputDto);
        TablePrinter.print(createdItemOutputDto);
        count = sportsService.countItems();
        System.out.println("Current number of sports items: " + count);

        TestUtils.printTestInfo("Sports Service Test - Save Wrong Type of Item");
        ItemInputDto wrongItemInputDto = itemMapper.itemToItemInputDto(ItemGeneratorUtil.generateByType(ItemType.FOOD));
        try {
            sportsService.saveItem(wrongItemInputDto);
        } catch (InvalidOperationException e) {
            TablePrinter.print(e.getMessage());
        }

        TestUtils.printTestInfo("Sports Service Test - Get One Item");
        ItemOutputDto fetchedItemOutputDto = sportsService.getItemById(createdItemOutputDto.getId());
        TablePrinter.print(fetchedItemOutputDto);

        TestUtils.printTestInfo("Sports Service Test - Get Wrong Item");
        try {
            sportsService.getItemById(createdItemOutputDto.getId() + 1);
        } catch (InvalidEntityException e) {
            TablePrinter.print(e.getMessage());
        }

        TestUtils.printTestInfo("Sports Service Test - Update Item");
        ItemInputDto updatedItemInputDto = itemMapper.itemOutputDtoToItemInputDto(fetchedItemOutputDto);
        updatedItemInputDto.setPrice(BigDecimal.valueOf(666));
        sportsService.updateItem(fetchedItemOutputDto.getId(), updatedItemInputDto);
        fetchedItemOutputDto = sportsService.getItemById(fetchedItemOutputDto.getId());
        TablePrinter.print(fetchedItemOutputDto);

        TestUtils.printTestInfo("Sports Service Test - Update Wrong Item");
        try {
            sportsService.updateItem(createdItemOutputDto.getId() + 1, updatedItemInputDto);
        } catch (InvalidEntityException e) {
            TablePrinter.print(e.getMessage());
        }
    }
}
