angular
	.module('pf-manager')
	.config([ '$translateProvider', function($translateProvider) {
	$translateProvider.translations('fr', {

		// Account
		MY_ACCOUNT: 'Mon compte',
		// -- User profile
		USER_UNAVAILABLE: 'Données utilisateur indisponibles',
		USER_PROFILE: 'Profil utilisateur',
		USER_USERNAME: 'Identifiant',
		USER_FIRSTNAME: 'Prénom',
		USER_LASTNAME: 'Nom',
		USER_EMAIL: 'Adresse e-mail',
		USER_COMPANY: 'Société',
		USER_UPDATED: 'Profil utilisateur mis à jour avec succès.',
		// -- Password change
		PASSWORD: 'Mot de passe',
		PASSWORD_CURRENT: 'Mot de passe actuel',
		PASSWORD_NEW: 'Nouveau mot de passe',
		PASSWORD_CONFIRM: 'Confirmation du mot de passe',
		PASSWORD_CHANGE: 'Modifier le mot de passe',
		PASSWORD_UPDATED: 'Mot de passe mis à jour avec succès.',
		// -- Tokens
		TOKENS: 'Tokens applicatifs',
		TOKEN_INFO: 'Les tokens permettent aux applications et scripts externes de s\'authentifier avec un mot de passe dédié, avec un délai d\'expiration optionnel.',
		TOKEN_COMMENTS: 'Description',
		TOKEN_CREATED_AT: 'Date de création',
		TOKEN_EXPIRES_AT: 'Date d\'expiration',
		TOKEN_REQUEST: 'Obtenir un token :',
		TOKEN_REQUEST_COMMENTS: 'Description (obligatoire)',
		TOKEN_REQUEST_DURATION: 'Durée (secondes)',
		TOKEN_GENERATE: 'Générer un token',
		TOKEN_GENERATE_SUCCESS: 'Token généré avec succès : <strong>{{token}}</strong> &ndash; Veuillez le copier immédiatement, car il ne sera plus affiché par la suite.',
		TOKEN_REVOKE: 'Révoquer le token',
		TOKEN_REVOKE_SUCCESS: 'Token révoqué avec succès.',

		// Common
		MENU: 'Menu',
		LOADING: 'Chargement...',
		SAVE: 'Enregistrer',
		ACTIONS: 'Actions',
		UNKNOWN: 'Inconnu(e)',
		NEVER: 'Jamais',
		LOGGED_AS: 'Connecté en tant que <strong><a href="account" title="{{displayName}}">{{userName}}</a></strong>',
		LOGOUT: 'Déconnexion',

		// Errors
		ERROR: 'ERREUR',
		ERROR_CODE: 'code',
		ERROR_UNKNOWN: 'Une erreur inconnue est survenue. Veuillez actualiser la page et renouveler votre tentative.',
		ERROR_REQUIRED: 'Ce champ est requis',
		ERROR_EMAIL: 'Adresse e-mail invalide',
		ERROR_PASSWORD_MATCH: 'Les mots de passe ne correspondent pas',
		ERROR_RETRY: 'Cliquez ici pour réessayer',

	});
}]);
