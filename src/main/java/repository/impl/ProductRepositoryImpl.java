package repository.impl;

import modelo.Product;
import repository.Repository;
import config.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements Repository<Product> {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance();
    }

    private Product createProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setNombre(resultSet.getString("nombre"));
        product.setPrecio(resultSet.getDouble("precio"));
        java.sql.Date dbSqlDate = resultSet.getDate("fechaRegistro");
        if (dbSqlDate != null) {
            LocalDate fechaRegistro = dbSqlDate.toLocalDate();
            product.setFechaRegistro(fechaRegistro.atStartOfDay());
        } else {
            product.setFechaRegistro(null);
        }
        product.setCategoryId(resultSet.getInt("categoryId"));
        return product;
    }

    @Override
    public List<Product> list() {
        List<Product> productList = new ArrayList<>();
        try (Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("""
                                                             SELECT p.*, c.nombre AS categoryName, c.id AS categoryId
                                                             FROM productos AS p 
                                                             INNER JOIN categorias AS c ON p.categoryId = c.id""")) {
            while (resultSet.next()) {
                Product product = createProduct(resultSet);
                productList.add(product);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public Product byId(Integer id) {
        Product product = null;
        try (PreparedStatement preparedStatement = getConnection()
                .prepareStatement("""
                                      
                        SELECT p.*, c.nombre as categoryName, c.id as categoryId
                                      FROM productos AS p 
                                      INNER JOIN categorias AS c ON p.categoryId = c.id
                                      WHERE p.id = ?
    
                             """))
        {
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                product=createProduct(resultSet);
            }
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public void save(Product product) {
        try(PreparedStatement preparedStatement = getConnection()
                .prepareStatement("""
                                       INSERT INTO productos (nombre, precio, fechaRegistro, categoryId) VALUES (?,?,?,?)""")
        ){
            preparedStatement.setString(1, product.getNombre());
            preparedStatement.setDouble(2, product.getPrecio());
            if(product.getFechaRegistro()!=null){
                preparedStatement.setDate(3,java.sql.Date.valueOf(product.getFechaRegistro().toLocalDate()) );
            } else {
                preparedStatement.setNull(3, Types.DATE);
            }
            preparedStatement.setInt(4, product.getCategoryId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        try(PreparedStatement preparedStatement = getConnection().prepareStatement(
                """ 
DELETE FROM productos WHERE id =?"""
        )){
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
