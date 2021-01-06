package com.patrick.sneakerkilleradmin.controller;

import com.patrick.sneakerkilleradmin.dto.SecondKillDto;
import com.patrick.sneakerkilleradmin.entity.SneakerItem;
import com.patrick.sneakerkilleradmin.result.Result;
import com.patrick.sneakerkilleradmin.result.ResultFactory;
import com.patrick.sneakerkilleradmin.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin")
public class ItemController {
    ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping(value = "/allitem")
    public Result getItemInfo(){
        return ResultFactory.buildSuccessResult(itemService.getAllItemInfo());
    }

    @PostMapping(value = "/item/add")
    public Result addItem(@RequestBody SneakerItem item){
        itemService.addItem(item);
        return ResultFactory.buildSuccessResult(null);
    }

    @PostMapping(value = "/item/update")
    public Result updateItem(@RequestBody SneakerItem item){
        itemService.updateItem(item);
        return ResultFactory.buildSuccessResult(null);
    }

    @GetMapping(value = "/second-kill/items")
    public Result getAllItem(){
        return ResultFactory.buildSuccessResult(itemService.getAllItemInfo());
    }

    @PostMapping(value = "/second-kill/onsale")
    public Result onSaleItem(@RequestBody SecondKillDto dto){
        if(itemService.onSaleItem(dto)){
            return ResultFactory.buildSuccessResult(null);
        }
        return ResultFactory.buildFailResult("该商品已上架或输入库存与尺码个数不匹配");
    }
}
