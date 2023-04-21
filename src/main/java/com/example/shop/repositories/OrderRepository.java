package com.example.shop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.shop.models.Order;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    List<Order> findByPersonId(int personId);

    @Query(value = "select * from orders where (lower(right(number, 4)) like '%?1') or (lower(right(number, 4)) like %?1%) or (lower(right(number, 4)) like '?1%')", nativeQuery = true)
    List<Order> findByNumberLastPartLikeString(String query);
}
