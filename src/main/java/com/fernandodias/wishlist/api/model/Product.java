package com.fernandodias.wishlist.api.model;

import java.util.Date;

import com.fernandodias.wishlist.api.model.request.ProductRequest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {

	private String sku;
	private String name;
	private Date addDate;

	public Product(ProductRequest request) {
		this.sku = request.getSku();
		this.name = request.getName();
		this.addDate = new Date();
	}

}
