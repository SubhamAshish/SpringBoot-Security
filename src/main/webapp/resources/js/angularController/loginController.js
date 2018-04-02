/**
 * 
 */
var myAppConstructor = angular.module("myApp",[]);
myAppConstructor.controller("loginController",function($scope, $http, $window){

	
	$scope.IsVisible=false;
	$scope.setpassIsVisible=false;
	$scope.ifOtpmatched = false;
	$scope.isOtpValidated = false;
	$scope.isInputName = false;
	
	//send otp
	$scope.sendOTP=function(forgotpassUsername){
		
		
		if(forgotpassUsername!=undefined){
			
			$scope.forgotpassUsername=forgotpassUsername;
			
			serverURL = "sendOtp?userName=" +forgotpassUsername;
			
			$http.get(serverURL).then(function(result) {
				
				console.log(result);
				
				if(result.status==200){
					
					document.getElementById('emailOtpError').innerHTML="";
					document.getElementById('emailOtpSuccess').innerHTML=result.data;
					
					$scope.IsVisible=true;
					$scope.isInputName = true;
					
				}else{
					document.getElementById('emailOtpError').innerHTML="";
					document.getElementById('emailOtpError').innerHTML=result.data;
				}
					
				
			},function(error){
				console.log(error);
			})
			
			
		}else{
			document.getElementById('emailOtpError').innerHTML="";
			document.getElementById('emailOtpError').innerHTML="please enter User Name/Email"
		}
		
		
	}
	
	
	
	//validate entered OTP
	$scope.validateOTPforforgotPass=function(forgotpassOTP){
		
		$scope.forgotpassOTP = forgotpassOTP;
		
		if($scope.forgotpassOTP.length == 4){
			
			/*document.getElementById('otpinvalid').innerHTML="";*/
			
			serverURL = "validateOtp?userName=" + $scope.forgotpassUsername+ "&otp=" + forgotpassOTP;
			
			$http.get(serverURL).then(function(result) {
				
				console.log(result.data);
				
				if(result.status==200){
					
					document.getElementById('otpverified').innerHTML=result.data;
					
					$scope.setpassIsVisible=true;
					$scope.ifOtpmatched = true;
					document.getElementById('otpinvalid').innerHTML="";
					document.getElementById('emailOtpError').innerHTML="";
					document.getElementById('emailOtpSuccess').innerHTML="";
					$scope.isOtpValidated = true;
					
				}else{
					
					document.getElementById('otpinvalid').innerHTML=result.data;
					document.getElementById('emailOtpError').innerHTML="";
					document.getElementById('emailOtpSuccess').innerHTML="";
				}
					
				
			},function(error){
				if(error.status==400){
					document.getElementById('otpinvalid').innerHTML=error.data;
					document.getElementById('emailOtpError').innerHTML="";
					document.getElementById('emailOtpSuccess').innerHTML="";
				}
				
			})
			
			
		}
		
	}
	
	
	//resend otp
	
	$scope.resendOTP = function(){
		
		$scope.setpassIsVisible=false;
		$scope.ifOtpmatched = false;
		document.getElementById('otpverified').innerHTML="";
		
		serverURL = "sendOtp?userName=" +$scope.forgotpassUsername;
		
		$http.get(serverURL).then(function(result) {
			
			
			if(result.status==200){
				
				alert("OTP SENT!");
				
				$scope.IsVisible=true;
				$scope.isOtpValidated = false;
				
				document.getElementById('otpinvalid').innerHTML="";
				document.getElementById('emailOtpError').innerHTML="";
				document.getElementById('emailOtpSuccess').innerHTML="";
				
				
				
			}else{
				
				document.getElementById('emailOtpError').innerHTML=result.data;
			}
				
			
		},function(error){
			console.log(error);
		})
		
	}
	
	
	
	//submit to generated forgot password
	
	$scope.geneartedPass = function(){
		
		serverURL = "forgotPassword?userName=" +$scope.forgotpassUsername +"&otp=" +$scope.forgotpassOTP;
		
		$http.post(serverURL).then(function(result) {
			
			//console.log(result.data[0].body)
			
			if(result.status==200){
				
				$scope.msg = result.data;
				$('#pop').modal("show");
				
			}else{
				
				document.getElementById('setpassError').innerHTML=result.data;
				
				
			}
				
			
		},function(error){
			console.log(error);
		})
		
	}
	
});






