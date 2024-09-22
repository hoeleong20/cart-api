<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:key name="foodKey" match="Item" use="foodName" />
    
    <xsl:template match="/">
        <html>
            <head>
                <title>Kitchen System</title>
            </head>
            <body>
                <h1>Preparation Order</h1>
                <table border="1">
                    <tr>
                        <th>Food Name</th>
                        <th>Order ID</th>
                        <th>Quantity</th>
                        <th>Complete Item</th>
                    </tr>
                    <!-- Loop through each unique foodName -->
                    <xsl:for-each select="CartItems/Item[generate-id() = generate-id(key('foodKey', foodName)[1])]">
                        <xsl:variable name="foodName" select="foodName" />
                        <tr>
                            <td rowspan="{count(key('foodKey', $foodName))+1}">
                                <xsl:value-of select="$foodName" />
                            </td>
                            <!-- Inner loop to display all items with the same foodName -->
                            <xsl:for-each select="key('foodKey', $foodName)">
                                <xsl:if test="position() = 1"></xsl:if>
                                <tr>
                                    <td><xsl:value-of select="orderID"/></td>
                                    <td><xsl:value-of select="quantity"/></td>
                                    <td>
                                        <!-- Checkbox or Button to mark as removed -->
                                        <form method="post" action="/remove-item">
                                            <input type="hidden" name="orderID" value="{orderID}" />
                                            <input type="hidden" name="foodName" value="{foodName}" />
                                            <input type="submit" value="Complete" />
                                        </form>
                                    </td>
                                </tr>
                            </xsl:for-each>
                            <!-- Total Quantity -->
                            <tr>
                                <td colspan="4">Total Quantity for <xsl:value-of select="$foodName" />: 
                                    <xsl:value-of select="sum(key('foodKey', $foodName)/quantity)" />
                                </td>
                            </tr>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
