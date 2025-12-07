package com.calidad.login.service;

//import java.util.ArrayList;
import java.util.List;

import com.calidad.login.dao.IDAOLogin;
import com.calidad.login.modelo.Usuario;

public class UserService {
    private IDAOLogin dao;
	
	public UserService(IDAOLogin dao) {
		this.dao = dao;
	}
	
	public Usuario createUser(String name, String email, String password) {
		Usuario user = null;
		if (password.length() >= 8 && password.length() <=16) {
			user = dao.findUsuarioByEmail(email);
			
			if (user == null) {
				user = new Usuario(email, false, name, password);
				int id = dao.save(user);
				user.setId(id);
			}
		}
		return user;
	}
	
	public List<Usuario> findAllUsers(){
		List<Usuario> users = dao.findAll();
		return users;
	}

	public Usuario findUserByEmail(String email) {
		return dao.findUsuarioByEmail(email);
	}

	public Usuario findUserById(int id) {
		return dao.findById(id);
	}
    
    public Usuario updateUser(Usuario user) {
    	Usuario userOld = dao.findById(user.getId());

		if (userOld != null){
			userOld.setName(user.getName());
			userOld.setPassword(user.getPassword());
			return dao.updateUser(userOld);
		}
		 return null;
	}

    public boolean deleteUser(int id) {
    	return dao.deleteById(id);
    }

}
