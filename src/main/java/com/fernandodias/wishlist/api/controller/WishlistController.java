package com.fernandodias.wishlist.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fernandodias.wishlist.api.model.Wishlist;
import com.fernandodias.wishlist.api.model.record.ProductRecord;
import com.fernandodias.wishlist.api.model.record.WishlistRecord;
import com.fernandodias.wishlist.api.model.request.WishlistRequest;
import com.fernandodias.wishlist.api.service.impl.IWishlistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@Tag(name = "Wishlist", description = "Wishlist API")
@RequestMapping("/wishlist")
public class WishlistController {

	@Autowired
	private IWishlistService wishlistService;

	@Operation(summary = "Save or update a wishlist", description = "Save the wish list if Id is null and update if the Id is filled")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Wishlist.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Resource not found", content = @Content),
			@ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content)})
	@PostMapping
	public ResponseEntity<?> saveOrUpdate(@Valid @RequestBody WishlistRequest request) {
		WishlistRecord wishlist = wishlistService.saveOrUpdate(request);
		return ResponseEntity.ok().body(wishlist);
	}

	@Operation(summary = "Get wishlist by userId", description = "Return the wish list by userId")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Wishlist.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Resource not found", content = @Content) })
	@GetMapping("/{userId}")
	public ResponseEntity<?> getAllByUser(@PathVariable @NotNull Long userId) {
		WishlistRecord wishlist = wishlistService.findAllByUserId(String.valueOf(userId));
		if (wishlist != null) {
			return ResponseEntity.ok(wishlist);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@Operation(summary = "Get all wishlists", description = "Return all wishlists")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Wishlist.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Resource not found", content = @Content) })
	@GetMapping
	public ResponseEntity<?> getAll() {
		List<WishlistRecord> wishlists = wishlistService.findAll();
		return ResponseEntity.ok(wishlists);
	}

	@Operation(summary = "Remove a product from the wishlist by user id and product sku", description = "Remove the product from the wishlist")
	@ApiResponse(responseCode = "204", description = "Product removed", content = @Content)
	@DeleteMapping("{userId}/product/{sku}")
	public ResponseEntity<?> removeProduct(@PathVariable @NotNull String userId, @PathVariable @NotNull String sku) {
		wishlistService.removeProduct(userId, sku);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Get products from wishlist by user id and name or sku", description = "Returns a list of products if it exists with the name or sku provided")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Wishlist.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Resource not found", content = @Content) })
	@GetMapping("{userId}/products")
	public ResponseEntity<?> getProductByUserIdAndNameOrSku(@PathVariable @NotNull String userId,
			@RequestParam(required = true) String nameOrSku) {
		List<ProductRecord> products = wishlistService.findProductByUserIdAndNameOrSku(userId, nameOrSku);
		return ResponseEntity.ok(products);
	}

}
