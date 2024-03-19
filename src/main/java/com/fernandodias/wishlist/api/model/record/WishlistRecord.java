package com.fernandodias.wishlist.api.model.record;

import java.util.List;

import com.fernandodias.wishlist.api.model.Product;

public record WishlistRecord(String id, String userId, List<Product> products) {

}
