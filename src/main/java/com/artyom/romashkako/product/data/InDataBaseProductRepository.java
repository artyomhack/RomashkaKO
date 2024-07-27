package com.artyom.romashkako.product.data;

import com.artyom.romashkako.common.exception.NotFoundException;
import com.artyom.romashkako.product.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
@AllArgsConstructor
public class InDataBaseProductRepository implements ProductRepository {

    private final CrudProductRepository repository;

    @Override
    public Product save(Product entity) {
        return repository.save(entity);
    }

    @Override
    public List<Product> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public boolean deleteById(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
