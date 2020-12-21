package com.example.demo.clr.services;

import com.example.demo.beans.ItemType;
import com.example.demo.clr.utils.TestArtUtils;
import com.example.demo.clr.utils.ItemGeneratorUtil;
import com.example.demo.clr.utils.TablePrinter;
import com.example.demo.clr.utils.TestUtils;
import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.mappers.ItemMapper;
import com.example.demo.services.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Order(5)
@Component
@RequiredArgsConstructor
public class AdminServiceTesting implements CommandLineRunner {

    private final AdminServiceImpl adminService;
    private final ItemMapper itemMapper;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(TestArtUtils.ADMIN_SERVICE_BANNER);

        TestUtils.printTestInfo("Admin Service Test - Get All");
        List<ItemOutputDto> allItems = adminService.getAllItems();
        TablePrinter.print(allItems);

        TestUtils.printTestInfo("Admin Service Test - Count");
        Long count = adminService.countItems();
        TablePrinter.print("Current number of items: " + count);

        ItemInputDto itemInputDto = itemMapper.itemToItemInputDto(ItemGeneratorUtil.generateByType(ItemType.SPORTS));

        TestUtils.printTestInfo("Admin Service Test - Save Item");
        ItemOutputDto createdItemOutputDto = adminService.saveItem(itemInputDto);
        TablePrinter.print(createdItemOutputDto);

        count = adminService.countItems();
        System.out.println("Current number of items: " + count);

        TestUtils.printTestInfo("Admin Service Test - Get One Item");
        ItemOutputDto fetchedItemOutputDto = adminService.getItemById(createdItemOutputDto.getId());
        TablePrinter.print(fetchedItemOutputDto);

        TestUtils.printTestInfo("Admin Service Test - Get Wrong Item");
        try {
            ItemOutputDto wrongItemInputDto = adminService.getItemById(createdItemOutputDto.getId() + 1);
        } catch (InvalidEntityException e) {
            TablePrinter.print(e.getMessage());
        }

        TestUtils.printTestInfo("Admin Service Test - Update Item");
        ItemInputDto updatedItemInputDto = itemMapper.itemOutputDtoToItemInputDto(fetchedItemOutputDto);
        updatedItemInputDto.setPrice(BigDecimal.valueOf(666));
        adminService.updateItem(fetchedItemOutputDto.getId(), updatedItemInputDto);
        fetchedItemOutputDto = adminService.getItemById(createdItemOutputDto.getId());
        TablePrinter.print(fetchedItemOutputDto);

        TestUtils.printTestInfo("Admin Service Test - Update Wrong Item");
        try {
            adminService.updateItem(createdItemOutputDto.getId() + 1, updatedItemInputDto);
        } catch (InvalidEntityException e) {
            TablePrinter.print(e.getMessage());
        }
    }
}
