package com.example.cart_api;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class CartItemsManager {

    private static final String XML_FILE_PATH = "cartItems.xml";

    // Method to save or append the cart items while avoiding duplicates
    public void saveCartItems(CartItems newItems) throws JAXBException {
        File xmlFile = new File(XML_FILE_PATH);

        if (xmlFile.exists()) {
            // Load existing items from the XML file
            JAXBContext jaxbContext = JAXBContext.newInstance(CartItems.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            CartItems existingItems = (CartItems) unmarshaller.unmarshal(xmlFile);

            // Check if new items already exist in the existing list, and avoid adding
            // duplicates
            for (CartItems.Item newItem : newItems.getItems()) {
                boolean isDuplicate = existingItems.getItems().stream()
                        .anyMatch(existingItem -> existingItem.getOrderID().equals(newItem.getOrderID()) &&
                                existingItem.getFoodName().equals(newItem.getFoodName()));

                // Add the item only if it is not a duplicate
                if (!isDuplicate) {
                    existingItems.getItems().add(newItem);
                }
            }

            // Save the updated list back to the XML file
            saveToXml(existingItems);
        } else {
            // If the file doesn't exist, create a new file and save the cart items
            saveToXml(newItems);
        }
    }

    // Method to save cart items to an XML file
    private void saveToXml(CartItems cartItems) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CartItems.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(cartItems, new File(XML_FILE_PATH));
    }
}
