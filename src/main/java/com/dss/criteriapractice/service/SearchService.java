package com.dss.criteriapractice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;


public class SearchService<ENTITY, DTO> {

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
                                .flatMap(f -> getCriteriaBuilderQuery(f, root, criteriaBuilder, searchText))
                                .toList()
                )
        );

        TypedQuery<ENTITY> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    private Stream<Predicate> getCriteriaBuilderQuery(Field f, Path<ENTITY> root, CriteriaBuilder cb, String searchTerm) {
        Type genericType = f.getGenericType();
        if (f.getType().getClassLoader() != null) {
            Class entityClass = f.getType();
            return Arrays.stream(entityClass.getDeclaredFields())
                    .flatMap(field -> getCriteriaBuilderQuery(field, root.get(f.getName()), cb, searchTerm));
        } else if (genericType.equals(String.class)) {
            return Stream.of(cb.like(cb.lower(root.get(f.getName())), searchTerm));
        } else {
            return Stream.of(cb.disjunction());
        }
    }


    private boolean checkExcluded(Field f) {
        return excludedFields.stream().noneMatch(s -> s.equalsIgnoreCase(f.getName()));
    }
}
