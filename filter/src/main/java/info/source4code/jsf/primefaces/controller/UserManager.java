package info.source4code.jsf.primefaces.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.folf.api.siac.SIAC;

@ManagedBean
@SessionScoped
public class UserManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);

	public static final String HOME_PAGE_REDIRECT = "/secured/home.xhtml?faces-redirect=true";
	public static final String LOGOUT_PAGE_REDIRECT = "/login.xhtml?faces-redirect=true";

	

	private String user;
	private String pass;	
	public boolean activo;
	boolean remember;

	public String login() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		SIAC api = new SIAC();
		String tokenUsuario = api.obtenerTokenUsuario(user, pass);
		if (tokenUsuario != null) {
			System.out.println("token para guardar:"+tokenUsuario);
			activo=true;
			HttpSession session = request.getSession();
			session.setAttribute("SIAC", api);
			session.setMaxInactiveInterval(30 * 60);
			if(remember){
				System.out.println("Se recuerda la cookie");
			}
			else{
				System.out.println("No se recuerda la cookie");
			}
			Cookie cookie = new Cookie("token", tokenUsuario);
			cookie.setMaxAge(30 * 60);
			response.addCookie(cookie);
			LOGGER.info("Acceso correcto '{}'", user);
			return HOME_PAGE_REDIRECT;

		} else {
			LOGGER.info("Error al accesar '{}'", user);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Error verifique información", "Credenciales invalidas."));
			return null;
		}
	}

	public String logout() {
		activo = false;
		String identifier = user;
		LOGGER.debug("Session invalida'{}'", identifier);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		LOGGER.info("Session terminada correctamente '{}'", identifier);
		return LOGOUT_PAGE_REDIRECT;
	}

	public String isLoggedInForwardHome() {
		if (isActivo()) {
			return HOME_PAGE_REDIRECT;
		}

		return null;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}
}