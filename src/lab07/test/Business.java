
package lab07.test;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;


/**
 * Business class — the main control center for this mini business system.
 * ------------------------------------------------------------
 * It manages three key parts:
 *   1. CustomerDirectory  →  All customers (people who buy products)
 *   2. ProductCatalog     →  All available products (with price ranges)
 *   3. MasterOrderList    →  All orders placed by customers
 *
 * Extended Features:
 *   ✅ Uses JavaFaker to automatically generate random customers, products, and orders.
 *   ✅ Allows testing or demo without manual data entry.
 *   ✅ Includes clear formatted printing for order details.
 *
 * All methods include detailed explanations for beginner understanding.
 *
 * Author: Jiaen Xu; Editor: zihuawu
 */
public class Business {

    // --- Core components ---
    private CustomerDirectory customerDirectory;
    private MasterOrderList masterOrderList;
    private SupplierDirectory supplierDirectory;
    private EmployeeDirectory employeeDirectory;




    // --- Faker & Random setup ---
    // Faker helps us create random realistic names and product info
    private final Faker faker = new Faker();
    // Random helps us generate random numbers for prices, quantities, etc.
    private final Random rand = new Random();


    public Business() {
        customerDirectory = new CustomerDirectory();
        masterOrderList = new MasterOrderList();
        supplierDirectory = new SupplierDirectory();
        employeeDirectory = new EmployeeDirectory();
    }

    // ===========================================================
    // Original Core Methods (Manual Control)
    // ===========================================================

    public Order createOrder(Customer c, String id) {
        Order o = masterOrderList.createOrder(c,id);
        c.addOrder(o);
        return o;
    }

    public OrderItem addOrderItem(Order order, Product p, int qty, double price) {
        return masterOrderList.addOrderItem(order, p, qty, price);
    }

    /**
     * Print all order items for one order.
     * Demonstrates formatted printing with printf.
     */
    public void printOrderDetails(Order order) {
        System.out.println("Order for customer: " + order.getCustomer().getName());
        for (OrderItem item : order.getOrderItems()) {
            double total = item.getQuantity() * item.getPaidPricePerItem();
            // Using printf for neat alignment and consistent decimals
            System.out.println("- " + item.getProduct().getName()
                + " | Quantity: " + item.getQuantity()
                + " | Price per item: $" + String.format("%.2f", item.getPaidPricePerItem())
                + " | Total: $" + String.format("%.2f", total));
        }
    }

    // Getters (for external access)
    public CustomerDirectory getCustomerDirectory() { return customerDirectory; }
    public MasterOrderList getMasterOrderList() { return masterOrderList; }
    public SupplierDirectory getSupplierDirectory() { return supplierDirectory; }

    
    
    // ===========================================================
    // New: Faker-Based Auto Data Generation
    // ===========================================================

    // ==============================================================
    //  1. Generate Random Suppliers
    // ==============================================================
    /**
     * Generates a list of random suppliers and adds them to the SupplierDirectory.
     * Each supplier will have an empty ProductCatalog initially (products are added later).
     *
     * @param count number of suppliers to create
     */
    public void generateRandomSuppliers(int count) {
        for (int i = 0; i < count; i++) {
            // faker.company().name() gives realistic company names like "Acme Corp", "Dell Technologies"
            String supplierName = faker.company().name();
            String supplierId = "S" + faker.number().digits(4);
            Supplier s = supplierDirectory.addSupplier(supplierName,supplierId);
        }

    }

    // ==============================================================
    //  2. Generate Random Products
    // ==============================================================
    /**
     * Randomly adds products to each existing supplier.
     * Each supplier receives 2–4 random products with randomized price ranges.
     *
     * @param minProducts minimum number of products per supplier
     * @param maxProducts maximum number of products per supplier
     */
    public void generateRandomProducts(int minProducts, int maxProducts) {
        for (Supplier s : supplierDirectory.getSuppliers()) {
            int numProducts = minProducts + rand.nextInt(maxProducts - minProducts + 1);

            for (int i = 0; i < numProducts; i++) {
                String name = faker.commerce().productName();  // e.g., "Ergonomic Wooden Chair"
                String id = "P " + faker.number().digits(5);
                double floor = 10 + rand.nextInt(50);
                double target = floor + rand.nextInt(30);
                double ceiling = target + rand.nextInt(20);
                s.addProduct(name, id, floor, target, ceiling);
            }
        }
    }

