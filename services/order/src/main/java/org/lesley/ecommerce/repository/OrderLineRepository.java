package org.lesley.ecommerce.repository;

import org.lesley.ecommerce.dtos.OrderLineResponse;
import org.lesley.ecommerce.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine , Integer> {
    List<OrderLine> findAllByOrderId(Integer id);

}
