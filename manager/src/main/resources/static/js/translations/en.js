angular
	.module('pf-manager')
	.config([ '$translateProvider', function($translateProvider) {
	$translateProvider.translations('en', {

		// Account
		MY_ACCOUNT: 'My account',
		USER_UNAVAILABLE: 'User data is not available',
		USER_PROFILE: 'User profile',
		USER_USERNAME: 'Username',
		USER_FIRSTNAME: 'First name',
		USER_LASTNAME: 'Last name',
		USER_EMAIL: 'Email',
		USER_COMPANY: 'Company',
		USER_UPDATED: 'User profile successfully updated.',
		PASSWORD: 'Password',
		PASSWORD_CURRENT: 'Current password',
		PASSWORD_NEW: 'New password',
		PASSWORD_CONFIRM: 'Confirm password',
		PASSWORD_CHANGE: 'Change password',
		PASSWORD_UPDATED: 'Password successfully updated.',

		// Common
		MENU: 'Menu',
		LOADING: 'Loading...',
		SAVE: 'Save',
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
