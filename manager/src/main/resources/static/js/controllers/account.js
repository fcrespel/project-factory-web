angular
	.module('pf-manager')
	.controller('AccountController', function($scope, $translate, AccountService, AlertService) {

	$scope.init = function() {
		$scope.user = {};
		$scope.passwordChange = {};
		$scope.getUser();
	};

	$scope.getUser = function() {
		$scope.user = AccountService.getUser();
	};

	$scope.saveUser = function() {
		$scope.user = AccountService.saveUser($scope.user, function() {
			$translate('USER_UPDATED').then(function(text) {
				AlertService.showSuccess(text);
			});
		});
	};

	$scope.changePassword = function() {
		AccountService.changePassword($scope.passwordChange.oldPassword, $scope.passwordChange.newPassword, function() {
			$translate('PASSWORD_UPDATED').then(function(text) {
				AlertService.showSuccess(text);
			});
			$scope.passwordChange = {};
			$scope.passwordForm.$setPristine();
			$scope.passwordForm.$setUntouched();
		});
	}

	$scope.init();

});
