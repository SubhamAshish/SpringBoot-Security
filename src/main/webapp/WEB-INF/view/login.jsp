<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
 
<%--  ${pageContext.request.contextPath}/login --%>

<html>

<head>


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Login user</title>

<jsp:include page="fragments/headtags.jsp"></jsp:include>

</head>


<style>

.forgotPass {
	color: #54aef6;
	text-decoration: none;
	cursor: pointer;
}
</style>

    <body ng-app="myApp" ng-controller="loginController">
    
    <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
      <font color="red">
        Your login attempt was not successful due to <br/><br/>
        <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
      </font>
    </c:if>
    
        <h1 id="banner">Login to Security Demo</h1> 
        <form name="f" action="login" method="POST">
            <%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> --%>
            <table>
                <tr>
                    <td>Username:</td>
                    <td><input type='text' name='username' /></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type='password' name='password'></td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan='2'><input name="submit" type="submit">&nbsp;<input name="reset" type="reset"></td>
                </tr>
            </table>
		<div class="form-group">
			<h5>
				<a class="forgotPass" data-toggle="modal" data-target="#myModalForm">Forgot
					password ?</a>
			</h5>
		</div>


	</form>
	
<!-- 	forgot password modal -->
	
	<div class="modal fade" id="myModalForm" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header forgotPass-header">
					<h4 class="modal-title" id="myModalLabel">Forgot Password ?</h4>
				</div>

				<!-- Modal Body -->
				<div class="modal-body">
					<form role="form" id="myForm" method="post">
						<div class="form-group">
							<div class="col-md-12 forgotPassUsername">
								<label>Enter Your Username/Email</label>
							</div>
							
							<div class="col-md-8 col-sm-8 col-xs-8">
								<input type="text" class="form-control" id="username"
									placeholder="Enter Username/Email" ng-model="forgotpassUsername" ng-disabled="isInputName" />
									
							<div style="display: inline-block; margin-left: 10px; color: red; vertical-align: middle;"
								id="usernameError" class="error-style">
							</div>
							
						   </div>
							<button type="submit" class="btn btn-default"
								ng-click="sendOTP(forgotpassUsername)">Generate OTP
							</button>
							
							<div style="display: inline-block; margin-left: 10px; color: red; vertical-align: middle;"
								id="emailOtpError" class="error-style">
							</div>
							
							<div style="display: inline-block; margin-left: 0px; color: #46b746; vertical-align: middle;"
                                    id="emailOtpSuccess" class="error-style">
                             </div>
                                
						</div>
						
						<div class="form-group" ng-show="IsVisible">
							<div class="col-md-12 col-sm-10 forgotPassUsername">
								<label>OTP sent to your email Id</label>
							</div>
							<div class="col-md-8 col-sm-8 col-xs-8">
								<input type="password" class="form-control otpmatchedpng" id="otp" maxlength="4"
									size="4" placeholder="Enter OTP" ng-model="forgotpassOTP" ng-change="validateOTPforforgotPass(forgotpassOTP)" ng-disabled="isOtpValidated" /> 
									<span class="correctMark otpmatchedsign" ng-show="ifOtpmatched">
									<svg height="24" width="24" viewBox="0 0 24 24"
										xmlns="http://www.w3.org/2000/svg" class="">
										<path d="M9 16.2L4.8 12l-1.4 1.4L9 19 21 7l-1.4-1.4L9 16.2z"
											stroke="#388e3c">
										</path>
									</svg>
									</span>
									
								<div style="display: inline-block; margin-left: 0px; color: #46b746; vertical-align: middle;"
									id="otpverified" class="error-style">
								</div>
								
								<div style="display: inline-block; margin-left: 0px; color: red;; vertical-align: middle;"
									id="otpinvalid" class="error-style">
								</div>
								
							</div>
							<button type="submit" class="btn btn-default"
								ng-click="resendOTP()">Resend OTP</button>
						</div>
						
						<div class="form-group" ng-show="setpassIsVisible">
							<!-- <div class="col-md-12 col-sm-10 forgotPassUsername">
								<label>Set Password</label>
							</div>
							<div class="col-md-8 col-sm-8 col-xs-8">
								<input type="password" class="form-control" id="newpasswordId" 
									placeholder="Enter your new password" ng-model="newpassword" data-toggle="password" />
									
								<div style="display: inline-block; margin-left: 0px; color: red; vertical-align: middle;"
									id="setpassError" class="error-style">
								</div>
								
								<div style="display: inline-block; margin-left: 0px; color: #46b746; vertical-align: middle;"
									id="setpassSuccessmsg" class="error-style">
								</div>
								
							</div> -->
							
							<div style="display: inline-block; margin-left: 0px; color: red; vertical-align: middle;"
                                    id="setpassError" class="error-style">
                                </div>
							
							<div style="display: inline-block; margin-left: 0px; color: #46b746; vertical-align: middle;"
                                    id="setpassSuccessmsg" class="error-style">
                             </div>
							
							<button type="submit" class="btn btn-default"
							ng-click="geneartedPass()">Submit</button>
								
								
						</div>
					</form>
				</div>
				<!-- Modal Footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal" ng-click="clearInput()">
						Close</button>
				</div>
			</div>
		</div>
	</div>
	
    <!--   success modal -->	
	
	<div id="pop" class="confrirmation-modal modal fade" role="dialog" data-backdrop="static" data-keyboard="false" tabindex="-1">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-body text-center">
                    <div class="successhead"><img alt="" src="resources/images/icons/Messages_success_icon.svg" style="width: 25px;margin-top: -5px;">&nbsp; SUCCESS</div>
                    <div class="successbody"  style="color:black; text:bold;"><h4>{{msg}}</h4></div>
                    <button type="button" class="btn btn-success" data-dismiss="modal" onClick="window.location.reload();">OK</button>
                </div>
            </div>
        </div>
    </div>
	
</body>
	
<script type="text/javascript" src="resources/js/angularController/loginController.js"></script>

<script type="text/javascript" src="resources/js/angularDirective/directives.js"></script>	
	
	
   
</html>