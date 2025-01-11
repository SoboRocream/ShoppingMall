package com.shoppingmall.cashshop.repository;

import com.shoppingmall.cashshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// import java.util.List;

@Repository //코드 명확성 향상
public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

//    List<Item> findByItemName(String itemName);
//
//    //@Query를 이용한 조회
//    //@Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
//    //List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
//
//    //@NativeQuery를 이용한 조회
//    @Query(value = "SELECT * FROM item i WHERE i.item_detail LIKE %:itemDetail% ORDER BY i.price DESC", nativeQuery = true)
//    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
