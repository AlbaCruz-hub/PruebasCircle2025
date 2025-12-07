package com.calidad.login.dao.integracion.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.calidad.login.dao.IDAOLogin;
import com.calidad.login.dao.UserMysqlIDAO;
import com.calidad.login.service.UserService;
import com.calidad.login.modelo.Usuario;

public class UserServiceIntegrationTest extends DBTestCase {

    private IDAOLogin dao;
    private UserService service;

    public UserServiceIntegrationTest() {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.mysql.cj.jdbc.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql://localhost:3307/calidad");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "root");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "123456");
    }

    @BeforeEach
    public void setup() throws Exception {
        dao = new UserMysqlIDAO();
        service = new UserService(dao);

        IDatabaseConnection connection = getConnection();
        try{
            if (connection == null){
                fail("Failed to establish a connection to the database");
            } else{
                System.out.println("Connection established successfully");
            }
            //Reiniciar el contador de Auto_Increment a 1
            //Esto es crucial para que el ID sea 1 y coincida con el XML
            connection.getConnection().createStatement().execute("ALTER TABLE usuarios AUTO_INCREMENT = 1");
            DatabaseOperation.TRUNCATE_TABLE.execute(connection, getDataSet());
            DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());

        } catch (Exception e) {
            fail("Error in setup: " + e.getMessage());
        } finally {
            if (connection != null){
                connection.close();
            }
         }
     }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/create.xml"));
    }

    //1. Crear un usuario - compara con XML

    @Test
    public void crearUsuario() {
        service.createUser("user1", "user1@gmail.com", "12345678");

        try {
            IDatabaseConnection conn = getConnection();
            conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);

            //Estado actual
            IDataSet databaseDataSet = conn.createDataSet();
            ITable actualTable = databaseDataSet.getTable("usuarios");

            //Estado esperado
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/addUser.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuarios");

            Assertion.assertEquals(expectedTable, actualTable);
        } catch (Exception e) {
            fail("Error in insert test: " + e.getMessage());
        }
    }

    //2. Eliminar usuario
    @Test
    public void eliminarUsuario(){
        try{
            IDatabaseConnection conn = getConnection();
            
            //Primero se inserta un usuario para tener algo que borrar
            IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/addUser.xml"));
            DatabaseOperation.CLEAN_INSERT.execute(conn, initialDataSet);

            //Ejecutar borrar
            service.deleteUser(1);

            //Verificar 

            IDataSet databaseDataSet = conn.createDataSet();
            ITable actualTable = databaseDataSet.getTable("usuarios");

            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/create.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuarios");

            Assertion.assertEquals(expectedTable, actualTable);
            conn.close();
        } catch (Exception e) {
            fail("Error in update test: " + e.getMessage());
        }
    }

    // 3. Actualizar password
    @Test
    public void actualizarUsuario(){
        try{
            //Insertar usuario original
            IDatabaseConnection conn = getConnection();
            IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/addUser.xml"));
            DatabaseOperation.CLEAN_INSERT.execute(conn, initialDataSet);

            //Preparar actualización
            Usuario userToUpdate = new Usuario("Vianey","user1@gmail.com","Viane190803");
            userToUpdate.setId(1);

            //Ejecutar 
            service.updateUser(userToUpdate);

            //Verificar
            IDataSet databaseDataSet = conn.createDataSet();
            ITable actualTable = databaseDataSet.getTable("usuarios");

            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/updateUser.xml"));
            ITable expectedTable = expectedDataSet.getTable("usuarios");

            Assertion.assertEquals(expectedTable, actualTable);
            conn.close();
        } catch (Exception e){
            fail("Error in update test: " + e.getMessage());
        }
    }

    //4. Buscar por email
    @Test
    public void busquedaEmail(){
        try{
            //Insertar usuarios
            IDatabaseConnection conn = getConnection();
            IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/addUser.xml"));
            DatabaseOperation.CLEAN_INSERT.execute(conn, initialDataSet);

            //Ejecutar
            Usuario result = service.findUserByEmail("user1@gmail.com");

            //Verificar
            assertNotNull(result);
            assertEquals("user1", result.getName());
            assertEquals("12345678", result.getPassword());

            conn.close();
        } catch (Exception e){
            fail("Error in findByEmail test: " + e.getMessage());
        }
    }

    //5. Busca todo
    @Test
    public void buscaTodo(){
        try{
            //Insertar varios usuarios
        IDatabaseConnection conn = getConnection();
        IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/initDB.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(conn, initialDataSet);

        //Ejecutar
        List<Usuario> result = service.findAllUsers();
        
        //Verificar
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("user1", result.get(0).getName());

        conn.close();
        } catch (Exception e){
        fail("Error in findAll test: " + e.getMessage());
        } 
    }

}
