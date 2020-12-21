package com.example.demo.clr.services;

import com.example.demo.beans.ItemType;
import com.example.demo.clr.utils.TestArtUtils;
import com.example.demo.clr.utils.ItemGeneratorUtil;
import com.example.demo.clr.utils.TablePrinter;
import com.example.demo.clr.utils.TestUtils;
import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.exceptions.InvalidOperationException;
import com.example.demo.mappers.ItemMapper;
import com.example.demo.services.ElectricityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Order(6)
@Component
@RequiredArgsConstructor
public class ElectricityServiceTesting implements CommandLineRunner {

    private final ElectricityServiceImpl electricityService;
    private final ItemMapper itemMapper;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(TestArtUtils.ELECTRICITY_SERVICE_BANNER);

        TestUtils.printTestInfo("Electricity Service Test - Get All");
        List<ItemOutputDto> allItems = electricityService.getAllItems();
        TablePrinter.print(allItems);

        TestUtils.printTestInfo("Electricity Service Test - Count");
        long count = electricityService.countItems();
        TablePrinter.print("Current number of electricity items: " + count);

        ItemInputDto itemInputDto = itemMapper.itemToItemInputDto(ItemGeneratorUtil.generateByType(ItemType.ELECTRICITY));

        TestUtils.printTestInfo("Electricity Service Test - Save Item");
        ItemOutputDto createdItemOutputDto = electricityService.saveItem(itemInputDto);
        TablePrinter.print(createdItemOutputDto);
        count = electricityService.countItems();
        System.out.println("Current number of electricity items: " + count);

        TestUtils.printTestInfo("Electricity Service Test - Save Wrong Type of Item");
        ItemInputDto wrongItemInputDto = itemMapper.itemToItemInputDto(ItemGeneratorUtil.generateByType(ItemType.FOOD));
        try {
            electricityService.saveItem(wrongItemInputDto);
        } catch (InvalidOperationException e) {
            TablePrinter.print(e.getMessage());
        }

        TestUtils.printTestInfo("Electricity Service Test - Get One Item");
        ItemOutputDto fetchedItemOutputDto = electricityService.getItemById(createdItemOutputDto.getId());
        TablePrinter.print(fetchedItemOutputDto);

        TestUtils.printTestInfo("Electricity Service Test - Get Wrong Item");
        try {
            electricityService.getItemById(createdItemOutputDto.getId() + 1);
        } catch (InvalidEntityException e) {
            TablePrinter.print(e.getMessage());
        }

        TestUtils.printTestInfo("Electricity Service Test - Update Item");
        ItemInputDto updatedItemInputDto = itemMapper.itemOutputDtoToItemInputDto(fetchedItemOutputDto);
        updatedItemInputDto.setPrice(BigDecimal.valueOf(666));
        electricityService.updateItem(fetchedItemOutputDto.getId(), updatedItemInputDto);
        fetchedItemOutputDto = electricityService.getItemById(fetchedItemOutputDto.getId());
        TablePrinter.print(fetchedItemOutputDto);

        TestUtils.printTestInfo("Electricity Service Test - Update Wrong Item");
        try {
            electricityService.updateItem(createdItemOutputDto.getId() + 1, updatedItemInputDto);
        } catch (InvalidEntityException e) {
            TablePrinter.print(e.getMessage());
        }
    }
}
