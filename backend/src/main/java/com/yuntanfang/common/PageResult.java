package com.yuntanfang.common;

import java.util.List;

public record PageResult<T>(long total, long pageNo, long pageSize, List<T> records) {

    public static <T> PageResult<T> of(List<T> records) {
        return new PageResult<>(records.size(), 1, records.size(), records);
    }
}
