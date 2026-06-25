VideoGameShop — Capstone 3

A full-stack e-commerce REST API built with Spring Boot and MySQL, paired with an interactive video game storefront frontend.
Overview
VideoGameShop is a backend API project developed as part of the Year Up United technical training program.
It powers an online video game store where users can browse products, manage a shopping cart, place orders, and update their profile. 
The frontend communicates with the API using JWT authentication and JSON data exchange.
Features

Browse and filter products by category, genre, and price range
User registration and login with JWT authentication
Shopping cart — add, update, and remove items (persists between sessions)
User profile — view and update shipping information
Checkout — converts cart into a permanent order with line items
Admin-only controls — create, update, and delete products and categories
Role-based access control (ROLE_USER vs ROLE_ADMIN)

Technologies Used
Backend

Java
Spring Boot
Spring Data JPA
Spring Security + JWT
REST API

Database

MySQL

Frontend

HTML, CSS, JavaScript, Bootstrap

Development Tools

IntelliJ IDEA
Maven
MySQL Workbench
Insomnia
Git / GitHub
Swagger UI

Architecture
Frontend (HTML/JS)
       ↓
Controller Layer  →  Handles HTTP requests, routes, and security
       ↓
Service Layer     →  Contains business logic
       ↓
Repository Layer  →  Communicates with database via Spring Data JPA
       ↓
MySQL Database

API ENDPOINTS

| Phase | Verb | URL | Description |
|-------|------|-----|-------------|
| 1 | GET | /categories | Get all categories |
| 1 | GET | /categories/{id} | Get category by ID |
| 1 | GET | /categories/{id}/products | Get products in category |
| 1 | POST | /categories | Add category (Admin only) |
| 1 | PUT | /categories/{id} | Update category (Admin only) |
| 1 | DELETE | /categories/{id} | Delete category (Admin only) |
| 2 | GET | /products | Search/filter products |
| 2 | GET | /products/{id} | Get product by ID |
| 3 | GET | /cart | Get current user's cart |
| 3 | POST | /cart/products/{id} | Add product to cart |
| 3 | PUT | /cart/products/{id} | Update cart item quantity |
| 3 | DELETE | /cart | Clear cart |
| 4 | GET | /profile | Get current user's profile |
| 4 | PUT | /profile | Update profile |
| 5 | POST | /orders | Checkout — create order from cart |

HOW IT WORKS

User opens the storefront and browses products
Frontend requests data from the backend API at localhost:8080
Controller receives the HTTP request
Service processes the business logic
Repository retrieves or saves data in MySQL
Data is returned as JSON to the frontend
User can log in, add items to cart, and place orders

Interesting Piece of Code
Location: src/main/java/org/yearup/service/OrderService.java — the checkout method

public Order checkout(int userId)
{
    ShoppingCart cart = shoppingCartService.getByUserId(userId);

  public Order checkout(int userId)
{
    ShoppingCart cart = shoppingCartService.getByUserId(userId);

    if (cart.getItems().isEmpty())
    {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Cannot checkout with an empty cart");
    }

    Order order = new Order();
    order.setUserId(userId);
    order.setDate(LocalDateTime.now());

    Profile profile = profileService.getByUserId(userId);
    if (profile != null)
    {
        order.setAddress(profile.getAddress());
        order.setCity(profile.getCity());
        order.setState(profile.getState());
        order.setZip(profile.getZip());
    }

    Order savedOrder = orderRepository.save(order);

    for (ShoppingCartItem cartItem : cart.getItems().values())
    {
        OrderLineItem lineItem = new OrderLineItem();
        lineItem.setOrderId(savedOrder.getOrderId());
        lineItem.setProductId(cartItem.getProduct().getProductId());
        lineItem.setSalesPrice(cartItem.getProduct().getPrice());
        lineItem.setQuantity(cartItem.getQuantity());
        orderLineItemRepository.save(lineItem);
    }

    shoppingCartService.clearCart(userId);
    return savedOrder;
}
This method is interesting because it coordinates four different services and repositories in a specific sequence that cannot be reordered. 
The order must be saved before line items can be created (because line items need the generated order_id), and the cart must be cleared last. 
It also captures the salesPrice at the moment of purchase rather than looking it up later — ensuring order history is always accurate even if product prices change in the future.

Application Screenshots

<img width="1905" height="902" alt="Screenshot 2026-06-25 155508" src="https://github.com/user-attachments/assets/4291c758-6ad7-41e7-abb6-b024bf329b12" />
<img width="1895" height="897" alt="Screenshot 2026-06-25 155543" src="https://github.com/user-attachments/assets/d7374466-e304-4b8b-8fe7-a5fbe75d2934" />
<img width="616" height="365" alt="Screenshot 2026-06-25 155716" src="https://github.com/user-attachments/assets/18c34982-21da-4fa9-a227-785e0cbe141e" />
<img width="912" height="907" alt="Screenshot 2026-06-25 160508" src="https://github.com/user-attachments/assets/6a3fa6ba-edae-42a5-a758-9be0d0ccde37" />




Setup Instructions

Clone the repository
Run create_database_videogamestore.sql in MySQL Workbench
Set environment variables DB_USERNAME and DB_PASSWORD in your run configuration
Run ECommerceApplication in IntelliJ
Open capstone-client-videogamestore/index.html through IntelliJ to launch the storefront
Access Swagger UI at http://localhost:8080/swagger-ui/index.html

Author
Kenny Sosa
