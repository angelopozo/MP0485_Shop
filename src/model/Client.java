package model;

import main.Payable;

/**
 *
 * @author Angelo
 */
public class Client extends Person implements Payable {

    int memberID = 456;
    Amount balance = new Amount(50.0);

    public Client(String name) {
        super(name);
    }

    @Override
    public boolean pay(Amount amountSale) {
        Amount finalAmount = new Amount(this.balance.getValue() - amountSale.getValue());

        return finalAmount.getValue() > 0;
    }

    public String getName() {
        return name;
    }

    public void setBalance(double balance) {
        this.balance.setValue(balance);
    }

    public Amount getBalance() {
        return balance;
    }

}
