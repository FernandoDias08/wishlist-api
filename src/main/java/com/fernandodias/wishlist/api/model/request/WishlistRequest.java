package com.fernandodias.wishlist.api.model.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistRequest implements Serializable{

	private static final long serialVersionUID = -9167809000879535316L;
	private String id;
	
	@NotBlank(message = "User ID is required")
	private String userId;
	
	@NotNull(message = "Product is required")
	private transient ProductRequest product;
	
}
