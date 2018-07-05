angular
	.module('pf-manager')
	.factory('AccountService', function($resource) {

	return $resource('v1/account/:action', {}, {
		'getPrincipal': { method: 'GET', params: { action: 'principal' } },
		'getUser': { method: 'GET', params: { action: 'user' } },
		'saveUser': { method: 'POST', params: { action: 'user' } },
		'changePassword': { method: 'POST', params: { action: 'password' } },
		'getTokens': { method: 'GET', isArray: true, params: { action: 'tokens' } },
		'generateToken': { method: 'POST', params: { action: 'tokens' } },
		'revokeToken': { method: 'DELETE', params: { action: 'tokens' }, url: 'v1/account/:action/:id' }
	});

});
