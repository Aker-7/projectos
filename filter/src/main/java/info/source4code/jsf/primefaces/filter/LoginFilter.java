package info.source4code.jsf.primefaces.filter;

import info.source4code.jsf.primefaces.controller.UserManager;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.folf.api.siac.SIAC;

public class LoginFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);

	public static final String LOGIN_PAGE = "/login.xhtml";

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

		UserManager userManager = (UserManager) httpServletRequest.getSession().getAttribute("userManager");
		SIAC apiSIAC = (SIAC) httpServletRequest.getSession().getAttribute("SIAC");

		System.out.println("api::" + apiSIAC);
		if (apiSIAC != null) {
			if (userManager.isActivo()) {
				// El usuario esta en session, continue request
				filterChain.doFilter(servletRequest, servletResponse);
			} else {
				LOGGER.debug("Usuario fuera de session");
				// El usuario no esta en session, redirigir a login
				httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + LOGIN_PAGE);
				return;
			}
		} else {
			LOGGER.debug("La API no esta en session");
			String tokenUser = getCookieValue(httpServletRequest, "token");
			System.out.println("token::" + tokenUser);
			if (tokenUser != null) {
				System.out.println("cookie con valor");
				SIAC apiNewSIAC = new SIAC();
				HttpSession session = httpServletRequest.getSession();
				session.setAttribute("SIAC", apiNewSIAC);
				session.setMaxInactiveInterval(30 * 60);
			}
			// El usuario no esta en session, redirigir a login
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + LOGIN_PAGE);
			return;
		}
		/*
		 * System.out.println("Api valor::"+ apiSIAC); boolean isApi=
		 * (apiSIAC!=null); if(!isApi){ //La api no en session
		 * httpServletResponse.sendRedirect(httpServletRequest.getContextPath()
		 * + LOGIN_PAGE); return; } else{ //La api si esta en session Cookie
		 * tokenUser = getCookie(servletRequest, "token");
		 * //System.out.println("cookie::"+tokenUser.getValue());
		 * if(tokenUser!=null){ SIAC apiNewSIAC = new SIAC(); HttpSession
		 * session = httpServletRequest.getSession();
		 * session.setAttribute("SIAC", apiNewSIAC);
		 * session.setMaxInactiveInterval(30 * 60); } else{
		 * httpServletResponse.sendRedirect(httpServletRequest.getContextPath()
		 * + LOGIN_PAGE); } filterChain.doFilter(servletRequest,
		 * servletResponse); }
		 */

		// -----------------------

		/*
		 * // se validad si el controlador esta en session if
		 * (!userManager.isActivo()) {
		 * httpServletResponse.sendRedirect(httpServletRequest.getContextPath()
		 * + LOGIN_PAGE); return; } // verificamos si la api esta en session if
		 * (userManager != null) { if (userManager.isActivo()) { LOGGER.debug(
		 * "Usuario en session"); // El usuario esta en session, continue
		 * request filterChain.doFilter(servletRequest, servletResponse); } else
		 * { LOGGER.debug("Usuario fuera de session"); // El usuario no esta en
		 * session, redirigir a login
		 * httpServletResponse.sendRedirect(httpServletRequest.getContextPath()
		 * + LOGIN_PAGE); return; } } else { LOGGER.debug(
		 * "La API no esta en session"); // El usuario no esta en session,
		 * redirigir a login
		 * httpServletResponse.sendRedirect(httpServletRequest.getContextPath()
		 * + LOGIN_PAGE); return; }
		 */
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("Se inicio el filtro");
		LOGGER.debug("LoginFilter initialized");
	}

	@Override
	public void destroy() {
		// close resources
	}

	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie != null && name.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}