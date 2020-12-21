package com.example.demo.web.controllers;

import com.example.demo.constants.ErrorMessageConstants;
import com.example.demo.dto.ItemInputDto;
import com.example.demo.dto.ItemOutputDto;
import com.example.demo.exceptions.InvalidEntityException;
import com.example.demo.services.AdminServiceImpl;
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
public class AdminControllerUnitTests {

    private final String ADMIN_CONTROLLER = "admin";

    private MockMvc mockMvc;

    @Mock
    private AdminServiceImpl adminService;

    @InjectMocks
    private AdminController adminController;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController)
                .setControllerAdvice(new RestControllerAdvice())
                .build();
    }

    @Test
    public void getItemSuccessTest() throws Exception {
        ItemOutputDto expected = TestUtils.getItemOutputDto(TestUtils.ITEM_TYPE);
        when(adminService.getItemById(anyLong())).thenReturn(expected);

        MvcResult mvcResult = this.mockMvc.perform(
                get(String.format("/%s/%s/%s", ADMIN_CONTROLLER, "items", expected.getId())))
                .andExpect(status().isOk())
                .andReturn();
        ItemOutputDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ItemOutputDto.class);

        assertEquals(expected, actual);
    }

    @Test
    public void getItemFailureTest() throws Exception {
        ErrorDetails expected = new ErrorDetails(ErrorMessageConstants.ITEM_NOT_FOUND);
        InvalidEntityException invalidEntityException = new InvalidEntityException(ErrorMessageConstants.ITEM_NOT_FOUND);
        doThrow(invalidEntityException).when(adminService).getItemById(anyLong());

        MvcResult mvcResult = this.mockMvc.perform(
                get(String.format("/%s/%s/%s", ADMIN_CONTROLLER, "items", TestUtils.ID)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorDetails actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDetails.class);
        assertEquals(expected, actual);
    }

    @Test
    public void getAllItemsSuccessTest() throws Exception {
        List<ItemOutputDto> expected = TestUtils.getItemOutputDtoList(TestUtils.ITEM_TYPE);
        when(adminService.getAllItems()).thenReturn(expected);

        MvcResult mvcResult = this.mockMvc.perform(
                get(String.format("/%s/%s", ADMIN_CONTROLLER, "items")))
                .andExpect(status().isOk())
                .andReturn();
        List<ItemOutputDto> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(expected, actual);
    }

    @Test
    public void countSuccessTest() throws Exception {
        long expected = 3L;
        when(adminService.countItems()).thenReturn(expected);

        MvcResult mvcResult = this.mockMvc.perform(
                get(String.format("/%s/%s", ADMIN_CONTROLLER, "count")))
                .andExpect(status().isOk())
                .andReturn();
        Long actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), Long.class);

        assertEquals(expected, actual);
    }

    @Test
    public void saveItemSuccessTest() throws Exception {
        ItemOutputDto expected = TestUtils.getItemOutputDto(TestUtils.ITEM_TYPE);
        when(adminService.saveItem(any())).thenReturn(expected);

        ItemInputDto requestBody = TestUtils.getItemInputDto(TestUtils.ITEM_TYPE);

        MvcResult mvcResult = this.mockMvc.perform(
                post(String.format("/%s/%s", ADMIN_CONTROLLER, "items"))
                        .content(mapper.writeValueAsString(requestBody))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        ItemOutputDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ItemOutputDto.class);

        assertEquals(expected, actual);
    }

    @Test
    public void updateItemSuccessTest() throws Exception {
        doNothing().when(adminService).updateItem(anyLong(), any());

        ItemInputDto requestBody = TestUtils.getItemInputDto(TestUtils.ITEM_TYPE);

        this.mockMvc.perform(
                put(String.format("/%s/%s/%s", ADMIN_CONTROLLER, "items", TestUtils.ID))
                        .content(mapper.writeValueAsString(requestBody))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void updateItemFailureTest() throws Exception {
        ItemInputDto requestBody = TestUtils.getItemInputDto(TestUtils.ITEM_TYPE);
        ErrorDetails expected = new ErrorDetails(ErrorMessageConstants.FAILED_UPDATE_NON_EXISTENT_ID);
        InvalidEntityException invalidEntityException = new InvalidEntityException(ErrorMessageConstants.FAILED_UPDATE_NON_EXISTENT_ID);
        doThrow(invalidEntityException).when(adminService).updateItem(anyLong(), any());

        MvcResult mvcResult = this.mockMvc.perform(
                put(String.format("/%s/%s/%s", ADMIN_CONTROLLER, "items", TestUtils.ID))
                        .content(mapper.writeValueAsString(requestBody))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorDetails actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDetails.class);
        assertEquals(expected, actual);
    }

    @Test
    public void deleteItemSuccessTest() throws Exception {
        doNothing().when(adminService).deleteItem(anyLong());

        this.mockMvc.perform(
                delete(String.format("/%s/%s/%s", ADMIN_CONTROLLER, "items", TestUtils.ID)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deleteItemFailureTest() throws Exception {
        ErrorDetails expected = new ErrorDetails(ErrorMessageConstants.FAILED_DELETE_NON_EXISTENT_ID);
        InvalidEntityException invalidEntityException = new InvalidEntityException(ErrorMessageConstants.FAILED_DELETE_NON_EXISTENT_ID);
        doThrow(invalidEntityException).when(adminService).deleteItem(anyLong());

        MvcResult mvcResult = this.mockMvc.perform(
                delete(String.format("/%s/%s/%s", ADMIN_CONTROLLER, "items", TestUtils.ID))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorDetails actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDetails.class);
        assertEquals(expected, actual);
    }
}
