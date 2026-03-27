package com.dss.criteriapractice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class FilterService<ENTITY, DTO> {

    private final EntityManager em;
    private final Class<ENTITY> clazz;
    private final Function<ENTITY, DTO> toDTO;
    private final List<String> excludedFields = new ArrayList<>();

    public FilterService(EntityManager em, Class<ENTITY> clazz, Function<ENTITY, DTO> toDTO) {
        this.em = em;
        this.clazz = clazz;
        this.toDTO = toDTO;
    }

    public List<DTO> filter(DTO dto) {
        List<Field> fieldList = Arrays.stream(dto.getClass().getDeclaredFields())
                .filter(this::checkExcluded).toList();
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
                                .flatMap(f -> getCriteriaBuilderQuery(f, dto, root, criteriaBuilder))
                                .toList()
                )
        );

        TypedQuery<ENTITY> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    private Stream<Predicate> getCriteriaBuilderQuery(Field f, Object dto, Path<ENTITY> root, CriteriaBuilder cb) {
        try {
            if (!f.canAccess(dto)) f.setAccessible(true);
            Type genericType = f.getGenericType();
            if (f.getType().getClassLoader() != null) {
                Object object = f.get(dto);
                return Arrays.stream(object.getClass().getDeclaredFields())
                        .flatMap(field -> getCriteriaBuilderQuery(field, object, root.get(f.getName()), cb));
            } else if (genericType.equals(String.class)) {
                return Stream.of(cb.like(cb.lower(root.get(f.getName())), "%" + f.get(dto).toString().toLowerCase() + "%"));
            } else if ((genericType.equals(Integer.class) || genericType.equals(Float.class) || genericType.equals(Double.class) || genericType.equals(Long.class) || genericType.equals(Boolean.class) || genericType.equals(LocalDate.class) || genericType.equals(LocalDateTime.class)) && f.get(dto) != null) {
                return Stream.of(cb.equal(root.get(f.getName()),f.get(dto)));
            } else {
                return Stream.of(cb.conjunction());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setExcludedFields(List<String> excludedFieldsList) {
        excludedFields.addAll(excludedFieldsList);
    }

    private boolean checkExcluded(Field f) {
        return excludedFields.stream().noneMatch(s -> s.equalsIgnoreCase(f.getName()));
    }
}
