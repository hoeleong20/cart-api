package com.example.cart_api;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class CartItemsManager {

    private static final String XML_FILE_PATH = "cartItems.xml";

    // Method to save or append the cart items
    public void saveCartItems(CartItems newItems) throws JAXBException {
        File xmlFile = new File(XML_FILE_PATH);

        if (xmlFile.exists()) {
            // If the file exists, append new items
            JAXBContext jaxbContext = JAXBContext.newInstance(CartItems.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            CartItems existingItems = (CartItems) unmarshaller.unmarshal(xmlFile);

            // Append new items to the existing list
            List<CartItems.Item> existingList = existingItems.getItems();
            existingList.addAll(newItems.getItems());

            // Marshal (save) the updated cart items back to the file
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
