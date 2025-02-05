package model;

import java.util.Arrays;

public class Sale {

    Client client;
    Product[] products;
    Amount amount;

    public Sale(Client client, Product[] products, Amount amount) {
        super();
        this.client = client;
        this.products = products;
        this.amount = amount;
    }

    public String getClient() {
        return client.getName();
    }

    public Product[] getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }

    public double getAmount() {
        return amount.getValue();
    }

    public void setAmount(double amount) {
        this.amount.setValue(amount);
    }

    @Override
    public String toString() {
        return "Sale [client=" + client + ", products=" + Arrays.toString(products) + ", amount=" + amount.getValue() + "]";
    }

}
