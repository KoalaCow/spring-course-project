package com.example.demo.repository;

import com.example.demo.beans.Item;
import com.example.demo.beans.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Long countByItemType(ItemType itemType);
    List<Item> findByItemType(ItemType itemType);
}