    // ==============================================================
    //  3. Generate Random Customers
    // ==============================================================
    /**
     * Creates random customers using Faker's name generator.
     *
     * @param count number of customers to generate
     */
    public void generateRandomCustomers(int count) {
        for (int i = 0; i < count; i++) {
            String name = faker.name().fullName();
            String id = "C " + faker.number().digits(4);
            
            int age = 18 + rand.nextInt(40);//18-57 years old
            String region = pickRandomRegion();
            String income = pickRandomIncomeLevel();
            
            customerDirectory.addCustomer(name, id, age, region, income);
        }

    }

    // ==============================================================
    //  4. Generate Random Orders
    // ==============================================================
    /**
     * Creates random orders for each customer.
     * Each order will later be filled with random OrderItems.
     *
     * Future enhancement: connect each Order with a random SalesPerson.
     *
     * @param ordersPerCustomer how many orders each customer will have
     * @param maxItemsPerOrder  maximum number of order items allowed per order
     */
    public void generateRandomOrders(int ordersPerCustomer, int maxItemsPerOrder) {
        for (Customer c : customerDirectory.getCustomers()) {
            for (int i = 0; i < ordersPerCustomer; i++) {
                String orderId = "O " + faker.number().digits(6);
                Order o = masterOrderList.createOrder(c,orderId);
                c.addOrder(o);
                SalesProfile sp = employeeDirectory.getRandomSalesProfile();
                o.setSalesProfile(sp);
                sp.addOrder(o);
                
                

                // ⚠️ Placeholder: later link a SalesPerson here
                // Example:
                // SalesProfile sp = pickRandomSalesPerson();
                // o.setSalesPerson(sp);

                // Now generate random items for this order
                generateRandomOrderItems(o, maxItemsPerOrder);
            }
        }

    }

    // ==============================================================
    //  5. Generate Random Order Items
    // ==============================================================
    /**
     * Adds random OrderItems to a given Order.
     * Randomly selects products from any supplier.
     *
     * @param order the order to populate
     * @param maxItems maximum number of order items to add
     */
    public void generateRandomOrderItems(Order order, int maxItems) {
        int numItems = 1 + rand.nextInt(Math.max(1, maxItems));

        for (int j = 0; j < numItems; j++) {
            Product p = pickRandomProductFromAnySupplier();
            if (p == null) continue;

            double paidPrice = p.getFloorPrice()
                    + rand.nextDouble() * (p.getCeilingPrice() - p.getFloorPrice());
            int qty = 1 + rand.nextInt(5);

            masterOrderList.addOrderItem(order, p, qty, paidPrice);
        }
    }

    // ==============================================================
    //  6. Placeholder: Generate Random SalesPeople (Todo)
    // ==============================================================
    /**
     * (Todo Feature)
     * Randomly generates SalesProfiles
     *
     */
    public void generateRandomSalesPeople(int count) {
        for (int i = 0; i < count; i++){
            String id = "EMP-" + faker.number().digits(4);
            String name = faker.name().fullName();
            SalesProfile sp = new SalesProfile(id, name);
            employeeDirectory.addSalesProfile(sp);
        }
        
        // TODO: implement later
    }

    // ==============================================================
    //  Helper: Random Product Selector
    // ==============================================================
    /**
     * Randomly selects one product from any supplier in the directory.
     * Returns null if there are no suppliers or no available products.
     */
    private Product pickRandomProductFromAnySupplier() {
        List<Supplier> suppliers = supplierDirectory.getSuppliers();
        if (suppliers.isEmpty()) return null;

        Supplier s = suppliers.get(rand.nextInt(suppliers.size()));
        ArrayList<Product> products = s.getProducts();
        if (products.isEmpty()) return null;

        return products.get(rand.nextInt(products.size()));
    }


