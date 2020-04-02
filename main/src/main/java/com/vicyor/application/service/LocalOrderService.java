package com.vicyor.application.service;

import com.vicyor.application.dto.SKUOrderDTO;

import java.util.List;

public interface LocalOrderService {
    void verifySKUOrderDTOLists(List<SKUOrderDTO> dtos) throws Exception;

    void executeCreateOrderTask(List<SKUOrderDTO> dtos) throws Exception;
}
