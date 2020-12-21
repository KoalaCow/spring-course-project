package com.example.demo.clr.controller;

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

@Order(2)
@Component
@RequiredArgsConstructor
public class AdminControllerTests implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final ItemMapper itemMapper;

    private static final String BASE_URI = "http://localhost:8080/admin";
    private static final String GET_ALL_URI = "/items";
    private static final String GET_ONE_URI = "/items/%d";
    private static final String SAVE_URI = "/items";
    private static final String UPDATE_URI = "/items/%d";
    private static final String COUNT_URI = "/count";
    private static final String DELETE_URI = "/items/%d";

    HttpHeaders headers;
    ItemInputDto itemInputDto;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(TestArtUtils.ADMIN_CONTROLLER_BANNER);

        itemInputDto = itemMapper.itemToItemInputDto(ItemGeneratorUtil.generate());

        prepareHeaders();

        TestUtils.printTestInfo("Admin Controller Test - Get All");
        getAllTest();

        TestUtils.printTestInfo("Admin Controller Test - Save New Item");
        Long newItemId = saveTest();

        TestUtils.printTestInfo("Admin Controller Test - Get One");
        getOneTest(newItemId);

        TestUtils.printTestInfo("Admin Controller Test - Get Wrong Item");
        getWrongItemTest(newItemId);

        TestUtils.printTestInfo("Admin Controller Test - Update Item");
        updateTest(newItemId);

        TestUtils.printTestInfo("Admin Controller Test - Count");
        countTest();

        TestUtils.printTestInfo("Admin Controller Test - Delete");
        deleteTest(newItemId);

        TestUtils.printTestInfo("Admin Controller Test - Delete wrong item");
        deleteWrongItemTest(newItemId);
    }

    private void prepareHeaders() {
        headers = new HttpHeaders();
        String plainCredentials = "admin:admin123";
        String encodedCredentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
        headers.set("Authorization", "Basic " + encodedCredentials);
    }

    private void getAllTest() throws TestException {
        ResponseEntity<List<ItemOutputDto>> response = restTemplate.exchange(BASE_URI + GET_ALL_URI, HttpMethod.GET,
                new HttpEntity<>(headers), new ParameterizedTypeReference<List<ItemOutputDto>>() {
                });
        if (response.getStatusCode().isError() || response.getBody() == null)
            throw new TestException("Failed admin controller get all test");
        TablePrinter.print(response.getBody());
    }

    private Long saveTest() throws TestException {
        ResponseEntity<ItemOutputDto> response = restTemplate.exchange(BASE_URI + SAVE_URI, HttpMethod.POST,
                new HttpEntity<>(itemInputDto, headers), ItemOutputDto.class);
        if (response.getStatusCode().isError() || response.getBody() == null)
            throw new TestException("Failed admin controller save test");
        TablePrinter.print(response.getBody());
        return response.getBody().getId();
    }

    private void getOneTest(Long newItemId) throws TestException {
        ResponseEntity<ItemOutputDto> response = restTemplate.exchange(String.format(BASE_URI + GET_ONE_URI, newItemId), HttpMethod.GET,
                new HttpEntity<>(headers), ItemOutputDto.class);
        if (response.getStatusCode().isError() || response.getBody() == null)
            throw new TestException("Failed admin controller get test");
        TablePrinter.print(response.getBody());
    }

    private void getWrongItemTest(Long newItemId) throws TestException {
        try {
            ResponseEntity<ItemOutputDto> response = restTemplate.exchange(String.format(BASE_URI + GET_ONE_URI, newItemId + 1), HttpMethod.GET,
                    new HttpEntity<>(headers), ItemOutputDto.class);
            throw new TestException("Failed admin controller get wrong item test");
        } catch (HttpStatusCodeException e) {
            TablePrinter.print(e.toString());
        }
    }

    private void updateTest(Long newItemId) throws TestException {
        itemInputDto.setPrice(BigDecimal.valueOf(666));
        ResponseEntity<Void> response = restTemplate.exchange(String.format(BASE_URI + UPDATE_URI, newItemId), HttpMethod.PUT,
                new HttpEntity<>(itemInputDto, headers), Void.class);
        if (response.getStatusCode().isError())
            throw new TestException("Failed admin controller update test");
        TablePrinter.print("Updated successfully");
    }

    private void countTest() throws TestException {
        ResponseEntity<Long> response = restTemplate.exchange(BASE_URI + COUNT_URI, HttpMethod.GET,
                new HttpEntity<>(headers), Long.class);
        if (response.getStatusCode().isError() || response.getBody() == null)
            throw new TestException("Failed admin controller count test");
        TablePrinter.print("Count: " + response.getBody());
    }

    private void deleteTest(Long newItemId) throws TestException {
        ResponseEntity<Void> response = restTemplate.exchange(String.format(BASE_URI + DELETE_URI, newItemId), HttpMethod.DELETE,
                new HttpEntity<>(itemInputDto, headers), Void.class);
        if (response.getStatusCode().isError())
            throw new TestException("Failed admin controller delete test");
        TablePrinter.print("Delete successfully");
    }

    private void deleteWrongItemTest(Long newItemId) throws TestException {
        try {
            ResponseEntity<Void> response = restTemplate.exchange(String.format(BASE_URI + DELETE_URI, newItemId + 1), HttpMethod.DELETE,
                    new HttpEntity<>(itemInputDto, headers), Void.class);
            throw new TestException("Failed admin controller delete wrong item test");
        } catch (HttpStatusCodeException e) {
            TablePrinter.print(e.toString());
        }
    }
}
