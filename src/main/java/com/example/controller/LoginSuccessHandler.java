package com.example.controller;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.domain.LoginUserDetails;
import com.example.domain.User;
import com.example.domain.UserModel;
import com.example.repository.LoginUserDetailsRepository;

/**
 * @author subham ashish (subham@sdrc.co.in)
 *
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private LoginUserDetailsRepository loginUserDetailsRepository;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		handle(request, response, authentication);
		clearAuthenticationAttributes(request);

	}

	private void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		String targetUrl = determineTargetUrl(authentication);

		if (response.isCommitted()) {

			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}

		// before redirecting save it's details to database
		LoginUserDetails loginUserDetails = new LoginUserDetails();
		
		UserModel model = (UserModel)authentication.getPrincipal();
		
		User user = new User(model.getUserId());

		String browserDetails = request.getHeader("User-Agent");

		String browser = browserInformation(browserDetails);

		loginUserDetails.setUser(user);
		loginUserDetails.setIpAddress(request.getRemoteAddr());
		loginUserDetails.setBrowser(browser);

		LoginUserDetails saveDetails = saveDetailsInDB(loginUserDetails);

		if (saveDetails != null) {
			
			model.setUserLoginMetaId(saveDetails);
			redirectStrategy.sendRedirect(request, response, targetUrl);
			
		} else
			throw new RuntimeException();
	}

	public  LoginUserDetails saveDetailsInDB(LoginUserDetails loginUserDetails) {
		
		LoginUserDetails saveDetails = loginUserDetailsRepository.save(loginUserDetails);
		
		return saveDetails;
	}

	/**
	 * @param browserDetails
	 * @return browser detail of logged in user
	 */
	private String browserInformation(String browserDetails) {

		String browserInfo = browserDetails.toLowerCase();

		String browser = "";

		// ===============Browser===========================
		if (browserInfo.contains("msie")) {
			String substring = browserDetails.substring(browserDetails.indexOf("MSIE")).split(";")[0];
			browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
		} else if (browserInfo.contains("safari") && browserInfo.contains("version")) {
			browser = (browserDetails.substring(browserDetails.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
					+ (browserDetails.substring(browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
		} else if (browserInfo.contains("opr") || browserInfo.contains("opera")) {
			if (browserInfo.contains("opera"))
				browser = (browserDetails.substring(browserDetails.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
						+ (browserDetails.substring(browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
			else if (browserInfo.contains("opr"))
				browser = ((browserDetails.substring(browserDetails.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
						.replace("OPR", "Opera");
		} else if (browserInfo.contains("chrome")) {
			browser = (browserDetails.substring(browserDetails.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
		} else if ((browserInfo.indexOf("mozilla/7.0") > -1) || (browserInfo.indexOf("netscape6") != -1)
				|| (browserInfo.indexOf("mozilla/4.7") != -1) || (browserInfo.indexOf("mozilla/4.78") != -1)
				|| (browserInfo.indexOf("mozilla/4.08") != -1) || (browserInfo.indexOf("mozilla/3") != -1)) {
			// browser=(userAgent.substring(userAgent.indexOf("MSIE")).split("
			// ")[0]).replace("/", "-");
			browser = "Netscape-?";

		} else if (browserInfo.contains("firefox")) {
			browser = (browserDetails.substring(browserDetails.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
		} else if (browserInfo.contains("rv")) {
			browser = "IE";
		} else {
			browser = "UnKnown, More-Info: " + browserDetails;
		}

		return browser;

	}

	private String determineTargetUrl(Authentication authentication) {

		boolean isAdmin = false;
		boolean isEditor = false;
		// boolean isModerator = false;
		// boolean isAuthor = false;
		// boolean isContributor = false;

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		for (GrantedAuthority grantedAuthority : authorities) {

			String result = grantedAuthority.getAuthority();

			String[] values = result.split(":");

			if (values[0].equals("ADMIN")) {

				isAdmin = true;
				break;

			} else if (values[0].contentEquals("Editor")) {

				isEditor = true;
				break;
			}
			
			
			

		}

		if (isAdmin) {
			return "/adminPage";
		} else if (isEditor) {
			return "/otheruserpage";
		} else {
			throw new IllegalStateException();
		}
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

	}

	// getter setter
	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

}
