package com.example.pickcountrystatecityjava;

import java.util.List;

public class CountryResponse {
    private List<CountryData> data;

    public List<CountryData> getData() {
        return data;
    }

    public void setData(List<CountryData> data) {
        this.data = data;
    }
}
