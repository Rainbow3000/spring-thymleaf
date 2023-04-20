package com.store.storewithspring.repository;

import com.store.storewithspring.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {

}
