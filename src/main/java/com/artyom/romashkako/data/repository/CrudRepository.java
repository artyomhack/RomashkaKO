package com.artyom.romashkako.data.repository;

import java.util.Optional;

public interface CrudRepository<T, ID> {
    <S extends T> S save(S entity);
    Iterable<T> findAll();
    Optional<T> findById(ID id);
    void deleteById(ID id);
}
