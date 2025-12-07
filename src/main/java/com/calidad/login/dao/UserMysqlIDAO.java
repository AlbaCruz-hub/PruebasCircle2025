package com.calidad.login.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.calidad.login.modelo.Usuario;

public class UserMysqlIDAO implements IDAOLogin {

    public Connection getConnectionMySQL() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/calidad", "root", "123456");
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e);
        }
        return con;
    }

    @Override
    public Usuario findByUserName(String name) {
        Connection connection = getConnectionMySQL();
        Usuario result = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM usuarios WHERE name =?");
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean isLogged = rs.getBoolean("isLogged");

                result = new Usuario(email, isLogged, username, password);
                result.setId(id);
            }

            rs.close();
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public int save(Usuario user) {
        Connection connection = getConnectionMySQL();
        int result = -1;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO usuarios(name,email,password,isLogged) VALUES(?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setBoolean(4, user.isLogged());

            if (preparedStatement.executeUpdate() >= 1) {
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        result = rs.getInt(1);
                    }
                }
            }

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public Usuario findUsuarioByEmail(String email) {
        Connection connection = getConnectionMySQL();
        Usuario result = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM usuarios WHERE email = ?");
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("name");
                String emailUser = rs.getString("email");
                String password = rs.getString("password");
                boolean isLogged = rs.getBoolean("isLogged");

                result = new Usuario(emailUser, isLogged, username, password);
                result.setId(id);
            }

            rs.close();
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public List<Usuario> findAll() {
        Connection connection = getConnectionMySQL();
        List<Usuario> listaAlumnos = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM usuarios");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean log = rs.getBoolean("isLogged");

                Usuario retrieved = new Usuario(email, log, name, password);
                retrieved.setId(id);
                listaAlumnos.add(retrieved);
            }

            rs.close();
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return listaAlumnos;
    }

    @Override
    public Usuario findById(int id) {
        Connection connection = getConnectionMySQL();
        Usuario result = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM usuarios WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int idUser = rs.getInt("id");
                String username = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                boolean isLogged = rs.getBoolean("isLogged");

                result = new Usuario(email, isLogged, username, password);
                result.setId(idUser);
            }

            rs.close();
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public boolean deleteById(int id) {
        Connection connection = getConnectionMySQL();
        boolean result = false;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?");
            preparedStatement.setInt(1, id);

            if (preparedStatement.executeUpdate() >= 1) {
                result = true;
            }

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public Usuario updateUser(Usuario userNew) {
        Connection connection = getConnectionMySQL();
        Usuario result = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE usuarios SET name = ?, password = ? WHERE id = ?");
            preparedStatement.setString(1, userNew.getName());
            preparedStatement.setString(2, userNew.getPassword());
            preparedStatement.setInt(3, userNew.getId());

            if (preparedStatement.executeUpdate() >= 1) {
                result = userNew;
            }

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }
}

