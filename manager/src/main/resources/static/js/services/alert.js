angular
	.module('pf-manager')
	.factory('AlertService', function($rootScope) {

	return {
		hideAll: function() {
			$rootScope.$broadcast('alert.dismiss');
		},

		showSuccess: function(message) {
			$rootScope.$broadcast('alert.success', {data: {message: message}});
		},

		showInfo: function(message) {
			$rootScope.$broadcast('alert.info', {data: {message: message}});
		},

		showWarning: function(message) {
			$rootScope.$broadcast('alert.warning', {data: {message: message}});
		},

		showError: function(error) {
			$rootScope.$broadcast('alert.error', error);
		}
	};

});
