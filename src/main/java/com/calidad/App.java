package com.calidad;

import java.util.List;

import com.calidad.login.modelo.Usuario;
import com.calidad.login.dao.DAOLogin;

/**
 * Hello world!
 *
 */
public class App {
   public static void main(String[] args){
    try{
        System.out.println("Iniciando...");

        //1. Crear el objeto Usuario 
        Usuario usuario = new Usuario("Vianey", "viane.cante@gmail.com", "123456789");

        //2. Instanciar el DAO
        DAOLogin dao = new DAOLogin();

        //3. Guardar el usuario usando el metodo del DAO
        int resultado = dao.save(usuario);

        if (resultado > 0){
            System.out.println("Usuario guardado con ID: " + resultado);
        } else{
            System.out.println("No se pudo guardar el usuario");
        }

        //4. Consultar todos los usuarios para verificar
        System.out.println("\nLista de usuarios en BD: ");
        List<Usuario> listaUsuarios = dao.findAll();

        for (Usuario u : listaUsuarios){
            System.out.println("ID: " + u.getId() + " - Nombre: " + u.getName());
        }
    } catch (Exception e){
        System.out.println("Ocurrio un error: " + e.getMessage());
    }
   }
}