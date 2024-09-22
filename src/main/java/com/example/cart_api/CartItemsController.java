package com.example.cart_api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import javax.xml.transform.Transformer;
import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Controller
public class CartItemsController {

    private static final Logger logger = LoggerFactory.getLogger(CartItemsController.class);

    @PostMapping(value = "/cart-items", consumes = "application/xml")
    public ResponseEntity<String> receiveCartItems(@RequestBody String requestBody) {
        logger.info("Received Raw XML Request Body: {}", requestBody);

        try {
            // Parse the XML into CartItems object
            JAXBContext jaxbContext = JAXBContext.newInstance(CartItems.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(requestBody);
            CartItems newItems = (CartItems) unmarshaller.unmarshal(reader);

            // Use CartItemsManager to save or append the items
            CartItemsManager manager = new CartItemsManager();
            manager.saveCartItems(newItems);

            logger.info("Cart items saved or appended successfully");
            return ResponseEntity.ok("Cart items saved or appended successfully");
        } catch (JAXBException e) {
            logger.error("Failed to parse XML", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing XML");
        }
    }

    @GetMapping("/show-cart")
    public ResponseEntity<String> showCart() {
        try {
            // Load XML and XSLT files
            File xmlFile = new File("cartItems.xml"); // Path to your XML file
            File xsltFile = new File("cartItems.xsl"); // Path to your XSLT file

            // Set up the transformation
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Perform transformation
            StringWriter writer = new StringWriter();
            transformer.transform(new StreamSource(xmlFile), new StreamResult(writer));

            // Return the transformed HTML content
            return ResponseEntity.ok(writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error displaying cart items");
        }
    }

    @PostMapping("/remove-item")
    public String removeItem(@RequestParam("orderID") String orderID, @RequestParam("foodName") String foodName) {
        try {
            // Load the XML file
            File xmlFile = new File("cartItems.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(CartItems.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            CartItems cartItems = (CartItems) unmarshaller.unmarshal(xmlFile);

            // Filter out the item that matches both orderID and foodName
            List<CartItems.Item> updatedItems = cartItems.getItems().stream()
                    .filter(item -> !(item.getOrderID().equals(orderID) && item.getFoodName().equals(foodName)))
                    .collect(Collectors.toList());

            cartItems.setItems(updatedItems);

            // Check if any items with the same orderID are still present
            boolean hasItemsWithSameOrderID = updatedItems.stream()
                    .anyMatch(item -> item.getOrderID().equals(orderID));

            // Save the updated XML back to the file
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(cartItems, xmlFile);

            // If no more items with the same orderID, update order status to 'completed'
            if (!hasItemsWithSameOrderID) {
                // Connect to the database and update the order status
                updateOrderStatusToCompleted(orderID);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/show-cart"; // Redirect back to the cart after removal
    }

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/restaurant-ordering-system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public void updateOrderStatusToCompleted(String orderID) {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Establish connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare the SQL statement
            String sql = "UPDATE orders SET status = ? WHERE id = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "completed"); // Set status to 'completed'
            pstmt.setString(2, orderID); // Bind the orderID

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Order status updated successfully!");
                System.out.println("Order status updated successfully!");
            } else {
                logger.info("No order found with the given orderID.");
                System.out.println("No order found with the given orderID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    // Other methods, such as showing the cart, can be added here
}
