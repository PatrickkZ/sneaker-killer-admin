package com.patrick.sneakerkilleradmin.service;

import com.patrick.sneakerkilleradmin.dto.SecondKillDto;
import com.patrick.sneakerkilleradmin.entity.SneakerItem;
import com.patrick.sneakerkilleradmin.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    ItemMapper itemMapper;

    @Autowired
    public ItemService(ItemMapper itemMapper){
        this.itemMapper = itemMapper;
    }

    public List<SneakerItem> getAllItemInfo(){
        return itemMapper.getAllItem();
    }

    public void addItem(SneakerItem item){
        item.setImage("default.jpeg");
        itemMapper.addItem(item);
    }

    public void updateItem(SneakerItem item){
        itemMapper.updateItem(item);
    }

    public boolean onSaleItem(SecondKillDto dto){
        Integer itemId = dto.getId();
        // 已经上架了
        if(itemMapper.countSecondKillItemById(itemId)>0){
            return false;
        }
        // 检查尺码和库存是否一一对应
        String[] sizes = dto.getSize().split(",");
        String[] stocks = dto.getStock().split(",");
        if(sizes.length != stocks.length){
            return false;
        }
        // 上架
        itemMapper.insertSecondKillItem(itemId, dto.getPrice(), dto.getStartTime(), dto.getEndTime());
        // 修改sku表
        for (int i = 0; i<sizes.length; i++){
            itemMapper.insertSku(itemId, sizes[i], Integer.parseInt(stocks[i]));
        }
        return true;
    }
}
