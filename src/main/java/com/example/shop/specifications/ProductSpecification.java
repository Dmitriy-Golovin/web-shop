package com.example.shop.specifications;

import java.util.ArrayList;

import org.hibernate.mapping.Join;
import org.springframework.data.jpa.domain.Specification;
import com.example.shop.models.Product;
import com.example.shop.models.Category;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
public class ProductSpecification implements Specification<Product> {

    private String search;
    private String categoryId;
    private String price_sort;
    private String price_from;
    private String price_to;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        ArrayList<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(search)) {

            // Predicate likeCenter = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + search.toLowerCase() + "%");
            // Predicate likeStart = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), search.toLowerCase()  + "%");
            // Predicate likeEnd = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + search.toLowerCase() );
            // predicates.add(criteriaBuilder.or(likeCenter, likeStart, likeEnd));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + search.toLowerCase() + "%"));
        }

        if (StringUtils.isNotBlank(categoryId)) {
            predicates.add(criteriaBuilder.equal(root.join("category").get("id"), categoryId));
        }

        if (StringUtils.isNotBlank(price_from)) {
            System.out.println(price_from);
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price_from));
        }

        if (StringUtils.isNotBlank(price_to)) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), price_to));
        }

        if (StringUtils.isNotBlank(price_sort)) {
            if (price_sort.equals("asc")) {
                query.orderBy(criteriaBuilder.asc(root.get("price")));
            }

            if (price_sort.equals("desc")) {
                query.orderBy(criteriaBuilder.desc(root.get("price")));
            }
        }

        return predicates.size() <= 0 ? null : criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    public String getSearch() {
        return this.search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPrice_sort() {
        return this.price_sort;
    }

    public void setPrice_sort(String price_sort) {
        this.price_sort = price_sort;
    }

    public String getPrice_from() {
        return this.price_from;
    }

    public void setPrice_from(String price_from) {
        this.price_from = price_from;
    }

    public String getPrice_to() {
        return this.price_to;
    }

    public void setPrice_to(String price_to) {
        this.price_to = price_to;
    }

}
