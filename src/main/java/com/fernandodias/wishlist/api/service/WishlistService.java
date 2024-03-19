package com.fernandodias.wishlist.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fernandodias.wishlist.api.config.exception.WishlistException;
import com.fernandodias.wishlist.api.model.Product;
import com.fernandodias.wishlist.api.model.Wishlist;
import com.fernandodias.wishlist.api.model.record.ProductRecord;
import com.fernandodias.wishlist.api.model.record.WishlistRecord;
import com.fernandodias.wishlist.api.model.request.WishlistRequest;
import com.fernandodias.wishlist.api.repository.WishlistRepository;
import com.fernandodias.wishlist.api.service.impl.IWishlistService;

@Service
public class WishlistService implements IWishlistService {

	@Autowired
	private WishlistRepository wishlistRepository;

	@Override
	public WishlistRecord saveOrUpdate(WishlistRequest request) {
		try {
			Wishlist wishlist = populateWishlist(request);
			wishlist = wishlistRepository.save(wishlist);
			return new WishlistRecord(wishlist.getId(), wishlist.getUserId(), wishlist.getProducts());
		} catch (Exception e) {
			throw new WishlistException("Error on save/update", e);
		}
	}

	private Wishlist populateWishlist(WishlistRequest request) {
		Wishlist wishlist = new Wishlist(request);
		Optional<Wishlist> actualWishlistOptional = wishlistRepository.findByIdOrUserId(request.getId(),
				request.getUserId());
		
		actualWishlistOptional.ifPresent(actualWishlist -> {
			wishlist.setId(actualWishlist.getId());
			Set<Product> actualProducts = new TreeSet<>(Comparator.comparing(Product::getSku));
			actualProducts.addAll(actualWishlist.getProducts());
			actualProducts.add(new Product(request.getProduct()));
			wishlist.setProducts(new ArrayList<>(actualProducts));
		});

		return wishlist;
	}

	@Override
	public void remove(String wishlistId, String sku) {
		Optional<Wishlist> actualWishlistOptional = wishlistRepository.findById(wishlistId);
		actualWishlistOptional.ifPresent(actualWishList -> {
			actualWishList.getProducts().removeIf(product -> product.getSku().equals(sku));
			wishlistRepository.save(actualWishList);
		});
	}

	@Override
	public WishlistRecord findAllByUserId(String userId) {
		return wishlistRepository.findByUserId(userId)
				.map(wishlist -> new WishlistRecord(wishlist.getId(), wishlist.getUserId(), wishlist.getProducts()))
				.orElse(null);
	}

	@Override
	public List<WishlistRecord> findAll() {
		return wishlistRepository.findAll().stream()
				.map(wishlist -> new WishlistRecord(wishlist.getId(), wishlist.getUserId(), wishlist.getProducts()))
				.toList();
	}

	@Override
	public List<ProductRecord> findProductByUserIdAndNameOrSku(String userId, String nameOrSku) {
		return wishlistRepository.findProductByUserIdAndNameOrSku(userId, nameOrSku)
				.map(wishlist -> wishlist.getProducts().stream()
						.map(product -> new ProductRecord(product.getSku(), product.getName(), product.getAddDate()))
						.toList())
				.orElse(Collections.emptyList());
	}

}
