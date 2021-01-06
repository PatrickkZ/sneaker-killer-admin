package com.patrick.sneakerkilleradmin.mapper;

import com.patrick.sneakerkilleradmin.dto.SecondKillDto;
import com.patrick.sneakerkilleradmin.entity.SneakerItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface ItemMapper {
    List<SneakerItem> getAllItem();
    void addItem(SneakerItem item);
    void updateItem(SneakerItem item);

    void insertSecondKillItem(@Param("itemId") Integer itemId, @Param("price")BigDecimal price, @Param("startTime")Date startTime, @Param("endTime")Date endTime);
    void insertSku(@Param("itemId") Integer itemId, @Param("size") String size, @Param("stock") Integer stock);
    int countSecondKillItemById(Integer id);
}
