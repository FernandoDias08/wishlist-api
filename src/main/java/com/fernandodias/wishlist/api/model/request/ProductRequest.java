package com.fernandodias.wishlist.api.model.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest implements Serializable {

	private static final long serialVersionUID = 901088682649426925L;
	private String sku;
	private String name;
}