    // ==============================================================
    //  7. High-level Mock Data Generator
    // ==============================================================
    /**
     * One-click method that builds a complete mock business dataset.
     * Runs all generation steps in a logical order:
     *   1. Suppliers
     *   2. Products
     *   3. Customers
     *   4. Orders (and order items)
     *   5. (Future) SalesPeople
     *
     * @param numSuppliers      number of suppliers
     * @param numCustomers      number of customers
     * @param ordersPerCustomer orders per customer
     * @param maxItemsPerOrder  maximum products per order
     */
    public void generateMockBusinessData(int numSuppliers, int numCustomers,
                                         int ordersPerCustomer, int maxItemsPerOrder) {
        generateRandomSuppliers(numSuppliers);
        generateRandomProducts(2, 4);
        generateRandomCustomers(numCustomers);
        // generateRandomSalesPeople(); // To do
        generateRandomSalesPeople(4);
        generateRandomOrders(ordersPerCustomer, maxItemsPerOrder);
    }
    
    // ===========================================================
    //  8. Printing Section — Summarize Generated Mock Data
    // ===========================================================
    /**
    * Print all suppliers and their products.
    */
    public void printAllSuppliers() {
        System.out.println("=== Supplier List and Products ===");
        if (supplierDirectory.isEmpty()) {
            System.out.println("[No suppliers available]");
            return;
        }

        for (Supplier s : supplierDirectory.getSuppliers()) {
            System.out.println("Supplier: " + s.getName() + " (ID: " + s.getId() + ")");
            ArrayList<Product> products = s.getProducts();
            if (products.isEmpty()) {
                System.out.println("   (No products yet)");
            } else {
                for (Product p : products) {
                    System.out.println("   - " + p.getName()
                            + " | ID: " + p.getId()
                            + " | floor $" + String.format("%.2f", p.getFloorPrice())
                            + " | target $" + String.format("%.2f", p.getTargetPrice())
                            + " | ceiling $" + String.format("%.2f", p.getCeilingPrice()));
                }
            }
            System.out.println();
        }
    }

    /**
     * Print all customers and their orders.
     */
    public void printAllCustomers() {
        System.out.println("=== Customer Orders Summary ===");
        if (customerDirectory.getCustomers().isEmpty()) {
            System.out.println("[No customers available]");
            return;
        }

        for (Customer c : customerDirectory.getCustomers()) {
            System.out.println("Customer: " + c.getName() + " (ID: " + c.getId() + ")");
            if (c.getOrders().isEmpty()) {
                System.out.println("   (No orders yet)");
            } else {
                for (Order o : c.getOrders()) {
                    printOrderDetails(o);
                }
            }
        }
        System.out.println();
    }

    /**
     * Print all orders from the MasterOrderList.
     */
    public void printAllOrders() {
        System.out.println("=== All Orders in System ===");
        if (masterOrderList.getOrders().isEmpty()) {
            System.out.println("[No orders created]");
            return;
        }

        for (Order o : masterOrderList.getOrders()) {
            System.out.println("Order ID: " + o.getId() +
                               " | Customer: " + o.getCustomer().getName());
            for (OrderItem item : o.getOrderItems()) {
                System.out.println("   - Product: " + item.getProduct().getName()
                        + " | Qty: " + item.getQuantity()
                        + " | Price: $" + String.format("%.2f", item.getPaidPricePerItem()));
            }
        }
        System.out.println();
    }

