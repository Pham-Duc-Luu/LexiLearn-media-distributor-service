package com.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QueryPaginationResult<T> {

    @NotNull
    private Integer total;
    @NotNull

    private Integer limit;
    @NotNull

    private Integer skip;
    private T metadata;

    public QueryPaginationResult() {
    }

    public QueryPaginationResult(Integer total, Integer limit, Integer skip, T metadata) {
        this.total = total;
        this.limit = limit;
        this.skip = skip;
        this.metadata = metadata;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public T getMetadata() {
        return metadata;
    }

    public void setMetadata(T metadata) {
        this.metadata = metadata;
    }
}
