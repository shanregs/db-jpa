package com.shan.sb.db.dbjpa.controller;

import com.shan.sb.db.dbjpa.dto.ItemDTO;
import com.shan.sb.db.dbjpa.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping
@RequiredArgsConstructor
@RestController("/api/orders/{orderId}/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getItemsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(itemService.getItemsByOrderId(orderId));
    }
}
