package io.codextension.pi.repository;

import io.codextension.pi.model.Dust;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by elie on 16.04.17.
 */
@Repository
public interface DustSensorRepository extends CrudRepository<Dust, String> {
    List<Dust> findByMeasuredDateBetweenOrderByMeasuredDateDesc(Date from, Date to);

}
