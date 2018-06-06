angular
	.module('pf-manager')
	.controller('AccountController', function($scope, $translate, AccountService, AlertService) {

	$scope.init = function() {
		$scope.user = {};
		$scope.passwordChange = {};
		$scope.tokenRequest = {};
		$scope.getUser();
		$scope.getTokens();
	};

	$scope.getUser = function() {
		AccountService.getUser(function(data) {
			$scope.user = data;
		});
	};

	$scope.saveUser = function() {
		AccountService.saveUser($scope.user, function(data) {
			$scope.user = data;
			$translate('USER_UPDATED').then(function(text) {
				AlertService.showSuccess(text);
			});
		});
	};

	$scope.changePassword = function() {
		AccountService.changePassword({ oldPassword: $scope.passwordChange.oldPassword, newPassword: $scope.passwordChange.newPassword }, function() {
			$translate('PASSWORD_UPDATED').then(function(text) {
				AlertService.showSuccess(text);
			});
			$scope.passwordChange = {};
			$scope.passwordForm.$setPristine();
			$scope.passwordForm.$setUntouched();
		});
	};

	$scope.getTokens = function() {
		AccountService.getTokens(function(data) {
			$scope.tokens = data;
		});
	};

	$scope.generateToken = function() {
		AccountService.generateToken($scope.tokenRequest, function(data) {
			$scope.generatedToken = data;
			$scope.tokenRequest = {};
			$scope.tokenForm.$setPristine();
			$scope.tokenForm.$setUntouched();
			$scope.getTokens();
		});
	};

	$scope.hideGeneratedToken = function() {
		$scope.generatedToken = undefined;
	};

	$scope.revokeToken = function(id) {
		AccountService.revokeToken({ id: id }, function() {
			$translate('TOKEN_REVOKE_SUCCESS').then(function(text) {
				AlertService.showSuccess(text);
			});
			$scope.getTokens();
		});
	};

	$scope.init();

});
