angular
	.module('pf-manager')
	.controller('UserinfoController', function($scope, $cookies, AccountService) {

	$scope.init = function() {
		$scope.user = AccountService.getUser();
		$scope.csrfToken = $cookies.get('XSRF-TOKEN');
	};

	$scope.init();

});
