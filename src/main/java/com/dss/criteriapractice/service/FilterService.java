package com.dss.criteriapractice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class FilterService<ENTITY, DTO> {

    private final EntityManager em;
    private final Class<ENTITY> clazz;
    private final Function<ENTITY, DTO> toDTO;

    public FilterService(EntityManager em, Class<ENTITY> clazz, Function<ENTITY, DTO> toDTO) {
        this.em = em;
        this.clazz = clazz;
        this.toDTO = toDTO;
    }

    public List<DTO> filter(DTO dto) {
        List<Field> fieldList = Arrays.stream(dto.getClass().getDeclaredFields()).toList();
        return filterAllFields(fieldList, dto).stream().map(toDTO).toList();
    }

    private List<ENTITY> filterAllFields(List<Field> fieldList, DTO dto) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ENTITY> query = criteriaBuilder.createQuery(clazz);
        Root<ENTITY> root = query.from(clazz);

        query.select(root);

        query.where(
                criteriaBuilder.and(
                        fieldList.stream()
                                .map(f -> {
                                    try {
                                        if (!f.canAccess(dto)) f.setAccessible(true);
                                        if (f.getGenericType().equals(String.class)) {
                                            return criteriaBuilder.like(root.get(f.getName()), "%" + f.get(String.class) + "%");
                                        }
                                        return criteriaBuilder.disjunction();
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .toList()
                )
        );

        TypedQuery<ENTITY> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }
}
