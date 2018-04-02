<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Role Management</title>

<jsp:include page="fragments/headtags.jsp"></jsp:include>

<script type="text/javascript" src="resources/js/angularController/roleManagementController.js"></script>
</head>
<body ng-app="myApp" ng-controller="roleManagementController">

<div class="container">


<div class="container">

        <h2>Manage user:(enable/disable)</h2>
        <br>
     <div>
            <div class="col-md-3 col-xs-7 text-center tooltipBox">
            <label>selectRole:</label>
                    <div class="input-group" style="margin: auto;">
                        <input type="text" id="txtBatchTime" placeholder="Select Role"
                            class="form-control" name="role"readonly=true style="width:75%;" ng-model="selectedUserRole">

                        <div class="input-group-btn"style="position: relative; color: black;">
                        
                            <button data-toggle="dropdown" class="btn adjust-color glysize"type="button">
                                <span class="glyphicon glyphicon-menu-down" style="color: black;"></span>
                                
                            </button>
                            <ul class="dropdown-menu" role="menu">
                                <li ng-repeat="role in userList" ng-click="selectUserRole(role)"><a href="" class="" >{{role}}</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
        </div>       
          
          
            
       <br>
        <div class="container" ng-show=roleUserTable>
        <div class="col-md-12 table-responsive">
        <div class="tableMargin table-responsive"
                style="width: 100%; max-height: 450px; overflow: auto;">
            <table id="user-role" class="table table-striped sdrc-table-header-fix "
                cellspacing="0" width="100%">
                <thead>
                    <tr>
                        <th>SL. NO.</th>
                        <th>User Name</th>
                        <th>Email</th>
                    </tr>
                </thead>
                <tbody>
                
                    <tr ng-repeat="user in userDetailsList">
                    
                        <td >{{$index+1}}</td>
                        <td>{{user.userName}}</td>
                        <td>{{user.email}}</td>
                       
                        <td>
                            <a href="" ng-show=user.enabled==false ng-click="submitEnableUser(user.userId)">enable</a>
                            <a href="" ng-show=user.enabled==true ng-click="disableUser(user.userId)">disable</a>
                        </td>
                    </tr>
                    
                </tbody>
            </table>
            </div>
        
        </div>

    </div>     
            
            
</div> 

<br>
<br>




