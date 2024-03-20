package com.fernandodias.wishlist.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MvcResult;

import com.fernandodias.wishlist.api.config.exception.WishlistException;
import com.fernandodias.wishlist.api.model.request.ProductRequest;
import com.fernandodias.wishlist.api.model.request.WishlistRequest;
import com.jayway.jsonpath.JsonPath;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WishlistApiTest extends BaseApiTest {

	private static final int WISHLIST_LIMIT_SIZE = 20;

	@Test
	@Order(1)
	void shouldSaveOrUpdate() throws Exception {
		WishlistRequest request = new WishlistRequest();
		ProductRequest product = new ProductRequest();
		product.setSku("777");
		product.setName("product 777");

		request.setUserId("333");
		request.setProduct(product);

		callPost("/wishlist", request).andExpect(status().isOk()).andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.userId").value("333")).andExpect(jsonPath("$.products.size()").value(1))
				.andExpect(jsonPath("$.products[0].sku").value("777"))
				.andExpect(jsonPath("$.products[0].name").value("product 777"));
	}

	@Test
	@Order(2)
	void shouldGetAllByUser() throws Exception {

		// WITH RESULT
		callGet("/wishlist/333").andExpect(status().isOk()).andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.userId").value("333")).andExpect(jsonPath("$.products.size()").value(1))
				.andExpect(jsonPath("$.products[0].sku").value("777"))
				.andExpect(jsonPath("$.products[0].name").value("product 777"));

		// WITHOUT RESULT
		callGet("/wishlist/99999").andExpect(status().isNotFound());
	}

	@Test
	@Order(3)
	void shouldGetAll() throws Exception {
		callGet("/wishlist").andExpect(status().isOk()).andExpect(status().isOk());
	}

	@Test
	@Order(4)
	void shouldGetProductByUserIdAndNameOrSku() throws Exception {

		// WITH VALUES
		callGet("/wishlist/products?userId=333&nameOrSku=7").andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$[0].sku").value("777"))
				.andExpect(jsonPath("$[0].name").value("product 777"));

		// WITHOUT VALUES
		callGet("/wishlist/products?userId=333&nameOrSku=88").andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(0));
	}

	@Test
	@Order(5)
	void shouldRemoveProduct() throws Exception {

		// GET WISHLIST
		MvcResult wishList = callGet("/wishlist/333").andExpect(status().isOk()).andReturn();
		String id = JsonPath.read(wishList.getResponse().getContentAsString(), "$.id").toString();

		// REMOVE PRODUCT
		callDelete("/wishlist/" + id + "/product/777").andExpect(status().isNoContent());

		// CHECK PRODUCT VALUES
		callGet("/wishlist/333").andExpect(status().isOk()).andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.userId").value("333")).andExpect(jsonPath("$.products.size()").value(0));
	}

	@Test
	@Order(6)
	void shouldSaveOrUpdateAddMoreProducts() throws Exception {
		WishlistRequest request = new WishlistRequest();
		ProductRequest product = new ProductRequest();
		product.setSku("777");
		product.setName("product 777");

		request.setUserId("333");
		request.setProduct(product);

		callPost("/wishlist", request).andExpect(status().isOk()).andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.userId").value("333")).andExpect(jsonPath("$.products.size()").value(1))
				.andExpect(jsonPath("$.products[0].sku").value("777"))
				.andExpect(jsonPath("$.products[0].name").value("product 777"));

		// ADD A SECOND PRODUCT
		ProductRequest secondProduct = new ProductRequest();
		secondProduct.setSku("888");
		secondProduct.setName("product 888");

		request.setUserId("333");
		request.setProduct(secondProduct);

		callPost("/wishlist", request).andExpect(status().isOk()).andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.userId").value("333")).andExpect(jsonPath("$.products.size()").value(2))
				.andExpect(jsonPath("$.products[0].sku").value("777"))
				.andExpect(jsonPath("$.products[0].name").value("product 777"))
				.andExpect(jsonPath("$.products[1].sku").value("888"))
				.andExpect(jsonPath("$.products[1].name").value("product 888"));

	}

	@Test
	@Order(7)
	void shouldNotSaveOrUpdate() throws Exception {
		WishlistRequest request = new WishlistRequest();
		ProductRequest product = new ProductRequest();
		request.setUserId("555");

		// ADD PRODUCT LIMIT IN WISHLIST
		for (int i = 1; i <= WISHLIST_LIMIT_SIZE; i++) {
			product.setSku(String.valueOf(i));
			product.setName("product " + String.valueOf(i));
			request.setProduct(product);
			callPost("/wishlist", request).andExpect(status().isOk()).andExpect(jsonPath("$.id").isNotEmpty())
					.andExpect(jsonPath("$.userId").value("555")).andExpect(jsonPath("$.products.size()").value(i));
		}

		// ADD A PRODUCT BEYOND THE LIMIT
		product.setSku("21");
		product.setName("product 21");
		request.setProduct(product);
		callPost("/wishlist", request).andExpect(status().is4xxClientError())
				.andExpect(result -> assertEquals(WishlistException.class, result.getResolvedException().getClass()))
				.andExpect(result -> assertThat(result.getResolvedException().getMessage()
						.equals("Error on save/update: The wishlist can have a maximum of 20 items")));

		// ADD A REPEATED PRODUCT
		product.setSku("1");
		product.setName("product 1");
		request.setProduct(product);
		callPost("/wishlist", request).andExpect(status().isOk()).andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.userId").value("555"))
				.andExpect(jsonPath("$.products.size()").value(WISHLIST_LIMIT_SIZE));

	}

}
