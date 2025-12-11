package com.cibertec.api_reservas_mesas.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.cibertec.api_reservas_mesas.dto.PageableResponse;

@Component
public class PageableMapper {
	public <T> PageableResponse<T> toPaginacionResponse(Page<T> page) {
        return new PageableResponse<T>(
                page.toList(),
                page.getNumber(),
                page.getSize(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst()
        );
    }
}