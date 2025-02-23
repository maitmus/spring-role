package com.github.maitmus.springrole.entity;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PaginationWrapper<T> {
    private final List<T> contents;
    private final int page;
    private final int pageSize;
    private final int totalPages;
    private final int totalElements;

    public PaginationWrapper(Page<T> page) {
        this.contents = page.getContent();
        this.page = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getNumberOfElements();
    }
}
