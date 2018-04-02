package com.example.controller;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.domain.LoginUserDetails;
import com.example.domain.UserModel;
import com.example.repository.LoginUserDetailsRepository;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

	@Autowired
	private LoginUserDetailsRepository loginUserDetailsRepository;

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		if (authentication != null && authentication.getDetails() != null) {
			try {

				UserModel model = (UserModel) authentication.getPrincipal();

				LoginUserDetails loginUserDetails = loginUserDetailsRepository.findByLoginMetaId(model.getUserLoginMetaId().getLoginMetaId());

				//Find current system time
				
				Timestamp timeStamp = new Timestamp(new java.util.Date().getTime());
				
				loginUserDetails.setLogoutTime(timeStamp);

				LoginUserDetails saveDetails = loginSuccessHandler.saveDetailsInDB(loginUserDetails);

				logger.info("user logged out");

				HttpSession session = request.getSession();
				session.invalidate();

				response.setStatus(HttpServletResponse.SC_OK);

				if (saveDetails != null) {

					// redirect to login
					response.sendRedirect("springcore/login");

				} else
					throw new RuntimeException();

			} catch (Exception e) {
				throw new RuntimeException();
			}
		}

	}
}
