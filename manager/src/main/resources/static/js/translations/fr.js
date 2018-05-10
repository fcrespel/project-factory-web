angular
	.module('pf-manager')
	.config([ '$translateProvider', function($translateProvider) {
	$translateProvider.translations('fr', {

		// Account
		MY_ACCOUNT: 'Mon compte',
		USER_UNAVAILABLE: 'Données utilisateur indisponibles',
		USER_PROFILE: 'Profil utilisateur',
		USER_USERNAME: 'Identifiant',
		USER_FIRSTNAME: 'Prénom',
		USER_LASTNAME: 'Nom',
		USER_EMAIL: 'Adresse e-mail',
		USER_COMPANY: 'Société',
		USER_UPDATED: 'Profil utilisateur mis à jour avec succès.',
		PASSWORD: 'Mot de passe',
		PASSWORD_CURRENT: 'Mot de passe actuel',
		PASSWORD_NEW: 'Nouveau mot de passe',
		PASSWORD_CONFIRM: 'Confirmation du mot de passe',
		PASSWORD_CHANGE: 'Modifier le mot de passe',
		PASSWORD_UPDATED: 'Mot de passe mis à jour avec succès.',

		// Common
		MENU: 'Menu',
		LOADING: 'Chargement...',
		SAVE: 'Enregistrer',
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
