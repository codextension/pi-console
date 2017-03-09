package io.codextension.pi.repository;

import io.codextension.pi.model.Dht;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by elie on 09.03.17.
 */
@Repository
public interface DhtRepository extends MongoRepository<Dht, String> {

    List<Dht> findByMeasuredDateBetween(Date from, Date to);
}
