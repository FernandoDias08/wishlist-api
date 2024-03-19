package com.fernandodias.wishlist.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fernandodias.wishlist.api.model.request.ProductRequest;
import com.fernandodias.wishlist.api.model.request.WishlistRequest;

@ExtendWith(MockitoExtension.class)
class WishlistApiTest extends BaseApiTest {

	@Test
	void shouldSaveOrUpdate() throws Exception {

		WishlistRequest request = new WishlistRequest();
		ProductRequest product = new ProductRequest();
		product.setSku("777");
		product.setName("product 777");

		request.setUserId("333");
		request.setProduct(product);

		callPost("/wishlist", request).andExpect(status().is(200)).andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.userId").value("333")).andExpect(jsonPath("$.products.size()").value(1))
				.andExpect(jsonPath("$.products[0].sku").value("777"))
				.andExpect(jsonPath("$.products[0].name").value("product 777"));
	}

}
