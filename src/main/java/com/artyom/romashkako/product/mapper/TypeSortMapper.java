package com.artyom.romashkako.product.mapper;

import com.artyom.romashkako.common.enums.TypeSort;
import org.springframework.stereotype.Component;

@Component
public class TypeSortMapper {

    public TypeSort toTypeSort(String sort) {
        var type = TypeSort.NON;
        switch (sort) {
            case "titleDesc" -> type = TypeSort.TITLE_DESC;
            case "titleAsc" -> type = TypeSort.TITLE_ASC;
            case "priceUp" -> type = TypeSort.PRICE_UP;
            case "priceDown" -> type = TypeSort.PRICE_DOWN;
        }
        return type;
    }

}
