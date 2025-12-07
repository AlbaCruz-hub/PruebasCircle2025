package com.calidad.login.service;

import com.calidad.login.dao.IDAOLogin;
import com.calidad.login.modelo.Usuario;

public class LoginService {
    IDAOLogin dao;
        
    public LoginService(IDAOLogin d){ // Inyección de dependencia 
            dao = d;
    }
    public boolean login(String email, String password){
        Usuario u = dao.findUsuarioByEmail(email);
        if (u != null){
            if (u.getPassword() != null && u.getPassword().equals(password)){
                u.setLogged(true);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    
    }

}
