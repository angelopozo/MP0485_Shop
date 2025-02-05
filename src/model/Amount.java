package model;

/**
 *
 * @author Angelo
 */
public class Amount {

    private double value;
    private String currency = "?";

    public Amount(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
