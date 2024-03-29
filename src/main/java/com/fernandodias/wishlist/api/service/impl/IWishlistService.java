package com.fernandodias.wishlist.api.service.impl;

import java.util.List;

import com.fernandodias.wishlist.api.model.record.ProductRecord;
import com.fernandodias.wishlist.api.model.record.WishlistRecord;
import com.fernandodias.wishlist.api.model.request.WishlistRequest;

public interface IWishlistService {
	
	WishlistRecord saveOrUpdate(WishlistRequest request);
	
	void removeProduct(String userId, String sku);
	
	WishlistRecord findAllByUserId(String userId);
	
	List<ProductRecord> findProductByUserIdAndNameOrSku(String userId, String nameOrSku);
	
	List<WishlistRecord> findAll();

}
