package application;

import modelo.Product;
import repository.Repository;
import repository.impl.ProductRepositoryImpl;
import config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Main2 {
    public static void main(String[] args) {
       try(Connection conn = DatabaseConnection.getInstance()){
            Repository<Product> repository = new ProductRepositoryImpl();
            System.out.println("~~~List products from database~~~");
            repository.list().stream().forEach(System.out::println);
            System.out.println("~~~Get by id:1~~~");
            System.out.println(repository.byId(1).toString());
            repository.save(new Product(5,"Lapiz",2000.00, LocalDateTime.now(),2));
         //  System.out.println(repository.byId(5).toString());
           repository.delete(5L);
        //   System.out.println(repository.byId(5).toString());
        }catch (SQLException e){
            e.printStackTrace();
       }
    }
}
