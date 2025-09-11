package com.superapp.branch_service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PageParams(int page, int size, String sort, String dir) {
    public static PageParams of(Integer page, Integer size, String sort, String dir) {
        int p = page == null || page < 0 ? 0 : page;
        int s = size == null || size < 1 || size > 200 ? 20 : size; // guardrails
        String srt = (sort == null || sort.isBlank()) ? "_id" : sort;
        String d = (dir == null || dir.isBlank()) ? "asc" : dir;
        return new PageParams(p, s, srt, d);
    }

    public Pageable toPageable() {
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(direction, sort));
    }

    public String sortDescriptor() {
        return sort + "," + (dir == null ? "asc" : dir);
    }
}
