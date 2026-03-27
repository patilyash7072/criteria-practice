package com.dss.criteriapractice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


public class SearchService<ENTITY, DTO>{

    private final EntityManager em;
    private final Class<ENTITY> clazz;
    private final Function<ENTITY, DTO> toDTO;
    private final List<String> excludedFields = new ArrayList<>();

    public SearchService(EntityManager em, Class<ENTITY> clazz, Function<ENTITY, DTO> toDTO) {
        this.em = em;
        this.clazz = clazz;
        this.toDTO = toDTO;
    }


    public List<DTO> search(String search) {
        List<Field> fieldList = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.getGenericType().equals(String.class))
                .filter(this::checkExcluded)
                .toList();
        return searchAllFields(fieldList, search).stream().map(toDTO).toList();
    }

    protected void setExcludedFields(List<String> excludedFieldsList) {
        excludedFields.addAll(excludedFieldsList);
    }

    private List<ENTITY> searchAllFields(List<Field> fieldList, String search) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ENTITY> query = criteriaBuilder.createQuery(clazz);
        Root<ENTITY> root = query.from(clazz);

        String searchText = "%" + search.toLowerCase() + "%";

        query.select(root);

        query.where(
                criteriaBuilder.or(
                        fieldList.stream()
                                .map(f -> criteriaBuilder.like(criteriaBuilder.lower(root.get(f.getName())), searchText))
                                .toArray(Predicate[]::new)
                )
        );

        TypedQuery<ENTITY> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    private boolean checkExcluded(Field f) {
        return excludedFields.stream().noneMatch(s -> s.equalsIgnoreCase(f.getName()));
    }
}
