angular
	.module('pf-manager')
	.factory('AccountService', function($resource) {

	var accountResource = $resource('v1/account/:action', {}, {});

	return {
		getPrincipal: function(successCallback, errorCallback) {
			return accountResource.get({}, successCallback, errorCallback);
		},

		getUser: function(successCallback, errorCallback) {
			return accountResource.get({action: 'user'}, successCallback, errorCallback);
		},

		saveUser: function(user, successCallback, errorCallback) {
			return accountResource.save({action: 'user'}, user, successCallback, errorCallback);
		},

		changePassword: function(oldPassword, newPassword, successCallback, errorCallback) {
			return accountResource.save({action: 'password'}, {oldPassword: oldPassword, newPassword: newPassword}, successCallback, errorCallback);
		}
	};

});
