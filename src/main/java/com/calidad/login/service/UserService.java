package com.calidad.login.service;

import java.util.List;
import com.calidad.login.dao.IDAOLogin;
import com.calidad.login.modelo.Usuario;

public class UserService {
    private IDAOLogin dao;

    public UserService(IDAOLogin dao) {
        this.dao = dao;
    }

    // Crear usuario
    public Usuario createUser(String name, String email, String password) {
        if (password.length() >= 8 && password.length() <= 16) {
            // Buscar si el usuario ya existe
            Usuario existingUser = dao.findUsuarioByEmail(email);

            if (existingUser != null) {
                // Devuelve el usuario existente, no se duplica
                return existingUser;
            }

            // Crear nuevo usuario
            Usuario user = new Usuario(name, email, password);
            int id = dao.save(user);
            user.setId(id);
            return user;
        }
        // Password inválido
        return null;
    }

    // Buscar todos los usuarios
    public List<Usuario> findAllUsers() {
        return dao.findAll();
    }

    // Buscar usuario por email
    public Usuario findUserByEmail(String email) {
        return dao.findUsuarioByEmail(email);
    }

    // Buscar usuario por ID
    public Usuario findUserById(int id) {
        return dao.findById(id);
    }

    // Actualizar usuario
    public Usuario updateUser(Usuario user) {
        Usuario userOld = dao.findById(user.getId());

        if (userOld != null) {
            userOld.setName(user.getName());
            userOld.setPassword(user.getPassword());
            return dao.updateUser(userOld);
        }
        return null;
    }

    // Eliminar usuario
    public boolean deleteUser(int id) {
        return dao.deleteById(id);
    }
}
