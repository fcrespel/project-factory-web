angular
	.module('pf-manager')
	.config([ '$translateProvider', function($translateProvider) {
	$translateProvider.translations('en', {

		// Account
		MY_ACCOUNT: 'My account',
		// -- User profile
		USER_UNAVAILABLE: 'User data is not available',
		USER_PROFILE: 'User profile',
		USER_USERNAME: 'Username',
		USER_FIRSTNAME: 'First name',
		USER_LASTNAME: 'Last name',
		USER_EMAIL: 'Email',
		USER_COMPANY: 'Company',
		USER_UPDATED: 'User profile successfully updated.',
		// -- Password change
		PASSWORD: 'Password',
		PASSWORD_CURRENT: 'Current password',
		PASSWORD_NEW: 'New password',
		PASSWORD_CONFIRM: 'Confirm password',
		PASSWORD_CHANGE: 'Change password',
		PASSWORD_UPDATED: 'Password successfully updated.',
		// -- Tokens
		TOKENS: 'Application tokens',
		TOKEN_INFO: 'Tokens allows external applications and scripted clients to authenticate using a dedicated password, that may optionally expire after a delay.',
		TOKEN_COMMENTS: 'Description',
		TOKEN_CREATED_AT: 'Creation date',
		TOKEN_EXPIRES_AT: 'Expiration date',
		TOKEN_REQUEST: 'Token request:',
		TOKEN_REQUEST_COMMENTS: 'Description (required)',
		TOKEN_REQUEST_DURATION: 'Duration (seconds)',
		TOKEN_GENERATE: 'Generate token',
		TOKEN_GENERATE_SUCCESS: 'Token successfully generated: <strong>{{token}}</strong> &ndash; Please copy it immediately, as it will not be displayed again.',
		TOKEN_REVOKE: 'Revoke token',
		TOKEN_REVOKE_SUCCESS: 'Token successfully revoked.',

		// Common
		MENU: 'Menu',
		LOADING: 'Loading...',
		SAVE: 'Save',
		ACTIONS: 'Actions',
		UNKNOWN: 'Unknown',
		NEVER: 'Never',
		LOGGED_AS: 'Logged in as <strong><a href="account" title="{{displayName}}">{{userName}}</a></strong>',
		LOGOUT: 'Logout',

		// Errors
		ERROR: 'ERROR',
		ERROR_CODE: 'code',
		ERROR_UNKNOWN: 'An unknown error occurred. Please refresh the page and try again.',
		ERROR_REQUIRED: 'This field is required',
		ERROR_EMAIL: 'Invalid email address',
		ERROR_PASSWORD_MATCH: 'Passwords do not match',
		ERROR_RETRY: 'Click here to try again',

	});
}]);
