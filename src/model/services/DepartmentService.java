package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao  = DaoFactory.createDepartmentDao();
	
	public List <Department> findAll() {
		return dao.findAll(); // vai no banco de dados buscar tudo
	}
	
	public void saveOrUpdate(Department obj) { //método de inserção e update de dados nos departamentos
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Department obj) {
		dao.deleteById(obj.getId());
	}
}
