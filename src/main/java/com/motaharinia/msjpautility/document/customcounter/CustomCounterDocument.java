package com.motaharinia.msjpautility.document.customcounter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;


/**
 *  کلاس داکیومنت شمارنده
 */
@Document(collection = "custom_counter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomCounterDocument {
    @Id
    private String  id;

    /**
     * شمارنده
     */
    private Long primaryKey;
}
