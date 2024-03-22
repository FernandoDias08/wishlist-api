package com.fernandodias.wishlist.api.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fernandodias.wishlist.api.model.request.WishlistRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "wishlist")
public class Wishlist implements Serializable{

	private static final long serialVersionUID = 7920128457917152597L;

	@Id
	private String id;

	@NotBlank(message = "User ID is required")
	private String userId;
	
	@NotBlank(message = "Products is required")
	private List<Product> products;

	public Wishlist(WishlistRequest request) {
		this.userId = request.getUserId();
		this.products = List.of(new Product(request.getProduct()));
	}

}
