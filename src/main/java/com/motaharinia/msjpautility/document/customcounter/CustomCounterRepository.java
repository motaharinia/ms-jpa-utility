package com.motaharinia.msjpautility.document.customcounter;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 *  کلاس ریپازیتوری شمارنده
 */

@Repository
public interface CustomCounterRepository extends MongoRepository<CustomCounterDocument, String> {
}
