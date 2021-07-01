package src.backingbean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import src.jsfcompslib.util.interfaces.IProcessable;

@Named
@SessionScoped
public class IndexBB implements IProcessable, Serializable {
	
	private String name = "NO INFORMADO";
	private String password = "NO INFORMADO";
	
	@Override
	public String process(){
		
		String msg = "nombre = " + getName() + ", pass = " + getPassword();
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
		
		
		
		
		
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	

}
