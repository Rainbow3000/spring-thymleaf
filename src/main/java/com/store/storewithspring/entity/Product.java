package com.store.storewithspring.entity;


import javax.persistence.*;

@Entity
@Table
public class Product  extends BaseEntity{
    private String productName;
    private String productImage;
    @Column(columnDefinition = "TEXT")
    private String productDescription;
    private Double priceNew;
    private Double priceOld;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getPriceNew() {
        return priceNew;
    }

    public void setPriceNew(Double priceNew) {
        this.priceNew = priceNew;
    }

    public Double getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(Double priceOld) {
        this.priceOld = priceOld;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
