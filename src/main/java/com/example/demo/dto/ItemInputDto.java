package com.example.demo.dto;

import com.example.demo.beans.ItemType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInputDto {
    @NotBlank
    private String name;

    @NotNull
    private ItemType itemType;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
}
