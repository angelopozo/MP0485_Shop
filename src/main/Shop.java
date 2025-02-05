package main;

import java.util.ArrayList;
import model.Product;
import model.Sale;
import java.util.Scanner;
import model.Amount;
import model.Client;
import model.Employee;

public class Shop {

    private Amount cash = new Amount(100.00);
    private ArrayList<Product> inventory;
    private ArrayList<Sale> sales = new ArrayList<>();

    final static double TAX_RATE = 1.04;

    public Shop() {
        inventory = new ArrayList<>();
    }

    public static boolean initSession() {
        Scanner sc = new Scanner(System.in);
        Employee employee = new Employee("test");
        boolean errorLogin = false;

        do {
            System.out.print("Introduzca numero del empleado: ");
            int numEmpleado = sc.nextInt();
            
            System.out.print("Introduzca contraseña: ");
            String contrasena = sc.next();
            
            if (!employee.login(numEmpleado, contrasena)) {
                System.out.println("No has iniciado sesión correctamente. Vuelve a intentarlo.");
            } else {
                errorLogin = true;
            }

        } while (errorLogin == false);
        return errorLogin;
    }

    public static void main(String[] args) {
        Shop shop = new Shop();

        shop.loadInventory();
        int opcion;
        boolean exit = false;

        if (initSession() == true) {
            System.out.println("Login correcto.");
            do {
                opcion = shop.menu();

                switch (opcion) {
                    case 1:
                        shop.showCash();
                        break;
                    case 2:
                        shop.addProduct();
                        break;
                    case 3:
                        shop.addStock();
                        break;
                    case 4:
                        shop.setExpired();
                        break;
                    case 5:
                        shop.showInventory();
                        break;
                    case 6:
                        shop.sale();
                        break;
                    case 7:
                        shop.showSales();
                        break;
                    case 8:
                        shop.showAmountSales();
                        break;
                    case 9:
                        shop.removeProduct();
                        break;
                    case 10:
                        exit = true;
                        break;
                }
            } while (!exit);
        }
    }

    public int menu() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n===========================");
        System.out.println("Menu principal miTienda.com");
        System.out.println("===========================");
        System.out.println("1) Contar caja");
        System.out.println("2) Añadir producto");
        System.out.println("3) Añadir stock");
        System.out.println("4) Marcar producto proxima caducidad");
        System.out.println("5) Ver inventario");
        System.out.println("6) Venta");
        System.out.println("7) Ver ventas");
        System.out.println("8) Ver importe total de ventas.");
        System.out.println("9) Eliminar producto");
        System.out.println("10) Salir programa");
        System.out.print("Seleccione una opción: ");
        int seleccion = sc.nextInt();

