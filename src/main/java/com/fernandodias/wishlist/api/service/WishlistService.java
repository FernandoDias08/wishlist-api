package com.fernandodias.wishlist.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
	
	private static final int WISHLIST_LIMIT_SIZE = 20;

	@Autowired
	private WishlistRepository wishlistRepository;

	@Override
    @CacheEvict(value = "wishlistCacheFindAllByUserId", key= "#request.userId")
	public WishlistRecord saveOrUpdate(WishlistRequest request) {
		try {
			Wishlist wishlist = populateWishlist(request);
			wishlist = wishlistRepository.save(wishlist);
			return new WishlistRecord(wishlist.getId(), wishlist.getUserId(), wishlist.getProducts());
		} catch (Exception e) {
			throw new WishlistException("Error on save/update: " + e.getMessage(), e);
		}
	}

	private Wishlist populateWishlist(WishlistRequest request) {
		Wishlist wishlist = new Wishlist(request);
		Optional<Wishlist> actualWishlistOptional = wishlistRepository.findByUserId(request.getUserId());
		
		actualWishlistOptional.ifPresent(actualWishlist -> {
			
			wishlist.setId(actualWishlist.getId());
			Set<Product> actualProducts = new TreeSet<>(Comparator.comparing(Product::getSku));
			actualProducts.addAll(actualWishlist.getProducts());
			actualProducts.add(new Product(request.getProduct()));
			
			checkWishlistLimit(actualProducts);
			wishlist.setProducts(new ArrayList<>(actualProducts));
		});

		return wishlist;
	}

	private void checkWishlistLimit(Set<Product> actualProducts) {
		if (actualProducts.size() > WISHLIST_LIMIT_SIZE) {
			throw new WishlistException(
					String.format("The wishlist can have a maximum of %s items", WISHLIST_LIMIT_SIZE));
		}
	}

	@Override
    @CacheEvict(value = "wishlistCacheFindAllByUserId", key= "#userId")
	public void removeProduct(String userId, String sku) {
		Optional<Wishlist> actualWishlistOptional = wishlistRepository.findByUserId(userId);
		actualWishlistOptional.ifPresent(actualWishList -> {
			actualWishList.getProducts().removeIf(product -> product.getSku().equals(sku));
			wishlistRepository.save(actualWishList);
		});
	}

	@Override
	@Cacheable(cacheNames = "wishlistCacheFindAllByUserId")
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
