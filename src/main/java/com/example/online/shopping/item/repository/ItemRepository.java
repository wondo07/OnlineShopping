package com.example.online.shopping.item.repository;

import com.example.online.shopping.item.entity.Item;
import com.example.online.shopping.item.repository.queryDsl.ItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    Item findByName(String name);
}
