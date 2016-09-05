<?php require_once('inc/config.inc.php'); ?>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10,IE=9" />
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
<title><?php echo $GLOBALS['portal-config']['title']; ?></title>
<link rel="stylesheet" type="text/css" href="/portal/lib/bootstrap-3.3.4/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/portal/themes/current/css/portal.css" />
<script type="text/javascript" src="/portal/toolbar.php?format=js&amp;tab=home"></script>
</head>

<body>
	<div class="container-fluid">
		<!-- Header -->
		<div id="header" class="row">
			<?php if (!empty($GLOBALS['portal-config']['logo'])) : ?>
			<h1><img src="<?php echo $GLOBALS['portal-config']['logo']; ?>" title="<?php echo $GLOBALS['portal-config']['title']; ?>" alt="<?php echo $GLOBALS['portal-config']['title']; ?>" /></h1>
			<?php else : ?>
			<h1><?php echo $GLOBALS['portal-config']['title']; ?></h1>
			<?php endif; ?>
		</div>

		<!-- Main area -->
		<div id="main" class="row">
			<div class="col-md-12">
				<div id="widgets" class="row">
					<?php foreach ($GLOBALS['portal-config']['services'] as $service) : ?>
					<?php if ($service['weight'] < 1000) : ?>
					<div class="col-xs-6 col-sm-4 col-md-3">
						<a id="widget-<?php echo $service['id'] ?>" class="widget" href="<?php echo $service['url'] ?>" data-toggle="popover" data-content="<?php echo trim(str_replace(array('"', "\n"), array("'", ''), $service['details'])) ?>">
							<h2><?php echo $service['title'] ?></h2>
							<div class="logo">
								<img src="/portal/img/services/<?php echo $service['id'] ?>.png" alt="<?php echo $service['title'] ?>" />
							</div>
							<div class="caption">
								<p><?php echo $service['description'] ?></p>
							</div>
						</a>
					</div>
					<?php endif; ?>
					<?php endforeach; ?>
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
			<p class="text-center"><a href="/portal/admin/">Outils d'administration</a></p>
		</div>
	</div>

	<script type="text/javascript" src="/portal/lib/jquery-1.11.3/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="/portal/lib/bootstrap-3.3.4/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="/portal/js/main.js"></script>
</body>

</html> 
