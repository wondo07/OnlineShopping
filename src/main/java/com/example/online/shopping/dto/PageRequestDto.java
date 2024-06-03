package com.example.online.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDto {

    private Object data;
    private DataInfo dataInfo;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class DataInfo{
        int page;
        int size;
        int totalPages;
        Long totalElement;
    }


    static public PageRequestDto of(List list, Page page){
        DataInfo dataInfo = new DataInfo(
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements());

        return new PageRequestDto(list,dataInfo);
    }
}
