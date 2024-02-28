package application;

import modelo.Product;
import repository.Repository;
import repository.impl.ProductRepositoryImpl;
import modelo.DatabaseConnection;
import modelo.Product;

import java.sql.Connection;
import java.sql.SQLException;

public class Main2 {
    public static void main(String[] args) {
       try(Connection conn = DatabaseConnection.getInstance()){
            Repository<Product> repository = new ProductRepositoryImpl();
            System.out.println("~~~List products from database~~~");
            repository.list().stream().forEach(System.out::println);
            System.out.println("~~~Get by id:1~~~");
            System.out.println(repository.byId(1).toString());
        }catch (SQLException e){
            e.printStackTrace();
       }
    }
}
