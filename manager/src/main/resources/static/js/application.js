'use strict';

var pfManagerApp = angular.module('pf-manager', [ 'ngMessages', 'ngResource', 'ngRoute', 'ui.bootstrap' ]);

pfManagerApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/account', {
		templateUrl : './views/account.html',
		controller : 'AccountController'
	}).
	otherwise({
		redirectTo : '/account'
	});
} ]);

pfManagerApp.config([ '$httpProvider', function($httpProvider) {
	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	
	$httpProvider.interceptors.push(function($q, $rootScope) {
		return {
			'request': function(config) {
				$rootScope.$broadcast('alert.dismiss');
				return config;
			},
			
			'responseError': function(rejection) {
				$rootScope.$broadcast('alert.error', rejection);
				return $q.reject(rejection);
			}
		};
	});
	
	$httpProvider.interceptors.push(function($q, $rootScope, $injector) {
		return {
			'request': function(config) {
				$rootScope.isLoading = true;
				$('#loadingWidget').fadeIn("fast");
				return config;
			},
			
			'requestError': function(rejection) {
				var $http = $http || $injector.get('$http');
				if ($http.pendingRequests.length < 1) {
					$rootScope.isLoading = false;
					$('#loadingWidget').fadeOut("fast");
				}
				return $q.reject(rejection);
			},
			
			'response': function(response) {
				var $http = $http || $injector.get('$http');
				if ($http.pendingRequests.length < 1) {
					$rootScope.isLoading = false;
					$('#loadingWidget').fadeOut("fast");
				}
				return response;
			},
			
			'responseError': function(rejection) {
				var $http = $http || $injector.get('$http');
				if ($http.pendingRequests.length < 1) {
					$rootScope.isLoading = false;
					$('#loadingWidget').fadeOut("fast");
				}
				return $q.reject(rejection);
			}
		};
	});
} ]);

pfManagerApp.directive('compareTo', function() {
	return {
		require: "ngModel",
		scope: {
			otherModelValue: "=compareTo"
		},
		link: function(scope, element, attributes, ngModel) {
			ngModel.$validators.compareTo = function(modelValue) {
				return modelValue == scope.otherModelValue;
			};
			scope.$watch("otherModelValue", function() {
				ngModel.$validate();
			});
		}
	};
});
