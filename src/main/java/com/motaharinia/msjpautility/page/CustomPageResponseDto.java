package com.motaharinia.msjpautility.page;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eng.motahari@gmail.com<br>
 * Response DTO for paginated requests
 */
@Data
@NoArgsConstructor
public class CustomPageResponseDto<T> implements Serializable {
    /**
     * Total pages
     * ex: 1000
     */
    private int totalPages;
    /**
     * Total rows before pagination
     * ex: 30000
     */
    private long totalElements;
    /**
     * Page rows size
     * ex: 30
     */
    private int size;
    /**
     * Page no
     * ex: 0
     */
    private int page;
    /**
     * Current Page real rows
     * ex: 25
     */
    private int numberOfElements;
    /**
     * List of current page rows
     */
    private List<T> content = new ArrayList<>();
    /**
     * Is this first page?
     */
    private boolean first;
    /**
     * Is this last page?
     */
    private boolean last;
    /**
     * Is it empty?
     */
    private boolean empty;

    public CustomPageResponseDto(Page page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.page = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.content = page.getContent();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.empty = page.isEmpty();
    }
}
