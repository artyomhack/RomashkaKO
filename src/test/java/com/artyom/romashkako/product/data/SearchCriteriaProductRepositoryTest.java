package com.artyom.romashkako.product.data;

import com.artyom.romashkako.product.model.Product;
import com.artyom.romashkako.product.utils.ProductUtils;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class SearchCriteriaProductRepositoryTest {

    @Autowired
    JdbcProductRepository productRepository;

    @Autowired
    SearchCriteriaProductRepository criteriaProductRepository;

    ProductUtils productUtils = new ProductUtils();

    @BeforeEach
    public void setUp() {
        productRepository.findAll().forEach(it -> productRepository.deleteProductById(it.getId()));
        Stream.of(
                new Product(null, "Розы", "Цветок с шипами", 59.99, true),
                new Product(null, "Ромашка","Растение используется в лечебных целях", 29.99, true),
                new Product(null, "Рогоз", "Высокие болотные травы", 34.79, false),
                new Product(null, "Базилик", "Род однолетних и многолетних трав", 67.99, true),
                new Product(null, "Багрянник", "Листопадные деревья высотой до 18 м", 144.99, false)
        ).forEach(productRepository::create);
    }

    @Test
    public void shouldReturnProductByCriteriaTitle_whereUseOneFilter() {
        String[] expectTitles = Arrays.array("Розы", "Ромашка", "Рогоз");
        var expectSize = expectTitles.length;

        var actual = criteriaProductRepository.findByCriteria("Ро",null,null,null,null );
        var actualSize = actual.size();

        assertEquals(expectSize, actualSize);
        assertThat(actual).extracting(Product::getTitle).containsOnly(expectTitles);
    }

    @Test
    public void shouldReturnProductByCriteriaPriceGreaterThan_whereUseOneFilter() {
        var expectPrices = Arrays.array(67.99, 144.99);
        var expectSize = expectPrices.length;

        var actual = criteriaProductRepository.findByCriteria(null, 59.99, null, null, null);
        var actualSize = actual.size();

        assertEquals(expectSize, actualSize);
        assertThat(actual).extracting(Product::getPrice).containsOnly(expectPrices);
    }

    @Test
    public void shouldReturnProductByCriteriaPriceLessThan_whereUseOneFilter() {
        var expectPrices = Arrays.array(29.99, 34.79, 59.99);
        var expectSize = expectPrices.length;

        var actual = criteriaProductRepository.findByCriteria(null, null, 60.00, null, null);
        var actualSize = actual.size();

        assertEquals(expectSize, actualSize);
        assertThat(actual).extracting(Product::getPrice).containsOnly(expectPrices);
    }

    @Test
    public void shouldReturnProductByCriteriaAvailableIsTrue_whereUseOneFilter() {
        var expectAvailable = Arrays.array(true, true, true);
        var expectSize = expectAvailable.length;

        var actual = criteriaProductRepository.findByCriteria(null, null, null, true, null);
        var actualSize = actual.size();

        assertEquals(expectSize, actualSize);
        assertThat(actual).extracting(Product::isAvailable).containsOnly(expectAvailable);
    }

    @Test
    public void shouldReturnProductByCriteriaWhereLimitIsOne_whereUseOneFilter() {
        var expectSize = 1;
        var actualSize = criteriaProductRepository.findByCriteria(null, null, null, null, 1).size();
        assertEquals(expectSize, actualSize);
    }
}