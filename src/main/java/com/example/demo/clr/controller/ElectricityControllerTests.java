package com.example.demo.clr.controller;

import com.example.demo.beans.ItemType;
import com.example.demo.clr.utils.ItemGeneratorUtil;
import com.example.demo.clr.utils.TablePrinter;
import com.example.demo.clr.utils.TestArtUtils;
import com.example.demo.clr.utils.TestUtils;
import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.TestException;
import com.example.demo.mappers.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Order(3)
@Component
@RequiredArgsConstructor
public class ElectricityControllerTests implements CommandLineRunner {
    private final RestTemplate restTemplate;
    private final ItemMapper itemMapper;

    private static final String BASE_URI = "http://localhost:8080/electricity";
    private static final String GET_ALL_URI = "/items";
    private static final String GET_ONE_URI = "/items/%d";
    private static final String SAVE_URI = "/items";
    private static final String UPDATE_URI = "/items/%d";
    private static final String COUNT_URI = "/count";

    HttpHeaders headers;
    ItemInputDto itemInputDto;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(TestArtUtils.ELECTRICITY_CONTROLLER_BANNER);

        itemInputDto = itemMapper.itemToItemInputDto(ItemGeneratorUtil.generateByType(ItemType.ELECTRICITY));

        prepareHeaders();

        TestUtils.printTestInfo("Electricity Controller Test - Get All");
        getAllTest();

        TestUtils.printTestInfo("Electricity Controller Test - Save New Item");
        Long newItemId = saveTest();

        TestUtils.printTestInfo("Electricity Controller Test - Save Wrong Item");
        saveWrongItemTest();

        TestUtils.printTestInfo("Electricity Controller Test - Get One");
        getOneTest(newItemId);

        TestUtils.printTestInfo("Electricity Controller Test - Get Wrong Item");
        getWrongItemTest(newItemId);

        TestUtils.printTestInfo("Electricity Controller Test - Update Item");
        updateTest(newItemId);

        TestUtils.printTestInfo("Electricity Controller Test - Update Wrong Item");
        updateWrongItemTest(newItemId);

        TestUtils.printTestInfo("Electricity Controller Test - Count");
        countTest();
    }

    private void prepareHeaders() {
        headers = new HttpHeaders();
        String plainCredentials = "electricity:electricity123";
        String encodedCredentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
        headers.set("Authorization", "Basic " + encodedCredentials);
    }

    private void getAllTest() throws TestException {
        ResponseEntity<List<ItemOutputDto>> response = restTemplate.exchange(BASE_URI + GET_ALL_URI, HttpMethod.GET,
                new HttpEntity<>(headers), new ParameterizedTypeReference<List<ItemOutputDto>>() {
                });
        if (response.getStatusCode().isError() || response.getBody() == null)
            throw new TestException("Failed electricity controller get all test");
        TablePrinter.print(response.getBody());
    }

    private Long saveTest() throws TestException {
        ResponseEntity<ItemOutputDto> response = restTemplate.exchange(BASE_URI + SAVE_URI, HttpMethod.POST,
                new HttpEntity<>(itemInputDto, headers), ItemOutputDto.class);
        if (response.getStatusCode().isError() || response.getBody() == null)
            throw new TestException("Failed electricity controller save test");
        TablePrinter.print(response.getBody());
        return response.getBody().getId();
    }

    private void saveWrongItemTest() throws TestException {
        ItemInputDto wrongItemInputDto = itemMapper.itemToItemInputDto(ItemGeneratorUtil.generateByType(ItemType.FOOD));
        try {
            ResponseEntity<ItemOutputDto> response = restTemplate.exchange(BASE_URI + SAVE_URI, HttpMethod.POST,
                    new HttpEntity<>(wrongItemInputDto, headers), ItemOutputDto.class);
            throw new TestException("Failed electricity controller save wrong item test");
        } catch (HttpStatusCodeException e) {
            TablePrinter.print(e.toString());
        }
    }

    private void getOneTest(Long newItemId) throws TestException {
        ResponseEntity<ItemOutputDto> response = restTemplate.exchange(String.format(BASE_URI + GET_ONE_URI, newItemId), HttpMethod.GET,
                new HttpEntity<>(headers), ItemOutputDto.class);
        if (response.getStatusCode().isError() || response.getBody() == null)
            throw new TestException("Failed electricity controller get test");
        TablePrinter.print(response.getBody());
    }

    private void getWrongItemTest(Long newItemId) throws TestException {
        try {
            ResponseEntity<ItemOutputDto> response = restTemplate.exchange(String.format(BASE_URI + GET_ONE_URI, newItemId + 1), HttpMethod.GET,
                    new HttpEntity<>(headers), ItemOutputDto.class);
            throw new TestException("Failed electricity controller get wrong item test");
        } catch (HttpStatusCodeException e) {
            TablePrinter.print(e.toString());
        }
    }

    private void updateTest(Long newItemId) throws TestException {
        itemInputDto.setPrice(BigDecimal.valueOf(666));
        ResponseEntity<Void> response = restTemplate.exchange(String.format(BASE_URI + UPDATE_URI, newItemId), HttpMethod.PUT,
                new HttpEntity<>(itemInputDto, headers), Void.class);
        if (response.getStatusCode().isError())
            throw new TestException("Failed electricity controller update test");
        TablePrinter.print("Updated successfully");
    }

    private void updateWrongItemTest(Long newItemId) throws TestException {
        itemInputDto.setPrice(BigDecimal.valueOf(777));
        itemInputDto.setItemType(ItemType.FOOD);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(String.format(BASE_URI + UPDATE_URI, newItemId), HttpMethod.PUT,
                    new HttpEntity<>(itemInputDto, headers), Void.class);
            throw new TestException("Failed electricity controller update wrong item test");
        } catch (HttpStatusCodeException e) {
            TablePrinter.print(e.toString());
        }
    }

    private void countTest() throws TestException {
        ResponseEntity<Long> response = restTemplate.exchange(BASE_URI + COUNT_URI, HttpMethod.GET,
                new HttpEntity<>(headers), Long.class);
        if (response.getStatusCode().isError() || response.getBody() == null)
            throw new TestException("Failed electricity controller count test");
        TablePrinter.print("Count: " + response.getBody());
    }
}