    /**
     * Placeholder method — print all SalesPeople and their handled orders.
     * (To be implemented later)
     */
    public void printAllSalesPeopleOrders() {
    System.out.println("=== Sales People and Their Orders ===");

    if (employeeDirectory.getSalesProfiles().isEmpty()) {
        System.out.println("[No salespeople found]");
        return;
    }

    for (SalesProfile sp : employeeDirectory.getSalesProfiles()) {
        System.out.println("\nSalesPerson: " + sp.getName() + " (ID: " + sp.getId() + ")");
        
        if (sp.getOrders().isEmpty()) {
            System.out.println("   (No orders handled yet)");
            continue;
        }

        for (Order o : sp.getOrders()) {
            System.out.println("  - Order ID: " + o.getId() +
                               " | Customer: " + o.getCustomer().getName());

            for (OrderItem item : o.getOrderItems()) {
                double lineTotal = item.getQuantity() * item.getPaidPricePerItem();
                System.out.println("      > Product: " + item.getProduct().getName()
                        + " | Qty: " + item.getQuantity()
                        + " | Price: $" + String.format("%.2f", item.getPaidPricePerItem())
                        + " | Line Total: $" + String.format("%.2f", lineTotal));
            }
        }
    }
    }
    
    // ==============================================================
    //  9. Create Markets & Channels
    // ==============================================================
    /**
     * Initialize at least 3 markets, each with exactly 2 channels.
     * One channel (Mobile App) is shared by two markets.
     */
    public MarketDirectory createMarketsAndChannels() {
        MarketDirectory md = new MarketDirectory();

    // Shared channel
        Channel mobileApp = new Channel("Mobile App", "Online");

    // Other channels
        Channel socialMedia = new Channel("Social Media Ads", "Online");
        Channel email = new Channel("Email Campaign", "Online");
        Channel accountManager = new Channel("Account Manager", "Offline");
        Channel website = new Channel("Corporate Website", "Online");

    // Market A: Young Budget Shoppers
        Market youngBudget = md.addMarket("Young Budget Shoppers");
        youngBudget.addChannel(socialMedia);
        youngBudget.addChannel(mobileApp); // shared

    // Market B: Rising Professionals
        Market risingPros = md.addMarket("Rising Professionals");
        risingPros.addChannel(email);
        risingPros.addChannel(mobileApp); // shared

    // Market C: Premium Loyal Customers
        Market premium = md.addMarket("Premium Loyal Customers");
        premium.addChannel(accountManager);
        premium.addChannel(website);

        return md;
    }
    private Market classifyCustomerToMarket(Customer c, MarketDirectory md) {
        int age = c.getAge();
        String income = c.getIncomeLevel();

        for (Market m : md.getMarkets()) {
            String name = m.getName();
            if (name.equals("Young Budget Shoppers")) {
                if (age < 30 && "LOW".equals(income)) {
                    return m;
                }
            } else if (name.equals("Rising Professionals")) {
                if (age >= 30 && age < 40 && "MEDIUM".equals(income)) {
                    return m;
                }
            } else if (name.equals("Premium Loyal Customers")) {
                if ("HIGH".equals(income) || age >= 40) {
                    return m;
                }
            }
        }

        // default fallback：Premium
        for (Market m : md.getMarkets()) {
            if (m.getName().equals("Premium Loyal Customers")) {
                return m;
            }
        }
        // back：first market
        return md.getMarkets().get(0);
    }

    // choose one of the two Channels in Market to customer
    private Channel assignChannelWithinMarket(Customer c, Market market) {
        ArrayList<Channel> channels = market.getChannels();
        if (channels.isEmpty()) return null;
        if (channels.size() == 1) return channels.get(0);

        // randomly choose
       int idx = rand.nextInt(channels.size());
       return channels.get(idx);
    }

    // assign Market & Channel to customers 
    public void assignMarketsAndChannels(MarketDirectory md) {
        for (Customer c : customerDirectory.getCustomers()) {
            Market m = classifyCustomerToMarket(c, md);
            c.setMarket(m);
            Channel ch = assignChannelWithinMarket(c, m);
            c.setChannel(ch);
        }
    }
    
