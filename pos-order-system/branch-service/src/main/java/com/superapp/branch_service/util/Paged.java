package com.superapp.branch_service.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Paged<T> {
    private List<T> items;
    private int page; // 0-based page index from request
    private int size; // page size
    private long total; // total number of elements
    private int totalPages; // ceil(total/size)
    private boolean hasNext;
    private boolean hasPrev;
    private String sort; // e.g. "name,asc" (optional)

    public static <T> Paged<T> fromPage(Page<T> p, String sort) {
        return Paged.<T>builder()
                .items(p.getContent())
                .page(p.getNumber())
                .size(p.getSize())
                .total(p.getTotalElements())
                .totalPages(p.getTotalPages())
                .hasNext(p.hasNext())
                .hasPrev(p.hasPrevious())
                .sort(sort)
                .build();
    }
}