package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yearup.models.OrderLineItem;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Integer>
{
}//now spring knows what sql to write.
//jps reads the annotations from model classes and method names from
//repository interfaces, then generates sql during runtime