    // ==============================================================
    //  10. Report 1 – By Market
    // ==============================================================
    public void printReportByMarket(MarketDirectory md) {
        // Market -> (Channel -> Stats)
        Map<Market, Map<Channel, Stats>> marketStats = new HashMap<>();
        // Market -> total revenue
        Map<Market, Double> marketTotalRevenue = new HashMap<>();

        for (Order o : masterOrderList.getOrders()) {
            Customer c = o.getCustomer();
            Market m = c.getMarket();
            Channel ch = c.getChannel();
            if (m == null || ch == null) continue;

            double orderRevenue = 0.0;
            for (OrderItem item : o.getOrderItems()) {
                orderRevenue += item.getQuantity() * item.getPaidPricePerItem();
            }

            marketStats.putIfAbsent(m, new HashMap<>());
            Map<Channel, Stats> channelMap = marketStats.get(m);
            channelMap.putIfAbsent(ch, new Stats());
            channelMap.get(ch).addOrder(orderRevenue);

            marketTotalRevenue.put(m, marketTotalRevenue.getOrDefault(m, 0.0) + orderRevenue);
        }

        // 按 Market 总收入降序排序
        ArrayList<Market> markets = new ArrayList<>(md.getMarkets());
        Collections.sort(markets, (m1, m2) -> Double.compare(
                marketTotalRevenue.getOrDefault(m2, 0.0),
                marketTotalRevenue.getOrDefault(m1, 0.0)
        ));

        System.out.println("===== REPORT 1 – BY MARKET =====");
        for (Market m : markets) {
            double totalRev = marketTotalRevenue.getOrDefault(m, 0.0);
            System.out.println("Market: " + m.getName()
                    + " | Total Revenue: $" + String.format("%.2f", totalRev));

            Map<Channel, Stats> channelMap = marketStats.getOrDefault(m, new HashMap<>());
            ArrayList<Channel> channels = new ArrayList<>(m.getChannels());

            // Market 内的两个 Channel 也按收入降序
            Collections.sort(channels, (c1, c2) -> {
                double r1 = channelMap.containsKey(c1) ? channelMap.get(c1).getRevenue() : 0.0;
                double r2 = channelMap.containsKey(c2) ? channelMap.get(c2).getRevenue() : 0.0;
                return Double.compare(r2, r1);
            });

            for (Channel ch : channels) {
                Stats st = channelMap.get(ch);
                int count = (st == null) ? 0 : st.getOrderCount();
                double rev = (st == null) ? 0.0 : st.getRevenue();
                System.out.println("   Channel: " + ch.getName()
                        + " | Orders: " + count
                        + " | Revenue: $" + String.format("%.2f", rev));
            }
            System.out.println();
        }
    }
    
    // ==============================================================
    //  11. Report 2 – By Channel
    // ==============================================================
    public void printReportByChannel() {
        Map<Channel, Stats> channelStats = new HashMap<>();

        for (Order o : masterOrderList.getOrders()) {
            Customer c = o.getCustomer();
            Channel ch = c.getChannel();
            if (ch == null) continue;

            double orderRevenue = 0.0;
            for (OrderItem item : o.getOrderItems()) {
                 orderRevenue += item.getQuantity() * item.getPaidPricePerItem();
            }

            channelStats.putIfAbsent(ch, new Stats());
            channelStats.get(ch).addOrder(orderRevenue);
        }

        ArrayList<Channel> channels = new ArrayList<>(channelStats.keySet());
        Collections.sort(channels, (c1, c2) -> Double.compare(
                channelStats.get(c2).getRevenue(),
                channelStats.get(c1).getRevenue()
        ));

        System.out.println("===== REPORT 2 – BY CHANNEL =====");
        for (Channel ch : channels) {
            Stats st = channelStats.get(ch);
            System.out.println("Channel: " + ch.getName()
                    + " | Orders: " + st.getOrderCount()
                    + " | Revenue: $" + String.format("%.2f", st.getRevenue()));
        }
    }

    private String pickRandomRegion() {
        String[] regions = {"West", "East", "South", "International"};
        return regions[rand.nextInt(regions.length)];
}
    private String pickRandomIncomeLevel() {
        String[] levels = {"LOW", "MEDIUM", "HIGH"};
        return levels[rand.nextInt(levels.length)];
}
}