<div class="container">
                <h2>
                    <b>Add New User</b>
                </h2>
        <br>

   <form class="form-horizontal">

            <div class="form-group">
                <label class="col-md-2 col-xs-4 control-label adjust-color"
                    style="vallign: center">User Name:</label>
                <div class="col-md-3 col-xs-7">
                    <input type="text" name="first_Name" id="txtUserName"
                        class="form-control" placeholder="Enter User Name:"
                        ng-model="userName">
                </div>
            </div>


            <div class="form-group">
                <label class="col-md-2 col-xs-4 control-label adjust-color">Select
                    Role :<span class="mandatory_star">&#42;</span>
                </label>
                <div class="col-md-3 col-xs-7 text-center tooltipBox">
                    <div class="input-group" style="margin: auto;">

                        <input type="text" id="txtRole"
                            placeholder="Select Role" class="form-control"
                            name="role"
                            ng-model="selectedRole.roleName" readonly=true>

                        <div class="input-group-btn"
                            style="position: relative; color: black;">
                            <button data-toggle="dropdown" class="btn"
                                type="button">
                                <span class="glyphicon glyphicon-menu-down" style="color: black"></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                             <li ng-repeat="role in roleList"
                                    ng-click="selectRole(role)"><a href="" class="">{{role.roleName}}</a></li>
 
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

          
<!-- role based area selection -->
  <div class="form-group" ng-show=nationalField>
                <label class="col-md-2 col-xs-4 control-label adjust-color">Select
                    National :<span class="mandatory_star">&#42;</span>
                </label>
                <div class="col-md-3 col-xs-7 text-center tooltipBox">
                    <div class="input-group" style="margin: auto;">

                        <input type="text" id="txtNational"
                            placeholder="Select Country:" class="form-control"
                            name="role"
                            ng-model="selectedCountry.areaName" readonly=true>

                        <div class="input-group-btn"
                            style="position: relative; color: black;">
                            <button data-toggle="dropdown" class="btn"
                                type="button">
                                <span class="glyphicon glyphicon-menu-down" style="color: black"></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                             <li ng-repeat="country in areaMap['NATIONAL'] | filter:{parentAreaId : -1}: true "
                                    ng-click="selectCountry(country)"><a href="" class="">{{country.areaName}}</a></li>
 
                            </ul>
                        </div>
                    </div>
                </div>
      </div>



 <div class="form-group" ng-show=stateField>
                <label class="col-md-2 col-xs-4 control-label adjust-color">Select
                    State :<span class="mandatory_star">&#42;</span>
                </label>
                <div class="col-md-3 col-xs-7 text-center tooltipBox">
                    <div class="input-group" style="margin: auto;">

                        <input type="text" id="txtState"
                            placeholder="Select State" class="form-control"
                            name="role"
                            ng-model="selectedState.areaName" readonly=true>

                        <div class="input-group-btn"
                            style="position: relative; color: black;">
                            <button data-toggle="dropdown" class="btn"
                                type="button">
                                <span class="glyphicon glyphicon-menu-down" style="color: black"></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                             <li ng-repeat="state in areaMap['STATE'] | filter:{parentAreaId:selectedCountry.areaId}: true "
                                    ng-click="selectState(state)"><a href="" class="">{{state.areaName}}</a></li>
 
                            </ul>
                        </div>
                    </div>
                </div>
      </div>


    <div class="form-group" ng-show=districtField>
                <label class="col-md-2 col-xs-4 control-label adjust-color">Select
                    District :<span class="mandatory_star">&#42;</span>
                </label>
                <div class="col-md-3 col-xs-7 text-center tooltipBox">
                    <div class="input-group" style="margin: auto;">

                        <input type="text" id="txtDistrict"
                            placeholder="Select District:" class="form-control"
                            name="role"
                            ng-model="selectedDistrict.areaName" readonly=true>

                        <div class="input-group-btn"
                            style="position: relative; color: black;">
                            <button data-toggle="dropdown" class="btn"
                                type="button">
                                <span class="glyphicon glyphicon-menu-down" style="color: black"></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                             <li ng-repeat="district in areaMap['DISTRICT'] | filter:{parentAreaId:selectedState.areaId}: true "
                                    ng-click="selectDistrict(district)"><a href="" class="">{{district.areaName}}</a></li>
 
                            </ul>
                        </div>
                    </div>
                </div>
      </div>


            <div class="form-group">
                <label class="col-md-2 col-xs-4 control-label adjust-color"
                    style="vallign: center">Password:</label>
                <div class="col-md-3 col-xs-7">
                    <input type="password" name="password" id="txtPassword"
                        class="form-control" placeholder="Enter Password:"
                        ng-model="password">
                </div>
            </div>

            <div class="form-group">
                <label class="col-md-2 col-xs-4 control-label adjust-color"
                    style="vallign: center">Confirm Password:</label>
                <div class="col-md-3 col-xs-7">
                    <input type="password" name="confirm_Password"
                        id="txtConfirmPassword" class="form-control"
                        placeholder="Confirm Password:" ng-model="confirmPassword">
                </div>
            </div>

        </form>

        <div class="form-group">
            <div class="row col-md-12 col-xs-12 " align="center">
                <button type="submit" class="btn btn-success" value="SUBMIT"
                    id="txtbuttonSuccess" ng-click="submitNewUser()">SUBMIT</button>
                
            </div>
        </div>
        
        

    </div>






    <h2>Manage Role-Feature-Permission:</h2>
        
  <br><br>
        <div class="container">
        <div class="col-md-12 table-responsive">
        <div class="tableMargin table-responsive"
                style="width: 100%; max-height: 450px; overflow: auto;">
            <table id="feature-permission" class="table table-striped"
                cellspacing="0" width="100%">
                <thead>
                    <tr>
                        <th>SL. NO.</th>
                        <th>RoleName</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="role in roleList">
                        <td >{{$index+1}}</td>
                        <td>{{role.roleName}}</td>
                        <td><a href="" ng-click=editRole(role.roleId,role.roleName)>edit</a> <span>|</span>
                            <a href=""  ng-show=role.enable==false ng-click=enableRole(role.roleId)>enable</a>
                            <a href=""  ng-show=role.enable==true ng-click=disableRole(role.roleId)>disable</a>
                        </td>
                    </tr>
                </tbody>
            </table>
            </div>
        
        </div>

    </div>

</div>



<div class="container" ng-show=editable>

    <h2>Information:</h2>
    <label>Role name:</label>    
   <input type=text ng-model="selectedRoleName" value="selectedRoleName" ng-disabled=true> 
       
  <br><br>
        <div class="container">
        <div class="col-md-12 table-responsive">
        <div class="tableMargin table-responsive"
                style="width: 100%; max-height: 450px; overflow: auto;">
            <table id="feature-permission" class="table table-striped"
                cellspacing="0" width="100%">
                <thead>
                    <tr>
                        <th>SL. NO.</th>
                        <th>Feature</th>
                        <th>Permission</th>
                        <th>Action</th>
                    </tr>
                </thead>
               
               <tbody>
                    <tr ng-repeat="(k,v) in featurePermissionList">
                    
                        <td >{{$index+1}}</td>
                        <td>{{v[0].featurePermission.feature.featureName}}</td>
                        <td>{{v[0].featurePermission.permission.permissionName}}</td>
                        
                        <td>
                            <a href="" ng-click=enableFeaturePermission(k) ng-show="v[0].enable==false">enable</a>
                            <a href=""  ng-click=disableFeaturePermission(k) ng-show="v[0].live==true && v[0].enable==true">disable</a>
                            
                            <button ng-show="v[0].live==false &&v[0].enable==true || v[0].live==false && v[0].enable==false" ng-click=
                            addFeaturePermission(v[0].featurePermission.featurePermissionId)>Add</button>
                            
                        </td>
                        
                        
                        
                    </tr>
                </tbody>
            </table> 
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
</html>