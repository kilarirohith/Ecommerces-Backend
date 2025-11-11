package com.Kilari.services.impl;

import com.Kilari.modal.Category;
import com.Kilari.modal.Deal;
import com.Kilari.modal.HomeCategory;
import com.Kilari.repository.DealRepository;
import com.Kilari.repository.HomeCategoryRepository;
import com.Kilari.services.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private  final HomeCategoryRepository categoryRepository;

    @Override
    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {

        HomeCategory category = categoryRepository.findById(deal.getCategory().getId()).orElse(null);

        Deal newDeal = dealRepository.save(deal);
        newDeal.setCategory(category);
        newDeal.setDiscount(deal.getDiscount());
        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDeal(Deal deal,Long id) throws Exception {
        Deal existingDeal = dealRepository.findById(id).orElse(null);
        HomeCategory category = categoryRepository.findById(deal.getCategory().getId()).orElse(null);

        if(existingDeal != null) {
            if(deal.getDiscount() != null) {
                existingDeal.setDiscount(deal.getDiscount());
            }
            if(category != null){
                existingDeal.setCategory(category);
            }
            return dealRepository.save(existingDeal);
        }
        throw new Exception("Deal not Found");
    }

    @Override
    public void deleteDeal(Long id) throws Exception {
         Deal deal = dealRepository.findById(id).orElseThrow(()->new Exception("Deal not found"));

         dealRepository.delete(deal);
    }
}
