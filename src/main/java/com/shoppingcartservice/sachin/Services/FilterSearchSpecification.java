package com.shoppingcartservice.sachin.Services;

import com.shoppingcartservice.sachin.Entities.Product.Product;
import com.shoppingcartservice.sachin.Entities.Product.Subcategory;
import com.shoppingcartservice.sachin.Reposistories.Product.SubcategoryRepo;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FilterSearchSpecification implements Specification<Product> {
    private double minPrice;
    private double maxPrice;
    private String category;
    private String searchString;

    public FilterSearchSpecification(double minPrice, double maxPrice, String category, String searchString) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.category = category;
        this.searchString = searchString;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicateArrayList=new ArrayList<>();

        if(minPrice>0){
            Predicate predicate=criteriaBuilder.greaterThanOrEqualTo(root.get("price"),minPrice);
            predicateArrayList.add(predicate);
        }
        if(maxPrice>0){
            Predicate predicate=criteriaBuilder.lessThanOrEqualTo(root.get("price"),maxPrice);
            predicateArrayList.add(predicate);
        }

        if(category!=null && !category.equals("all")){
            Predicate predicate=criteriaBuilder.equal(root.get("category"),category);
            predicateArrayList.add(predicate);
        }

        if(searchString!=null){
            Predicate predicate=criteriaBuilder.or(criteriaBuilder.like(root.get("name"),"%"+searchString+"%"),criteriaBuilder.like(root.get("details"),"%"+searchString+"%"));
            predicateArrayList.add(predicate);
        }


        return criteriaBuilder.and(predicateArrayList.toArray(Predicate[]::new));
    }
}
