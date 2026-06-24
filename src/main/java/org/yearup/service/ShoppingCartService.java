package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.CartItem;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

import java.util.List;

@Service
public class ShoppingCartService
{
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService)
    {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productService = productService;
    }
// now loads cart item rows, looks up each product, builds shopping cart
    public ShoppingCart getByUserId(int userId)
    {
        ShoppingCart cart = new ShoppingCart();
        List<CartItem> cartItems = shoppingCartRepository.findByUserId(userId);

        for (CartItem cartItem : cartItems)
        {
            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(productService.getById(cartItem.getProductId()));
            item.setQuantity(cartItem.getQuantity());
            cart.add(item);
        }

        return cart;
    }
// checks if product already in cart, if yes increases quantity, no nothing = new row
    public ShoppingCart addProduct(int userId, int productId)
    {
        CartItem existing = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if (existing != null)
        {
            existing.setQuantity(existing.getQuantity() + 1);
            shoppingCartRepository.save(existing);
        }
        else
        {
            CartItem newItem = new CartItem();
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(1);
            shoppingCartRepository.save(newItem);
        }

        return getByUserId(userId);
    }
// finds existing row and sets quantity to whatever user wants
    public ShoppingCart updateProduct(int userId, int productId, int quantity)
    {
        CartItem existing = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

        if (existing != null)
        {
            existing.setQuantity(quantity);
            shoppingCartRepository.save(existing);
        }

        return getByUserId(userId);
    }
// deletes all rows for user in one go using the repository's deletebyuserid
    public void clearCart(int userId)
    {
        shoppingCartRepository.deleteByUserId(userId);
    }
}
