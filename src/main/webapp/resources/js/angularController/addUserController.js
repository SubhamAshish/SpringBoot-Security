/**
 * 
 */

angular.module('myApp',[]).controller('addUserController',function($scope,$http){
	
	
$scope.roleUserTable=false;
$scope.nationalField = false;
$scope.stateField = false
$scope.districtField=false;
	
$http.get('allRole').then(function(result){
		
		$scope.roleList = result.data;
		
	},function(error){
		console.log(error);
		
	});
	
//selected role while adding new user
$scope.selectRole = function(role){
	
	$scope.selectedRole = role;
	
	//national
	if($scope.selectedRole.roleId==1){
		
		$scope.nationalField = true;
		$scope.stateField = false;
		$scope.districtField=false;
	}
	
	//state
	if($scope.selectedRole.roleId==2){
		
		$scope.nationalField = true;
		$scope.stateField = true
		$scope.districtField=false;
	}
	
	//district
	if($scope.selectedRole.roleId==3){
		
		$scope.nationalField = true;
		$scope.stateField = true
		$scope.districtField=true;
	}

	
	$scope.areaList = $scope.areaMap[$scope.selectedRole.roleName];
}


//role based users
$http.get('allUser').then(function(result){
	
	$scope.userDetails = result.data;
	$scope.userList = Object.keys(result.data);
	
},function(error){
	console.log(error);
	
});


//selectedrole from userList
$scope.selectUserRole=function(role){

	$scope.userDetailsList = [];
	
	$scope.selectedUserRole=role;
	
	angular.forEach($scope.userDetails, function(value, key){
		if(key == role){
			for(var i=0; i<value.length; i++){
				
				$scope.userDetailsList.push(value[i]);
			}
		}
	});
	
	
	$scope.roleUserTable=true;
}



$scope.selectUser = function(user){
	
	$scope.selectedUser = user;
	
}

//country selection
$scope.selectCountry = function(country){
	
	$scope.selectedCountry = country;
	$scope.selectedState=undefined;
	$scope.selectedDistrict=undefined;
	
}

//state selection
$scope.selectState = function(state){
	
	$scope.selectedState = state;
	$scope.selectedDistrict=undefined;
	
	
}

//district selection
$scope.selectDistrict = function(district){
	
	$scope.selectedDistrict = district;
	
}


//enable user
$scope.submitEnableUser=function(userId){
	
	serverURL = "enableUser?userId=" +userId;
	
	$http.post(serverURL).then(function(result) {
		
		console.log(result.data)
		
	},function(error){
		console.log(error);
	})
	
}

//disable user
$scope.disableUser = function(userId){
	
	serverURL = "disableUser?userId=" +userId;
	
	$http.post(serverURL).then(function(result) {
		
		console.log(result.data)
		if(result.data=="already disable")
			alert("user is already disabled");
		else
			alert("Successfull");
	
	},function(error){
		console.log(error);
	})
	
	
}


//load area
$http.get('getAllArea').then(function(result){
	
	$scope.areaMap = result.data;
	
},function(error){
	console.log(error);
	
});




//submit new user button
$scope.submitNewUser=function(){
	
	
	var password = document.getElementById("txtPassword").value;
	var reTypePassword = document.getElementById("txtConfirmPassword").value;
	
	if($scope.userName == undefined){
		
		alert("username cannot be empty!")
		
	}else if($scope.password == undefined || $scope.confirmPassword == undefined){
		alert("password or confirmpassword is blank!")
	}else if(!(password == reTypePassword)){
		alert("password and confirm password is not same!")
	}else if($scope.selectedRole == undefined){
		alert("please select role")
	}else{
		
		serverURL = "addUser?userName=" + $scope.userName
		+ "&password=" + $scope.password + "&roleId=" + $scope.selectedRole.roleId;
		
		
		$http.post(serverURL).then(function(result) {
			
			console.log(result.data)
		})
		
	}
	
}

	
});