package com.artyom.romashkako.product.data;

import com.artyom.romashkako.common.enums.TypeSort;
import com.artyom.romashkako.product.model.Product;
import com.artyom.romashkako.product.utils.ProductUtils;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

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

        var actual = criteriaProductRepository.findByCriteria("Ро",null,null,null,null, null);
        var actualSize = actual.size();

        assertEquals(expectSize, actualSize);
        assertThat(actual).extracting(Product::getTitle).containsOnly(expectTitles);
    }

    @Test
    public void shouldReturnProductByCriteriaPriceGreaterThan_whereUseOneFilter() {
        var expectPrices = Arrays.array(67.99, 144.99);
        var expectSize = expectPrices.length;

        var actual = criteriaProductRepository.findByCriteria(null, 59.99, null, null, null, null);
        var actualSize = actual.size();

        assertEquals(expectSize, actualSize);
        assertThat(actual).extracting(Product::getPrice).containsOnly(expectPrices);
    }

    @Test
    public void shouldReturnProductByCriteriaPriceLessThan_whereUseOneFilter() {
        var expectPrices = Arrays.array(29.99, 34.79, 59.99);
        var expectSize = expectPrices.length;

        var actual = criteriaProductRepository.findByCriteria(null, null, 60.00, null, null,null);
        var actualSize = actual.size();

        assertEquals(expectSize, actualSize);
        assertThat(actual).extracting(Product::getPrice).containsOnly(expectPrices);
    }

    @Test
    public void shouldReturnProductByCriteriaAvailableIsTrue_whereUseOneFilter() {
        var expectAvailable = Arrays.array(true, true, true);
        var expectSize = expectAvailable.length;

        var actual = criteriaProductRepository.findByCriteria(null, null, null, true, null, null);
        var actualSize = actual.size();

        assertEquals(expectSize, actualSize);
        assertThat(actual).extracting(Product::isAvailable).containsOnly(expectAvailable);
    }

    @Test
    public void shouldReturnProductByCriteriaLimitIsOne_whereUseOneFilter() {
        var expectSize = 1;
        var actualSize = criteriaProductRepository.findByCriteria(null, null, null, null, 1, null).size();
        assertEquals(expectSize, actualSize);
    }

    @Test
    public void shouldReturnProductByCriteriaTitleAndPriceGreatThan_whereUseMultiFilters() {
        var expectTitle = Arrays.array("Розы", "Рогоз");
        var expectPrice = Arrays.array(59.99, 34.79);
        var expectAvailable = Arrays.array(true, false);
        var expectSize = 2;

        var actual = criteriaProductRepository.findByCriteria("Ро", 29.99, null, null, null, null);
        var actualSize = actual.size();

        assertEquals(expectSize, actualSize);
        assertThat(actual).extracting(Product::getTitle).containsOnly(expectTitle);
        assertThat(actual).extracting(Product::getPrice).containsOnly(expectPrice);
        assertThat(actual).extracting(Product::isAvailable).containsOnly(expectAvailable);
    }

    @Test
    public void shouldReturnProductByCriteriaPriceLessThanAndAvailableIsFalseAndLimit_whereUseMultiFilters() {
        var expectPrice = Arrays.array(34.79, 144.99);
        var expectAvailable = Arrays.array(false, false);
        var expectSize = 2;

        var actual = criteriaProductRepository.findByCriteria(null, null, 145.00, false, 2, null);
        var actualSize = actual.size();

        assertEquals(expectSize, actualSize);
        assertThat(actual).extracting(Product::getPrice).containsOnly(expectPrice);
        assertThat(actual).extracting(Product::isAvailable).containsOnly(expectAvailable);
    }

    @Test
    public void shouldSortingProductByTitleDesc_whereUseOnlySort() {
        productRepository.findAll().forEach(it -> productRepository.deleteProductById(it.getId()));

        var list = productUtils.getProductForSorting();
        list.forEach(productRepository::create);

        var expectTitles = list.stream()
                .map(Product::getTitle)
                .sorted(Comparator.reverseOrder())
                .toList();

        var actualTitles = criteriaProductRepository.findByCriteria(null,null,null,null,null, TypeSort.TITLE_DESC).stream()
                .map(Product::getTitle)
                .toList();

        assertIterableEquals(expectTitles, actualTitles);
    }

    @Test
    public void shouldSortingProductByTitleAsc_whereUseOnlySort() {
        productRepository.findAll().forEach(it -> productRepository.deleteProductById(it.getId()));

        var list = productUtils.getProductForSorting();
        list.forEach(productRepository::create);

        var expectTitles = list.stream()
                .map(Product::getTitle)
                .sorted(Comparator.naturalOrder())
                .toList();

        var actualTitles = criteriaProductRepository.findByCriteria(null,null,null,null,null, TypeSort.TITLE_ASC).stream()
                .map(Product::getTitle)
                .toList();

        assertIterableEquals(expectTitles, actualTitles);
    }

    @Test
    public void shouldSortingProductByTitleDesc_whereUseOnlySort_whenProductsIsEmpty() {
        productRepository.findAll().forEach(it -> productRepository.deleteProductById(it.getId()));

        var expectTitles = Collections.EMPTY_LIST;

        var actualTitles = criteriaProductRepository.findByCriteria(null,null,null,null,null, TypeSort.TITLE_DESC).stream()
                .map(Product::getTitle)
                .toList();

        assertIterableEquals(expectTitles, actualTitles);
    }

    @Test
    public void shouldSortingProductByTitleAsc_whereUseOnlySort_whenProductsIsEmpty() {
        productRepository.findAll().forEach(it -> productRepository.deleteProductById(it.getId()));

        var expectTitles = Collections.EMPTY_LIST;

        var actualTitles = criteriaProductRepository.findByCriteria(null,null,null,null,null, TypeSort.TITLE_ASC).stream()
                .map(Product::getTitle)
                .toList();

        assertIterableEquals(expectTitles, actualTitles);
    }

    @Test
    public void shouldSortingProductByPriceDown_whereUseOnlySort() {
        productRepository.findAll().forEach(it -> productRepository.deleteProductById(it.getId()));

        var list = productUtils.getProductForSorting();
        list.forEach(productRepository::create);

        var expectPrices = list.stream()
                .map(Product::getPrice)
                .sorted(Comparator.reverseOrder())
                .toList();

        var actualPrices = criteriaProductRepository.findByCriteria(null,null,null,null,null, TypeSort.PRICE_DOWN).stream()
                .map(Product::getPrice)
                .toList();

        assertIterableEquals(expectPrices, actualPrices);
    }

    @Test
    public void shouldSortingProductByPriceUp_whereUseOnlySort() {
        productRepository.findAll().forEach(it -> productRepository.deleteProductById(it.getId()));

        var list = productUtils.getProductForSorting();
        list.forEach(productRepository::create);

        var expectPrices = list.stream()
                .map(Product::getPrice)
                .sorted(Comparator.naturalOrder())
                .toList();

        var actualPrices = criteriaProductRepository.findByCriteria(null,null,null,null,null, TypeSort.PRICE_UP).stream()
                .map(Product::getPrice)
                .toList();

        assertIterableEquals(expectPrices, actualPrices);
    }

    @Test
    public void shouldSortingProductByPriceUp_whereUseOnlySort_whenProductsIsEmpty() {
        productRepository.findAll().forEach(it -> productRepository.deleteProductById(it.getId()));

        var expectPrices = Collections.EMPTY_LIST;

        var actualPrices = criteriaProductRepository.findByCriteria(null,null,null,null,null, TypeSort.PRICE_UP).stream()
                .map(Product::getPrice)
                .toList();

        assertIterableEquals(expectPrices, actualPrices);
    }

    @Test
    public void shouldSortingProductByPriceDown_whereUseOnlySort_whenProductsIsEmpty() {
        productRepository.findAll().forEach(it -> productRepository.deleteProductById(it.getId()));

        var expectPrices = Collections.EMPTY_LIST;

        var actualPrices = criteriaProductRepository.findByCriteria(null,null,null,null,null, TypeSort.PRICE_DOWN).stream()
                .map(Product::getPrice)
                .toList();

        assertIterableEquals(expectPrices, actualPrices);
    }
}