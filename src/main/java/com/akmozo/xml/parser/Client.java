
package com.akmozo.xml.parser;

import com.akmozo.xml.generated.product.Product;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class Client {
    
    private List<Product> articles;
    private String nom;
    private String adresse;

    public Client(List<Product> articles, String nom, String adresse) {
        this.articles = articles;
        this.nom = nom;
        this.adresse = adresse;
    }
    
    public Client(){
    }

    public List<Product> getArticles() {
        return articles;
    }

    public void setArticles(List<Product> articles) {
        this.articles = articles;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
}
