package com.Kilari.services;

import com.Kilari.modal.Home;
import com.Kilari.modal.HomeCategory;

import java.util.List;

public interface HomeService {

    public Home createHomePageData(List<HomeCategory> allCategories);
}
