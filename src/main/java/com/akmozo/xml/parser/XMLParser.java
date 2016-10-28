package com.akmozo.xml.parser;

import com.akmozo.xml.generated.order.Order;
import com.akmozo.xml.generated.order.OrderLine;
import com.akmozo.xml.generated.product.Product;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*

C:\cours\netbeans\xml\src\main\resources\sans_ns>xjc order.xsd
analyse dun schéma...
compilation dun schéma...
com\akmozo\_2016\order\ObjectFactory.java
com\akmozo\_2016\order\Order.java
com\akmozo\_2016\order\OrderLine.java
com\akmozo\_2016\order\package-info.java
com\akmozo\_2016\product\ObjectFactory.java
com\akmozo\_2016\product\Product.java
com\akmozo\_2016\product\package-info.java

 */
public class XMLParser {

    // <used-for-DOM-parsing>
    static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    static final InputStream[] MY_SCHEMAS = {
        // ajouter les fichiers xsd pour valider les XMLs
        Thread.currentThread().getContextClassLoader().getResourceAsStream("sans_ns/product.xsd"),
        Thread.currentThread().getContextClassLoader().getResourceAsStream("sans_ns/order.xsd")
    };
    // </used-for-DOM-parsing>
    
    // JAXB XML parsing
    public static void main(String[] args) {
    
        try {
            JAXBContext jctx = JAXBContext.newInstance(Client.class);
            
            List<Product> articles = new ArrayList<>();
            articles.add(new Product("product 01", BigDecimal.ONE, BigInteger.ONE));
            articles.add(new Product("product 02", BigDecimal.ONE, BigInteger.ONE));
            articles.add(new Product("product 03", BigDecimal.ONE, BigInteger.ONE));
            
            Client client = new Client(articles, "nom", "adresse");
            Marshaller m = jctx.createMarshaller();
            m.marshal(client, new FileOutputStream("src/main/resources/sans_ns/new_client.xml"));
            
        } catch (JAXBException | FileNotFoundException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
    }    

    // JAXB XML parsing
    public static void mainJAXB(String[] args) {
        
        try {
            
            JAXBContext jctx = JAXBContext.newInstance("com.akmozo.product:com.akmozo.order");
            
            Unmarshaller um = jctx.createUnmarshaller();
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("sans_ns/product.xml");
            JAXBElement<Product> pElement = (JAXBElement<Product>) um.unmarshal(in);
            Product product = (Product) pElement.getValue();
            
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream("sans_ns/order.xml");
            JAXBElement<Order> oElement = (JAXBElement<Order>) um.unmarshal(in);
            Order order = (Order) oElement.getValue();
            
            for(int i = 0; i < order.getOrderLine().size(); i++){
                OrderLine ord_line = (OrderLine)order.getOrderLine().get(i);
                System.err.println(ord_line.getDescription());
            }
            
            Product new_product = new Product();
            new_product.setDescription("TV LG FDC695");
            new_product.setNum(BigInteger.valueOf(275));
            new_product.setPrice(BigDecimal.valueOf(558));
            
            com.akmozo.xml.generated.product.ObjectFactory factory = new com.akmozo.xml.generated.product.ObjectFactory();
            
            JAXBElement<Product> prodElement = factory.createProduct(new_product);           
            
            Marshaller m = jctx.createMarshaller();
            m.marshal( prodElement, new FileOutputStream("src/main/resources/sans_ns/tv_lg.xml"));            
            
        } catch (JAXBException | FileNotFoundException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    // DOM XML parsing
    public static void mainDOM(String[] args) {

        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("sans_ns/order.xml");

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

            docBuilderFactory.setNamespaceAware(true);
            docBuilderFactory.setValidating(true);
            docBuilderFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);   // valider le xml selon XSD
            docBuilderFactory.setAttribute(JAXP_SCHEMA_SOURCE, MY_SCHEMAS);         // ajouter les chemins de fichier XSD          

            docBuilderFactory.setIgnoringComments(true);
            docBuilderFactory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document docXml = docBuilder.parse(in);

            Node docElement = docXml.getDocumentElement();
            NodeList listeNoeudFils = docElement.getChildNodes();

            //System.out.println("nb child : " + listeNoeudFils.getLength());
            for (int i = 0; i < listeNoeudFils.getLength(); i++) {
                Node childNode = listeNoeudFils.item(i);
                if ("o:orderLine".equals(childNode.getNodeName())) {
                    System.out.println("");
                    System.out.println("Réf. article : " + childNode.getAttributes().item(0).getNodeValue());
                    for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
                        Node child = childNode.getChildNodes().item(j);

                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            Element val = (Element) child;
                            System.err.println("     " + val.getLocalName() + " : " + val.getTextContent());
                        }
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
