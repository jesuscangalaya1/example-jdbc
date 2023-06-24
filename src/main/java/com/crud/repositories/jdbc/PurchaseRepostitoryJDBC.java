package com.crud.repositories.jdbc;

import com.crud.dtos.request.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PurchaseRepostitoryJDBC {

    private final JdbcTemplate jdbcTemplate;

    public void purchaseFlight (PurchaseRequest purchaseRequest){

    }




}
