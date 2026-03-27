package com.dss.criteriapractice.service;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CrudService<ENTITY, ID, DTO> {

    private final JpaRepository<ENTITY, ID> repository;

    private final Function<ENTITY, DTO> toDTO;
    private final Function<DTO, ENTITY> toEntity;


    public CrudService(JpaRepository<ENTITY, ID> repository, Function<ENTITY, DTO> toDTO, Function<DTO, ENTITY> toEntity) {
        this.repository = repository;
        this.toDTO = toDTO;
        this.toEntity = toEntity;
    }

    public DTO save(DTO dto) {
        return Optional.of(dto)
                .map(toEntity)
                .map(repository::save)
                .map(toDTO)
                .orElseThrow();
    }

    public DTO findById(ID id) {
        return repository.findById(id).map(toDTO).orElseThrow();
    }

    public List<DTO> findAll() {
        return repository.findAll().stream().map(toDTO).toList();
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public DTO update(DTO dto) {
        return save(dto);
    }

}
