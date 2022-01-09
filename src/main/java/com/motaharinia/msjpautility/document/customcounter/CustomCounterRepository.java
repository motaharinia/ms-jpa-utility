package com.motaharinia.msjpautility.document.customcounter;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *  کلاس ریپازیتوری شمارنده
 */

@Repository
public interface CustomCounterRepository extends MongoRepository<CustomCounterDocument, String> {
}
