pfManagerApp.controller('AlertController', function($scope, $log) {

	$scope.init = function() {
		$('#alerts .alert .close').click(function() {
			$(this).parent().addClass('hide');
		});
	};

	$scope.hideAll = function(success) {
		$('#alerts .alert').addClass('hide');
	}

	$scope.showSuccess = function(success) {
		$scope.success = success;
		$("#alert-success").removeClass('hide');
	};

	$scope.showInfo = function(info) {
		$scope.info = info;
		$("#alert-info").removeClass('hide');
	};

	$scope.showWarning = function(warning) {
		$scope.warning = warning;
		$("#alert-warning").removeClass('hide');
	};

	$scope.showError = function(error) {
		$scope.error = error;
		$("#alert-error").removeClass('hide');
	};

	$scope.$on('alert.dismiss', function(event) {
		$scope.hideAll();
	});

	$scope.$on('alert.success', function(event, success) {
		$scope.showSuccess(success);
	});

	$scope.$on('alert.info', function(event, info) {
		$scope.showInfo(info);
	});

	$scope.$on('alert.warning', function(event, warning) {
		$scope.showWarning(warning);
	});

	$scope.$on('alert.error', function(event, error) {
		$scope.showError(error);
	});

	$scope.init();

});
