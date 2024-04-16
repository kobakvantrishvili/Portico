package com.portico.portico.mapper;
import com.portico.portico.domain.Product;
import com.portico.portico.domain.ProductStorage;
import com.portico.portico.web.schema.AddProductSchema;

public class AddProductMapper {

    public static Product mapToProduct(AddProductSchema schema) {
        // Create a new Product object
        Product product = new Product();

        // Map fields from AddProductSchema to Product
        product.setName(schema.getName());
        product.setManufacturer(schema.getManufacturer());
        product.setCost(schema.getCost());
        product.setDimensions(schema.getDimensions());
        product.setWeight(schema.getWeight());

        return product;
    }

    public static ProductStorage mapToProductStorage(AddProductSchema schema) {
        // Create a new ProductStorage object
        ProductStorage productStorage = new ProductStorage();

        // Map fields from AddProductSchema to ProductStorage
        productStorage.setWarehouseId(schema.getWarehouseId());
        productStorage.setStock(schema.getStock());

        return productStorage;
    }
}

