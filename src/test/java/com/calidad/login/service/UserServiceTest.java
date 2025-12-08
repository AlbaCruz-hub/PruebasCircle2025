package com.calidad.login.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.calidad.login.dao.IDAOLogin;
import com.calidad.login.modelo.Usuario;

public class UserServiceTest {

    private IDAOLogin dao;
    private UserService userService;

    @BeforeEach
    void setUp(){
        //Se inicializan los mocks antes de cada prueba
        dao = mock(IDAOLogin.class);
        userService = new UserService(dao);
    }

    //1. Creacion de usuario - Camino feliz
    @Test
    void crearUsuarioExitoso(){
        // SetUp
        String email = "alba.cruz@correo.com";
        String password = "Password1234";
        String nombre = "Alba";
        
        //Si el usuario no existe
        when(dao.findUsuarioByEmail(email)).thenReturn(null);

        //Al guardar usuario retorna el ID 1
        when(dao.save(any(Usuario.class))).thenReturn(1);

        //Ejercicio
        Usuario result = userService.createUser(nombre, email, password);

        //Verificar
        assertThat("El email deberia coincidir", result.getEmail(), is(email));
        assertThat("El ID debería ser 1", result.getId(), is(1));

        //Se verifica que el metofo fue llamado 1 vez
        verify(dao, times(1)).save(any(Usuario.class));
        System.out.println("Test Crear usuario fue exitoso");

    }

    //2. Crear usuario con password corto - Camino no feliz
    @Test
    void crearUsuarioPassCorto(){
        // SetUp
        String email = "vianey@correo.com";
        String password = "123";
        String nombre = "Vianey";
        
        //Ejercicio
        Usuario result = userService.createUser(nombre, email, password);

        //Verificar
        //Regresa null porque la validacion falla
        assertThat("El usuario deberia ser nulo por password corto", result, is(nullValue()));

        //Se comprueba que no se llamo a guardar
        verify(dao, times(0)).save(any(Usuario.class));
        System.out.println("Test Password Corto fue exitoso");
    }

    //3. Eliminar usuario
    @Test
    void eliminarUsuario(){
        //SetUp
        int userId = 1;
        when(dao.deleteById(userId)).thenReturn(true);

        //Ejercicio
        boolean result = userService.deleteUser(userId);

        //Verificar
        assertThat(result, is(true));
        verify(dao).deleteById(userId);

        System.out.println("Test Eliminar usuario fue exitoso");
    }

    //4. Actualizar usuario (password)
    @Test
    void actualizarUsuario(){
        //SetUp
        int id = 1;
        Usuario userPassNuevo = new Usuario("Vianey", "via@hotmail.com", "NuevoPass1111");
        userPassNuevo.setId(id);

        Usuario oldUser = new Usuario ("Vianey", "via@hotmail.com", "Pass12345");
        oldUser.setId(id);

        //Se simula que se encontro al antiguo usuario
        when(dao.findById(id)).thenReturn(oldUser);

        //Se simula la actualización
        when(dao.updateUser(any(Usuario.class))).thenReturn(oldUser);

        //Ejercicio
        Usuario result = userService.updateUser(userPassNuevo);

        //Verificar
        assertThat("El password debería haber cambiado", result.getPassword(), is("NuevoPass1111"));
        verify(dao).updateUser(any(Usuario.class));
        System.out.println("Test actualizar password de usuario fue exitoso");

    }

    //5. Buscar por email
    @Test
    void buscarEmail(){
        //SetUp
        String email = "gabi@gmail.com";
        Usuario buscaUsuario = new Usuario("Gabriela", email, "Gabilu12345");
        when(dao.findUsuarioByEmail(email)).thenReturn(buscaUsuario);

        //Ejercicio
        Usuario result = userService.findUserByEmail(email);

        //Verificar
        assertThat(result.getEmail(), is(email));
        System.out.println("Test Buscar por email fue exitoso");

    }

    //6. Buscar todos
    @Test
    void buscaTodos(){
        //SetUp
        List<Usuario> userlist = new ArrayList<>();
        userlist.add(new Usuario("Deysi", "deysi@gmail.com", "Contra12345"));
        userlist.add(new Usuario("Irving", "irving@gmail.com", "pechArmand"));

        when(dao.findAll()).thenReturn(userlist);

        //Ejercicio
        List<Usuario> result = userService.findAllUsers();

        //Verificar
        assertThat("La lista devuelta debe ser igual a la esperada", result, is(userlist));
        System.out.println("Test buscar todos, comparando listas, fue exitoso");

    }

    //7. Usuario ya existente
    @Test
    void crearUsuarioYaExistente(){
        //SetUp
        String email = "existente@correo.com";
        String password = "Password1234";
        String nombre = "Usuario existente";

        //Se simula que el DAO encuentra un usuario con ese email
        Usuario usuarioEncontrado = new Usuario(nombre, email, password);
        usuarioEncontrado.setId(10);
        when(dao.findUsuarioByEmail(email)).thenReturn(usuarioEncontrado);

        //Ejercicio
        Usuario result = userService.createUser(nombre, email, password);

        //Verificar
        //El usuario no debe ser nulo, debe ser el usuario que ya existía
        assertThat(result, is(usuarioEncontrado));
        //verificar que no se llamó a dao.save(), ya que no se debe de duplicar registros
        verify(dao, never()).save(any(Usuario.class));
        System.out.println("Test crear usuario existente (no duplicado) fue exitoso");

    }

}