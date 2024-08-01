package com.artyom.romashkako.product.data;

import com.artyom.romashkako.common.enums.TypeSort;
import com.artyom.romashkako.product.model.Product;
import jakarta.persistence.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.*;


@Slf4j
@Repository
@Transactional(propagation = Propagation.MANDATORY)
@AllArgsConstructor
public class JdbcProductRepository implements ProductRepository, SearchCriteriaProductRepository {

    private final RowMapper<Product> rowMapper = (rs, rowNum) -> new Product(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getBoolean("available")
    );

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Product create(Product entity) {
        String sql = "INSERT INTO product (title, description, price, available) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getDescription());
            ps.setDouble(3, entity.getPrice());
            ps.setBoolean(4, entity.isAvailable());
            return ps;
        }, keyHolder);

        var id = keyHolder.getKey();
        entity.setId((Integer) id);
        return entity;
    }


    @Override
    public Product update(Product entity) {
        jdbcTemplate.update("UPDATE product SET title=?2, description=?3, price=?4, available=5 WHERE id=?1",
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPrice(),
                entity.isAvailable()
        );
        return findById(entity.getId()).get();
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM product", rowMapper);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM product WHERE id=?", new Object[]{id}, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            log.debug("Product by id {} not found.", id);
            return Optional.empty();
        }
    }

    @Override
    public int deleteProductById(Integer id) {
        return jdbcTemplate.update("DELETE FROM product WHERE id=?", id);
    }


    @Override
    public List<Product> findByCriteria(String title, Double priceGT, Double priceLT, Boolean available, Integer limit, TypeSort sort) {
        var sql = new StringBuilder("SELECT * FROM product WHERE 1=1");
        var params = new ArrayList<>();

        if (Objects.nonNull(title)) {
            sql.append(" AND title ~* ?");
            params.add(title);
        }
        if (Objects.nonNull(priceGT)) {
            sql.append(" AND price > ?");
            params.add(priceGT);
        }
        if (Objects.nonNull(priceLT)) {
            sql.append(" AND price < ?");
            params.add(priceLT);
        }
        if (Objects.nonNull(available)) {
            sql.append(" AND available = ?");
            params.add(available);
        }
        if (Objects.nonNull(limit)) {
            sql.append(" LIMIT ?");
            params.add(limit);
        }

        switch (sort) {
            case TITLE_DESC -> sql.append(" ORDER BY title DESC;");
            case TITLE_ASC -> sql.append(" ORDER BY title ASC;");
            case PRICE_DOWN -> sql.append(" ORDER BY price DESC;");
            case PRICE_UP ->  sql.append(" ORDER BY price ASC;");
            case NON -> sql.append(" ORDER BY id;");
        }

        return jdbcTemplate.query(sql.toString(), rowMapper, params);
    }

}
