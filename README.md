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

### 2. Install Extensions (VS Code)

1. Install the Java Extension Pack and Spring Boot Extension Pack from the VS Code Extensions Marketplace.
   
### 3. Install Maven:

**a. Download Maven from the official Apache Maven website.**
  - File Type : Binary zip archive
  
  - File Link : apache-maven-3.9.9-bin.zip
  https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip

**b. Extract the files to a directory, e.g., C:\Maven.**

**c. Add Maven to the System PATH:**

  - Right-click This PC -> Properties -> Advanced system settings -> Environment Variables.
  
  - Under System Variables, select Path -> Edit -> New and add the path to your Maven bin folder (e.g., C:\Maven\bin).
  
  - Click OK.

**d. Verify Maven Installation:**

  - Open a new Command Prompt and run:
  
  `mvn -version`
  
  - If it shows the Maven version, the maven has been install and setup successfully.

<br/><br/>**If it's still not recognized:**

**a. Add the Maven bin path manually in PowerShell:**

  - Run this command in PowerShell:
  
      `$env:Path += ";C:\apache-maven-3.9.9\bin"`
  
  - Now try running mvn spring-boot:run again.

**b. If exist Java Version Problem (This Maven project using jdk-23)**   

  *The JAVA_HOME environment variable is not defined correctly, this environment variable is needed to run this program.*
    
<br/><br/>
**Solution:**

**(i) Go to the location of your installed JDK (e.g., C:\Program Files\Java\jdk-23) and copy the path.**

**(ii) Set JAVA_HOME**

  - Right-click on This PC or My Computer, then click Properties.
    
  - Click Advanced system settings.
    
  - Click Environment Variables.
    
  - Under System Variables, click New and:
    
    - Set Variable Name: JAVA_HOME
  
    - Set Variable Value: C:\Program Files\Java\jdk-23 (using your own installe jdk path)
  
    - Click OK to save.

**(iii) Add JAVA_HOME to the PATH**

  - In System Variables, find and select Path, then click Edit.
  
  - Add a new entry: %JAVA_HOME%\bin.
  
  - Click OK to save everything.

**(iv) Verify Maven Installation Again:**

  - Open a new PowerShell window and run: `mvn -version`
    
  - If it shows the Maven version, the maven has been install and setup successfully.
  
  - If it still show empty, Try restarting your computer and check again. Because sometimes, changes to environment variables don’t take effect until a restart.

  - The project should be done setup at this step and can show the `mvn -version`. Following step is run the project.

<br/><br/>
### 4.Build and Run the Project:

Build the project using Maven:

`mvn clean install`

Run the Spring Boot application:

`mvn spring-boot:run`

### 5.Run the Application:

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
