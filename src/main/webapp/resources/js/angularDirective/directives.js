//fot otp
myAppConstructor.directive('onlyFourDigits', function () {

	  return {
	      restrict: 'A',
	      require: '?ngModel',
	      link: function(scope, element, attrs, ngModelCtrl) {
				if(!ngModelCtrl) {
					return; 
				}
				
				ngModelCtrl.$parsers.push(function(val) {
					if (angular.isUndefined(val)) {
						var val = '';
					}
					
					var clean = val.replace(/[^0-9]/g, '');
					if(!angular.isUndefined(clean)) {
		            	 var num=0;
		            	 if(clean.length>5 ){
		            		 num =clean.slice(0,6);
		            		 clean= num;
		            	 }
		            		 
		             }
					
					if (val !== clean) {
						ngModelCtrl.$setViewValue(clean);
						ngModelCtrl.$render();
					}
					return clean;
				});
				
				element.bind('keypress', function(event) {
					if(typeof InstallTrigger !== 'undefined'){
						if(event.charCode === 101 || event.charCode === 46 || event.charCode === 32) {
							event.preventDefault();
						}
					}
					else{
						if(event.keyCode === 101 || event.keyCode === 46 || event.keyCode === 32) {
							event.preventDefault();
						}
					}
					
				});
			}
	  };
	});



