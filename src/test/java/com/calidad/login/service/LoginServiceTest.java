package com.calidad.login.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.calidad.login.dao.IDAOLogin;
import com.calidad.login.modelo.Usuario;

public class LoginServiceTest {

    private IDAOLogin idaologin;
    private Usuario usuario;
    private LoginService loginService; // Esta es la clase que vamos a probar.

    @BeforeEach
    void setUp(){
        //Se inicializa el mock y el servicio
        idaologin = mock(IDAOLogin.class);
        loginService = new LoginService(idaologin);
        System.out.println("Comenzando prueba...");
    }

    @AfterEach 
    void tearDown(){
        System.out.println("Prueba finalizada.\n");
    }

    @Test
    void loginExitosoTest(){
        // SetUp
        String email = "correo@correo.com";
        String password = "abcd1234";

            // Mock findUsuarioByEmail
        usuario = new Usuario(email,true,"Vianey",password);
        when(idaologin.findUsuarioByEmail(email)).thenReturn(usuario);

            // Ejercicio
        Boolean resultadoesperado = loginService.login(email, password);
        Boolean resultadoejecucion = true;

            // Verificación
        assertThat(resultadoesperado, is(resultadoejecucion));
        System.out.println("Login exitoso");
    }

    @Test
    void loginFallido(){
        // SetUp    
        String email = "correo@correo.com";
        String password =  "abcd12";

        usuario = new Usuario(email,true,"Vianey", "ContraNueva12");   
        when(idaologin.findUsuarioByEmail(email)).thenReturn(usuario);

        // Ejercicio 
        Boolean resultadoes = loginService.login(email, password);
        Boolean resultadoej = false;   

        // Verificación
        assertThat(resultadoes, is(resultadoej)); 
        System.out.println("Login fallido por password incorrecto");   
    }

    @Test
    void loginInexistente(){
        // SetUp  
        String email = "correo@gmail.com";
        String password =  "abcd12";

        usuario = null;
        when(idaologin.findUsuarioByEmail(email)).thenReturn(usuario);
            
        // Ejercicio
        Boolean resultadoes = loginService.login(email,password);
        Boolean resultadoej = false;

        // Verificación
        assertThat(resultadoes,is(resultadoej));
        System.out.println("Login inexistente");                                                    
    }


}
