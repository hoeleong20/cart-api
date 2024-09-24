# Java Cart Items API - README
## Overview
This Java project is a Spring Boot-based application designed to handle cart item operations for a restaurant's kitchen system. It receives cart data via XML from a restaurant ordering system, processes the data, and displays it using XSLT for grouping orders by food name. The kitchen can remove items after preparation, and once all items in an order are complete, the system updates the order status in the database to “completed.”

## Features
- Receive cart items in XML format through a RESTful API.

- Parse and manage cart data using JAXB.

- Use XSLT for grouping and displaying food items.

- Remove cart items after they are served.

- Automatically update order status when all items are completed.

## Technologies Used
- Spring Boot: Framework for building Java web services.

- JAXB (Java Architecture for XML Binding): For converting XML data to Java objects and vice versa.

- XSLT (Extensible Stylesheet Language Transformations): For transforming XML data into HTML for kitchen-side display.

- MySQL: Database for storing order and cart data.

- REST API: For communication between the restaurant system (PHP) and the kitchen system (Java).

## Setup and Installation
### 1. Clone the repository:

In terminal: 

`git clone <repository-url>`

`cd java-cart-api`

### 2.Configure the Database:

Update the MySQL database configuration in application.properties file:

```
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/restaurant-ordering-system
spring.datasource.username=root
spring.datasource.password=
```

### 3.Build and Run the Project:

Build the project using Maven:

`mvn clean install`

Run the Spring Boot application:

`mvn spring-boot:run`

### 4.Run the Application:

The application will run on `http://localhost:8080`.

## API Endpoints
### Receive Cart Items
- URL: `/cart-items`

- Method: `POST`

- Content-Type: `application/xml`

- Description: Receives XML data containing cart items and appends them to the existing list.

### Display Cart Items
- URL: `/show-cart`

- Method: `GET`

- Description: Displays grouped cart items using XSLT for the kitchen.

### Remove Cart Item
- URL: `/remove-item`

- Method: `POST`

- Parameters:
  - `orderID`: The ID of the order to update.

  - `foodName`: The name of the food item to remove.
  
- Description: Removes an item from the cart. If all items in an order are removed, the order status is updated to "completed".

### How It Works
1. Receiving Cart Items: The kitchen receives cart items from the restaurant's ordering system in XML format through the `/cart-items` endpoint. The XML is parsed using JAXB and saved to an XML file. The system checks for duplicates (same order ID and food name) before appending the data.

2. Displaying Cart Items: The `/show-cart` endpoint uses XSLT to group items by food name, allowing the kitchen to prepare similar items together.

3. Removing Cart Items: Once a food item is prepared, the kitchen can remove it from the cart via the `/remove-item` endpoint. If all items of an order are removed, the system updates the order status in the MySQL database to "completed."

### XSLT Transformation
The XSLT file (`cartItems.xsl`) is used to transform the XML data into an HTML table that groups food items by name, making it easier for the kitchen to prepare similar items in batches.

### Future Improvements
- Implement user authentication for kitchen staff.

- Add real-time updates using WebSockets for immediate order updates.

- Enhance error handling and validation for better stability.

### License
This project is licensed under the MIT License.
