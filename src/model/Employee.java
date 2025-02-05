package model;

import main.Logable;

/**
 *
 * @author Angelo
 */
public class Employee extends Person implements Logable {

    final int employeeID = 123;
    final String password = "test";

    public Employee(String name) {
        super(name);
    }

    @Override
    public boolean login(int user, String password) {
        return this.employeeID == user && this.password.equals(password);
    }
}
