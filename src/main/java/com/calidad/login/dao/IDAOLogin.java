package com.calidad.login.dao;

import java.util.List;

import com.calidad.login.modelo.Usuario;

public interface IDAOLogin {
    Usuario findUsuarioByEmail(String email);

    Usuario findByUserName(String name);

    int save(Usuario user);

    List<Usuario> findAll();
    
    Usuario findById(int id);
    
    boolean deleteById(int id);
   
    Usuario updateUser(Usuario userOld);
   
}
