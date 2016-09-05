<?php require_once('../inc/config.inc.php'); ?>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10,IE=9" />
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
<title><?php echo $GLOBALS['portal-config']['title']; ?></title>
<link rel="stylesheet" type="text/css" href="/portal/lib/bootstrap-3.3.4/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/portal/themes/current/css/portal.css" />
<script type="text/javascript" src="/portal/toolbar.php?format=js"></script>
</head>

<body>
	<div class="container">
		<!-- Main area -->
		<div id="main" class="row">
			<div class="col-sm-offset-1 col-sm-10 col-md-offset-2 col-md-8">
				<!-- Content -->
				<div id="content" class="row error">
					<?php if (!empty($GLOBALS['portal-config']['logo'])) : ?>
					<h1><img src="<?php echo $GLOBALS['portal-config']['logo']; ?>" title="<?php echo $GLOBALS['portal-config']['title']; ?>" alt="<?php echo $GLOBALS['portal-config']['title']; ?>" /></h1>
					<?php else : ?>
					<h1><?php echo $GLOBALS['portal-config']['title']; ?></h1>
					<?php endif; ?>
					<h2>404 - Page introuvable</h2>
					<p class="text-danger"><strong>La page demand&eacute;e n'existe pas ou plus.</strong></p>
					<p>Merci de v&eacute;rifier l'URL de la page ou revenir &agrave; la page pr&eacute;c&eacute;dente.</p>
				</div>
			</div>
		</div>

		<!-- Footer -->
		<div id="footer" class="row">
			<?php if (count($GLOBALS['portal-config']['contacts']) > 0) : ?>
			<ul class="list-inline text-center">
				<li>Contacts :</li>
				<?php foreach ($GLOBALS['portal-config']['contacts'] as $contact) : ?>
				<li><a href="mailto:<?php echo $contact['mail'] ?>"><?php echo $contact['name'] ?></a></li>
				<?php endforeach; ?>
			</ul>
			<?php endif; ?>
		</div>
	</div>

	<script type="text/javascript" src="/portal/lib/jquery-1.11.3/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="/portal/lib/bootstrap-3.3.4/js/bootstrap.min.js"></script>
</body>

</html> 
