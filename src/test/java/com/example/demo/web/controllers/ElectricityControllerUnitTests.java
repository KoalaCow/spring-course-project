package com.example.demo.web.controllers;

import com.example.demo.beans.ItemType;
import com.example.demo.constants.ErrorMessageConstants;
import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.exceptions.InvalidOperationException;
import com.example.demo.services.ElectricityServiceImpl;
import com.example.demo.utils.TestUtils;
import com.example.demo.web.advice.RestControllerAdvice;
import com.example.demo.web.model.ErrorDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ElectricityControllerUnitTests {

    private final String ELECTRICITY_CONTROLLER = "electricity";

    private MockMvc mockMvc;

    @Mock
    private ElectricityServiceImpl electricityService;

    @InjectMocks
    private ElectricityController electricityController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(electricityController)
                .setControllerAdvice(new RestControllerAdvice())
                .build();
    }

    @Test
    public void getItemSuccessTest() throws Exception {
        ItemOutputDto expected = TestUtils.getItemOutputDto(ItemType.ELECTRICITY);
        when(electricityService.getItemById(anyLong())).thenReturn(expected);

        MvcResult mvcResult = this.mockMvc.perform(
                get(String.format("/%s/%s/%s", ELECTRICITY_CONTROLLER, "items", expected.getId())))
                .andExpect(status().isOk())
                .andReturn();
        ItemOutputDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ItemOutputDto.class);

        assertEquals(expected, actual);
    }

    @Test
    public void getItemInvalidEntityTest() throws Exception {
        ErrorDetails expected = new ErrorDetails(ErrorMessageConstants.ITEM_NOT_FOUND);
        InvalidEntityException invalidEntityException = new InvalidEntityException(ErrorMessageConstants.ITEM_NOT_FOUND);
        doThrow(invalidEntityException).when(electricityService).getItemById(anyLong());

        MvcResult mvcResult = this.mockMvc.perform(
                get(String.format("/%s/%s/%s", ELECTRICITY_CONTROLLER, "items", TestUtils.ID)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorDetails actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDetails.class);
        assertEquals(expected, actual);
    }

    @Test
    public void getItemInvalidOperationTest() throws Exception {
        ErrorDetails expected = new ErrorDetails(ErrorMessageConstants.FAILED_GET_ITEM_OUTSIDE_DOMAIN);
        InvalidOperationException invalidOperationException = new InvalidOperationException(ErrorMessageConstants.FAILED_GET_ITEM_OUTSIDE_DOMAIN);
        doThrow(invalidOperationException).when(electricityService).getItemById(anyLong());

        MvcResult mvcResult = this.mockMvc.perform(
                get(String.format("/%s/%s/%s", ELECTRICITY_CONTROLLER, "items", TestUtils.ID)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorDetails actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDetails.class);
        assertEquals(expected, actual);
    }

    @Test
    public void getAllItemsSuccessTest() throws Exception {
        List<ItemOutputDto> expected = TestUtils.getItemOutputDtoList(ItemType.ELECTRICITY);
        when(electricityService.getAllItems()).thenReturn(expected);

        MvcResult mvcResult = this.mockMvc.perform(
                get(String.format("/%s/%s", ELECTRICITY_CONTROLLER, "items")))
                .andExpect(status().isOk())
                .andReturn();
        List<ItemOutputDto> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(expected, actual);
    }

    @Test
    public void countSuccessTest() throws Exception {
        long expected = 3L;
        when(electricityService.countItems()).thenReturn(expected);

        MvcResult mvcResult = this.mockMvc.perform(
                get(String.format("/%s/%s", ELECTRICITY_CONTROLLER, "count")))
                .andExpect(status().isOk())
                .andReturn();
        Long actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);

        assertEquals(expected, actual);
    }

    @Test
    public void saveItemSuccessTest() throws Exception {
        ItemOutputDto expected = TestUtils.getItemOutputDto(ItemType.ELECTRICITY);
        when(electricityService.saveItem(any())).thenReturn(expected);

        ItemInputDto requestBody = TestUtils.getItemInputDto(ItemType.ELECTRICITY);

        MvcResult mvcResult = this.mockMvc.perform(
                post(String.format("/%s/%s", ELECTRICITY_CONTROLLER, "items"))
                        .content(mapper.writeValueAsString(requestBody))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        ItemOutputDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ItemOutputDto.class);

        assertEquals(expected, actual);
    }

    @Test
    public void saveItemInvalidOperationTest() throws Exception {
        ItemInputDto requestBody = TestUtils.getItemInputDto(ItemType.FOOD);
        ErrorDetails expected = new ErrorDetails(ErrorMessageConstants.FAILED_SAVE_ITEM_OUTSIDE_DOMAIN);
        InvalidOperationException invalidOperationException = new InvalidOperationException(ErrorMessageConstants.FAILED_SAVE_ITEM_OUTSIDE_DOMAIN);
        doThrow(invalidOperationException).when(electricityService).saveItem(any());

        MvcResult mvcResult = this.mockMvc.perform(
                post(String.format("/%s/%s", ELECTRICITY_CONTROLLER, "items"))
                        .content(mapper.writeValueAsString(requestBody))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorDetails actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDetails.class);
        assertEquals(expected, actual);
    }

    @Test
    public void updateItemSuccessTest() throws Exception {
        doNothing().when(electricityService).updateItem(anyLong(), any());

        ItemInputDto requestBody = TestUtils.getItemInputDto(ItemType.ELECTRICITY);

        this.mockMvc.perform(
                put(String.format("/%s/%s/%s", ELECTRICITY_CONTROLLER, "items", TestUtils.ID))
                        .content(mapper.writeValueAsString(requestBody))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void updateItemInvalidEntityTest() throws Exception {
        ItemInputDto requestBody = TestUtils.getItemInputDto(ItemType.ELECTRICITY);
        ErrorDetails expected = new ErrorDetails(ErrorMessageConstants.FAILED_UPDATE_NON_EXISTENT_ID);
        InvalidEntityException invalidEntityException = new InvalidEntityException(ErrorMessageConstants.FAILED_UPDATE_NON_EXISTENT_ID);
        doThrow(invalidEntityException).when(electricityService).updateItem(anyLong(), any());

        MvcResult mvcResult = this.mockMvc.perform(
                put(String.format("/%s/%s/%s", ELECTRICITY_CONTROLLER, "items", TestUtils.ID))
                        .content(mapper.writeValueAsString(requestBody))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorDetails actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDetails.class);
        assertEquals(expected, actual);
    }

    @Test
    public void updateItemInvalidOperationTest() throws Exception {
        ItemInputDto requestBody = TestUtils.getItemInputDto(ItemType.FOOD);
        ErrorDetails expected = new ErrorDetails(ErrorMessageConstants.FAILED_UPDATE_ITEM_OUTSIDE_DOMAIN);
        InvalidOperationException invalidOperationException = new InvalidOperationException(ErrorMessageConstants.FAILED_UPDATE_ITEM_OUTSIDE_DOMAIN);
        doThrow(invalidOperationException).when(electricityService).updateItem(anyLong(), any());

        MvcResult mvcResult = this.mockMvc.perform(
                put(String.format("/%s/%s/%s", ELECTRICITY_CONTROLLER, "items", TestUtils.ID))
                        .content(mapper.writeValueAsString(requestBody))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorDetails actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDetails.class);
        assertEquals(expected, actual);
    }
}
