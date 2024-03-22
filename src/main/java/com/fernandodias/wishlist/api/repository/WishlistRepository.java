package com.fernandodias.wishlist.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fernandodias.wishlist.api.model.Wishlist;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {

	Optional<Wishlist> findByUserId(String userId);

	@Query(value = "{'userId': ?0, 'products': { $elemMatch: { $or: [ { 'name': { $regex: ?1, $options: 'i' } }, { 'sku': { $regex: ?1, $options: 'i' } } ] } } }", fields = "{'products.$': 1}")
	Optional<Wishlist> findProductByUserIdAndNameOrSku(String userId, String nameOrSku);

}
