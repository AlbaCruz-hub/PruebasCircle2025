package com.calidad.login.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.calidad.login.modelo.Usuario;

public class DAOLogin implements IDAOLogin {

    //Metodo para obtener la conexion 
    public Connection getConnectionMySQL(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/calidad";
            String strUserID = "root";
            String strPassword = "123456";
            con = DriverManager.getConnection(dbURL, strUserID, strPassword);
        } catch (Exception e){
            System.out.println("Error de conexion " + e);
        }
        return con;
    }

    //Metodo para mapear resultados
    private Usuario mapResultSetToUsuario(ResultSet rs) throws Exception{
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        boolean isLogged = rs.getBoolean("isLogged");

        Usuario u = new Usuario (email, isLogged, name, password);
        u.setId(id);
        return u;
    }

    @Override
    public Usuario findUsuarioByEmail(String email) {
        Connection connection = getConnectionMySQL();
        Usuario result = null;
        try{
            String query = "SELECT * FROM usuarios WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                result = mapResultSetToUsuario(rs);
            }

            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }
        return result;
    }

    @Override
    public int save(Usuario user) {
        Connection connection = getConnectionMySQL();
        int result = -1;
        try{
            String query = "INSERT INTO usuarios (name, email, password, isLogged) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setBoolean(4, false);

            if (preparedStatement.executeUpdate() >=1){
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()){
                    result = rs.getInt(1);
                }
            }
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }
        return result;
    }

    @Override
    public List<Usuario> findAll() {
        Connection connection = getConnectionMySQL();
        List<Usuario> listaUsuarios = new ArrayList<>();
        try{
            String query = "SELECT * FROM usuarios";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                listaUsuarios.add(mapResultSetToUsuario(rs));
            }
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }
        return listaUsuarios;
    }

    @Override
    public Usuario updateUser(Usuario userOld) {
        Connection connection = getConnectionMySQL();
        Usuario result = null;
        try{
            String query = "UPDATE usuarios SET name = ?, password = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, userOld.getName());
            preparedStatement.setString(2, userOld.getPassword());
            preparedStatement.setInt(3, userOld.getId());

            if (preparedStatement.executeUpdate() >= 1){
                result = userOld;
            }
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }
        return result;
    }

    @Override
    public Usuario findById(int id) {
        Connection connection = getConnectionMySQL();
        Usuario result = null;
        try{
            String query = "SELECT * FROM usuarios WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                result = mapResultSetToUsuario(rs);
            }
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }
        return result;
    }

    @Override
    public boolean deleteById(int id) {
        Connection connection = getConnectionMySQL();
        boolean result = false;
        try{
            String query = "DELETE FROM usuarios WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            if (preparedStatement.executeUpdate() >= 1){
                result = true;
            }
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }
        return result;
    }

    @Override
    public Usuario findByUserName(String name) {
        Connection connection = getConnectionMySQL();
        Usuario result = null;
        try{
            String query = "SELECT * FROM usuarios WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                result = mapResultSetToUsuario(rs);
            }
            connection.close();
        } catch (Exception e){
            System.out.println(e);
        }
        return result;
    }
    
}
