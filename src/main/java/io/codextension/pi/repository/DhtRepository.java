package io.codextension.pi.repository;

import io.codextension.pi.model.Dht;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by elie on 09.03.17.
 */
@Repository
public interface DhtRepository extends CrudRepository<Dht, String> {

    List<Dht> findByMeasuredDateBetweenOrderByMeasuredDateDesc(Date from, Date to);
}
