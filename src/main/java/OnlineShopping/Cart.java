package OnlineShopping;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

    public class Cart {
        private List<Product> items = new ArrayList<>();

        public void addProduct(Product product) {
            items.add(product);
        }

        public void removeProduct(Product product) {
            items.remove(product);
        }

        public List<Product> getItems() {
            return items;
        }

        public int getItemCount() {
            return items.size();
        }
        public double getTotalPrice() {
            double total = 0;
            for (Product product : items) {
                total += product.getPrice();
            }
            return total;
        }

        public void clear() {
            items.clear();
        }

        public String generateBill(String username) {
            // Get current date and time
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = username + "_" + timestamp + ".txt";

            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                writer.println("Online Shopping Platform - Purchase Bill");
                writer.println("User: " + username);
                writer.println("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                writer.println("-------------------------------------------");
                writer.println("Items:");

                for (Product product : items) {
                    writer.println(product.getName() + " - $" + product.getPrice() + " - Made in " + product.getMadeIn());
                }

                writer.println("-------------------------------------------");
                writer.println("Total: $" + getTotalPrice());


                clear();

                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
                return null;


            }
        }}
