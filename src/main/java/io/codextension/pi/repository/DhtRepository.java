package io.codextension.pi.repository;

import io.codextension.pi.model.Dht;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by elie on 09.03.17.
 */
public interface DhtRepository extends CrudRepository<Dht, Long> {

    List<Dht> findByMeasuredDateBetweenOrderByMeasuredDateDesc(Date from, Date to);
}
