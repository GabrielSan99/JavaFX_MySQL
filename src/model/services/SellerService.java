package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao  = DaoFactory.createSellerDao();
	
	public List <Seller> findAll() {
		return dao.findAll(); // vai no banco de dados buscar tudo
	}
	
	public void saveOrUpdate(Seller obj) { //método de inserção e update de dados nos departamentos
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Seller obj) {
		dao.deleteById(obj.getId());
	}
}
