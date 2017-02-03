package com.teamtreehouse.public_data.dao;

import com.teamtreehouse.public_data.model.Country;

import java.util.List;

public interface CountryDao {
    void update(Country country);
    void save(Country country);
    void delete(Country country);
    List<Country> fetchAllCountries();
    Country findByCode(List<Country> countries, String code);
    Country minInternetUsers();
    Country maxInternetUsers();
    Country minAdultLiteracy();
    Country maxAdultLiteracy();
    Double avgInternetUsers();
    Double avgAdultLiteracy();
    Double getCorrelationCoefficient();
    boolean isPositive();
}
