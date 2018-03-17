angular
	// Main app
	.module('pf-manager', [
		'ngMessages', 'ngResource', 'ngRoute',
		'ui.bootstrap',
		'cgBusy'
	])

	// Routes config
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/account', {
			templateUrl : './views/account.html',
			controller : 'AccountController'
		}).
		otherwise({
			redirectTo : '/account'
		});
	} ])

	// Compiler config
	.config([ '$compileProvider', function ($compileProvider) {
		$compileProvider.debugInfoEnabled(false);
		$compileProvider.commentDirectivesEnabled(false);
	} ])

	// HTTP config
	.config([ '$httpProvider', function($httpProvider) {
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

		$httpProvider.interceptors.push(function($q, $rootScope) {
			return {
				'request': function(config) {
					var defer = $q.defer();
					config.defer = defer;
					$rootScope.activePromises = $rootScope.activePromises || [];
					$rootScope.activePromises.push(defer.promise);
					defer.promise.finally(function() {
						angular.forEach($rootScope.activePromises, function(promise, index) {
							if (promise === defer.promise) {
								$rootScope.activePromises.splice(index, 1);
							}
						});
					});
					return config;
				},

				'requestError': function(rejection) {
					if (rejection.config && rejection.config.defer) {
						rejection.config.defer.reject();
					}
					return $q.reject(rejection);
				},

				'response': function(response) {
					if (response.config && response.config.defer) {
						response.config.defer.resolve();
					}
					return response;
				},

				'responseError': function(rejection) {
					if (rejection.config && rejection.config.defer) {
						rejection.config.defer.reject();
					}
					return $q.reject(rejection);
				}
			};
		});
	} ])

	// Busy indicator config
	.value('cgBusyDefaults', {
		backdrop: true,
		templateUrl: 'loading.html',
		delay: 200,
		minDuration: 100
	});