        return seleccion;
    }

    /**
     * load initial inventory to shop
     */
    public void loadInventory() {
        addProduct(new Product("Manzana", 10.00, true, 10));
        addProduct(new Product("Pera", 20.00, true, 20));
        addProduct(new Product("Hamburguesa", 30.00, true, 30));
        addProduct(new Product("Fresa", 5.00, true, 20));
    }

    /**
     * show current total cash
     */
    private void showCash() {
        System.out.println("Dinero actual: " + cash.getValue());
    }

    /**
     * add a new product to inventory getting data from console
     */
    public void addProduct() {
        Scanner scanner = new Scanner(System.in);
        Product product;
        String name;

        do {
            System.out.print("\nNombre: ");
            name = scanner.nextLine();

            product = findProduct(name);
            if (product != null) {
                System.out.println("No se puede añadir un producto dos veces.");
            }
        } while (product != null);

        System.out.print("Precio mayorista: ");
        double wholesalerPrice = scanner.nextDouble();
        System.out.print("Stock: ");
        int stock = scanner.nextInt();

        addProduct(new Product(name, wholesalerPrice, true, stock));
        
        System.out.println("\n¿Te gustaría añadir otro producto al inventario?");
        System.out.println("1.- Sí");
        System.out.println("2.- No");
        System.out.print("Indique qué le gustaría hacer: ");
        
        int seleccion = scanner.nextInt();
        
        if (seleccion == 1) {
            addProduct();
        }
    }

    public void removeProduct() {
        Scanner scanner = new Scanner(System.in);
        Product product;
        do {
            System.out.print("Seleccione un nombre de producto: ");
            String name = scanner.next();
            product = findProduct(name);

            if (product != null) {
                System.out.println("El producto " + product.getName() + " ha sido eliminado");
                inventory.remove(product);
            } else {
                System.out.println("El producto que has indicado no está en el inventario");
            }
        } while (product == null);
    }

    /**
     * add stock for a specific product
     */
    public void addStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();
        Product product = findProduct(name);

        if (product != null) {
            // ask for stock
            System.out.print("Seleccione la cantidad a añadir: ");
            int stock = scanner.nextInt();
            // update stock product
            product.setStock(product.getStock() + stock);
            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());

        } else {
            System.out.println("No se ha encontrado el producto con nombre " + name);
        }
    }

    /**
     * set a product as expired
     */
    private void setExpired() {
        Scanner scanner = new Scanner(System.in);
        Product product;
        String name;

        do {
            System.out.print("Seleccione un nombre de producto: ");
            name = scanner.next();
            product = findProduct(name);
            if (product == null) {
                System.out.println("Ese producto no está en el inventario. Prueba otra vez");
            }
        } while (product == null);
        product.expire();
        System.out.println("El precio del producto " + name + " ha sido actualizado a " + product.getPublicPrice());
    }

    /**
     * show all inventory
     */
    public void showInventory() {
        System.out.println("Contenido actual de la tienda:");
        for (Product product : inventory) {
            if (product != null) {
                System.out.println(product.getName());
            }
        }
    }

    /**
     * make a sale of products to a client
     */
    public void sale() {
        // ask for client name
        Scanner sc = new Scanner(System.in);
        ArrayList<Product> vendidos = new ArrayList<>();

        System.out.println("\nRealizar venta, escribir nombre cliente");
        Client client = new Client(sc.nextLine());

        // sale product until input name is not 0
        Amount totalAmount = new Amount(0.0);
        String name = "";
        while (!name.equals("0")) {
            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
            name = sc.nextLine();

            if (name.equals("0")) {
                break;
            }
            Product product = findProduct(name);
            boolean productAvailable = false;

            if (product != null && product.isAvailable()) {
                vendidos.add(product);
                productAvailable = true;
                totalAmount.setValue((totalAmount.getValue() + product.getPublicPrice()));
                product.setStock(product.getStock() - 1);
                // if no more stock, set as not available to sale
                if (product.getStock() == 0) {
                    product.setAvailable(false);
                }
                System.out.println("Producto añadido con éxito");
            }

            if (!productAvailable) {
                System.out.println("Producto no encontrado o sin stock");
            }
        }

        // show cost total
        totalAmount.setValue((totalAmount.getValue() * TAX_RATE));

        if (client.pay(totalAmount)) {
            cash.setValue((cash.getValue() + totalAmount.getValue()));
            client.setBalance(client.getBalance().getValue() - totalAmount.getValue());

            Product[] productosVendidos = vendidos.toArray(Product[]::new);
            Sale ventaHecha = new Sale(client, productosVendidos, totalAmount);
            sales.add(ventaHecha);
            System.out.println("Venta realizada con éxito, total: " + totalAmount.getValue());
        } else {
            cash.setValue((cash.getValue() + totalAmount.getValue()));
            client.setBalance(client.getBalance().getValue() - totalAmount.getValue());

            Product[] productosVendidos = vendidos.toArray(Product[]::new);
            Sale ventaHecha = new Sale(client, productosVendidos, totalAmount);
            sales.add(ventaHecha);
            System.out.println("Venta realizada con éxito, total: " + totalAmount.getValue());
            System.out.println("Cliente debe: " + client.getBalance().getValue());
        }
        
        System.out.println("\n¿Te gustaría añadir otra venta al registro?");
        System.out.println("1.- Sí");
        System.out.println("2.- No");
        System.out.print("Indique qué le gustaría hacer: ");
        
        int seleccion = sc.nextInt();
        
        if (seleccion == 1) {
            sale();
        }
    }

    /**
     * show all sales
     */
    private void showSales() {
        if (!sales.isEmpty()) {
            System.out.println("Lista de ventas:");
            for (Sale sale : sales) {
                if (sale != null) {
                    System.out.println("Nombre del cliente: " + sale.getClient());
                    for (Product product : sale.getProducts()) {
                        System.out.println("- " + product.getName());
                    }
                    System.out.println("Precio total de la venta: " + sale.getAmount() + "\n");
                }
            }
        } else {
            System.out.println("Aún no se ha realizado ninguna venta.");
        }
    }

    /**
     * muestra el total del importe de las ventas
     *
     * @return string con importe total o mensaje de error porque no se ha hecho ninguna venta.
     */
    public void showAmountSales() {
        double totalAmount = 0;
        if (!sales.isEmpty()) {
            for (int i = 0; i < sales.size(); i++) {
                totalAmount += sales.get(i).getAmount();
            }
            System.out.println("El importe total de todas las ventas es: " + totalAmount);
        } else {
            System.out.println("No se ha realizado ninguna venta. Por lo tanto el importe total es 0.");
        }
    }

    /**
     * add a product to inventory
     *
     * @param product
     */
    public void addProduct(Product product) {
        inventory.add(product);
    }

    /**
     * find product by name
     *
     * @param name
     * @return product found by name
     */
    public Product findProduct(String name) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) != null && inventory.get(i).getName().equalsIgnoreCase(name)) {
                return inventory.get(i);
            }
        }
        return null;
    }
}
