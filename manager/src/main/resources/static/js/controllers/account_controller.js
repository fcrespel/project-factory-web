pfManagerApp.controller('AccountController', function($rootScope, $scope, $log, AccountService, AlertService) {

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
			AlertService.showSuccess('Profil utilisateur mis à jour avec succès.');
		});
	};

	$scope.changePassword = function() {
		AccountService.changePassword($scope.passwordChange.oldPassword, $scope.passwordChange.newPassword, function() {
			AlertService.showSuccess('Mot de passe mis à jour avec succès.');
			$scope.passwordChange = {};
			$scope.passwordForm.$setPristine();
			$scope.passwordForm.$setUntouched();
		});
	}

	$scope.init();

});
