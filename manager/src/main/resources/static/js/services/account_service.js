pfManagerApp.factory('AccountService', function($resource, $log) {

	var accountResource = $resource('v1/account/:action', {}, {});

	var accountService = {
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

	return accountService;

});
