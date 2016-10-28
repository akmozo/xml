<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : style.xsl
    Created on : 25 août 2016, 09:54
    Author     : stagiaire
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
                xmlns:o="http://www.akmozo.com/2016/Order" 
                xmlns:p="http://www.akmozo.com/2016/Product">
    
    <xsl:output method="html"/>

    <xsl:template match="/o:order">
        <html>
            <head>
            </head>
            <body>
                <p>Commande de <b>
                        <xsl:value-of select="@date" />
                    </b>
                </p>
                <p>Liste des articles : </p>
                <table 
                    style='border-spacing: 20px; border-collapse: separate; padding: 5px; border: 1px solid #e7e7e7; background-color: #fbfbfb;'>
                    <tr>
                        <th>N°</th>
                        <th>Réf.</th>
                        <th>Description</th>
                        <th>Prix</th>
                        <th>Quantité</th>
                        <th>Total</th>
                    </tr>
                    <xsl:for-each select="o:orderLine">                        
                        <tr>
                            <td>
                                <xsl:number count="o:orderLine" level="single" format="1. "/>
                            </td>
                            <td> 
                                <xsl:value-of select="@num" />
                            </td>
                            <td> 
                                <xsl:value-of select="p:description" />
                            </td>
                            <td> 
                                <xsl:value-of select="p:price" />
                            </td>
                            <td> 
                                <xsl:value-of select="o:quantity" />
                            </td>
                            <td>
                                <xsl:value-of select="o:total" />
                            </td>
                        </tr>                        
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
