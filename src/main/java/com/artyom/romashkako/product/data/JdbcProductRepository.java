package com.artyom.romashkako.product.data;

import com.artyom.romashkako.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Repository
@Transactional(propagation = Propagation.MANDATORY)
@AllArgsConstructor
public class JdbcProductRepository implements ProductRepository {

    private final RowMapper<Product> rowMapper = (rs, rowNum) -> new Product(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getBoolean("available")
    );

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Product save(Product entity) {
        Integer id;
        if (Objects.isNull(entity.getId())) {
            id = jdbcTemplate.queryForObject("SELECT nextval('seq_products')", Integer.class);
            jdbcTemplate.update("insert into products values (?, ?, ?, ?, ?)",
                    id,
                    entity.getTitle(),
                    entity.getDescription(),
                    entity.getPrice(),
                    entity.isAvailable()
            );
        } else {
            id = entity.getId();
            jdbcTemplate.update("update products set title=?2, description=?3, price=?4, available=?5 where id=?1",
                    id,
                    entity.getTitle(),
                    entity.getDescription(),
                    entity.getPrice(),
                    entity.isAvailable()
            );
        }

        return findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM products", rowMapper);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM products WHERE id=?", rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            log.debug("Result row mapper is null");
            return Optional.empty();
        }
    }

    @Override
    public int deleteProductById(Integer id) {
        return jdbcTemplate.update("DELETE FROM products WHERE id=?", id);
    }
}
