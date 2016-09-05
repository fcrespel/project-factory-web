<?php ob_start(); ?>
<?php require_once('inc/config.inc.php'); ?>
<?php if ($GLOBALS['portal-config']['format'] == 'html') : ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,IE=11,IE=10,IE=9" />
<meta charset="UTF-8" />
<title><?php echo $GLOBALS['portal-config']['title']; ?> Toolbar</title>
<link rel="stylesheet" type="text/css" href="/portal/themes/current/css/portal.css" />
</head>

<body>
<div id="platform-toolbar-wrapper">
<?php endif; ?>
	<div id="platform-toolbar">
		<!-- Platform title -->
		<?php if (!empty($GLOBALS['portal-config']['logo_toolbar'])) : ?>
		<h1><a href="/"><img src="<?php echo $GLOBALS['portal-config']['logo_toolbar']; ?>" title="<?php echo $GLOBALS['portal-config']['title']; ?>" alt="<?php echo $GLOBALS['portal-config']['title']; ?>" /></a></h1>
		<?php else : ?>
		<h1><a href="/"><?php echo $GLOBALS['portal-config']['title']; ?></a></h1>
		<?php endif; ?>

		<!-- Service tabs -->
		<ul class="tabs">
			<?php $currentTab = isset($_GET['tab']) ? $_GET['tab'] : ''; ?>
			<li id="home" class="<?php echo ('home' == $currentTab) ? 'active' : '' ?>">
				<a href="/portal/" title="Portail" target="_top">Portail</a>
			</li>
			<?php if ('admin' == $currentTab || in_array($currentTab, array_keys($GLOBALS['portal-config']['admintools']))) : ?>
			<?php foreach ($GLOBALS['portal-config']['admintools'] as $admintool) : ?>
			<li id="<?php echo $admintool['id'] ?>" class="<?php echo ($admintool['id'] == $currentTab) ? 'active' : '' ?>">
				<a href="<?php echo $admintool['url'] ?>" title="<?php echo $admintool['description'] ?>" target="_top"><?php echo $admintool['title'] ?></a>
			</li>
			<?php endforeach; ?>
			<?php else : ?>
			<?php foreach ($GLOBALS['portal-config']['services'] as $service) : ?>
			<li id="<?php echo $service['id'] ?>" class="<?php echo ($service['id'] == $currentTab) ? 'active' : '' ?>">
				<a href="<?php echo $service['url'] ?>" title="<?php echo $service['description'] ?>" target="_top"><?php echo $service['title'] ?></a>
			</li>
			<?php endforeach; ?>
			<?php endif; ?>
		</ul>
	
		<?php if ($GLOBALS['portal-config']['serviceMessage']['enabled']) : ?>
		<!-- Service message -->
		<div id="service-message" class="<?php echo $GLOBALS['portal-config']['serviceMessage']['type']; ?>">
			<span title="<?php echo $GLOBALS['portal-config']['serviceMessage']['detail']; ?>">
				<?php echo $GLOBALS['portal-config']['serviceMessage']['title']; ?>
			</span>
		</div>
		<?php endif; ?>
		
		<!--[if lt IE 9]>
		<div id="service-message" class="critical">
			<span title="Vous utilisez une version obsolète d'Internet Explorer. Pour profiter d'une navigation plus rapide et plus sécurisée, effectuez une mise à niveau dès aujourd'hui.">
				Cette version d'Internet Explorer n'est plus supportée. <a href="http://windows.microsoft.com/fr-FR/internet-explorer/products/ie/home" target="_top">Mettre à jour</a>
			</span>
		</div>
		<![endif]--> 
	
		<div style="clear: both;"></div>
	</div>
<?php if ($GLOBALS['portal-config']['format'] == 'html') : ?>
</div>
</body>

</html>
<?php endif; ?>
<?php
switch ($GLOBALS['portal-config']['format']) {
	case 'javascript':
		$output = ob_get_contents();
		$outputJSON = json_encode(utf8_encode($output));
		ob_end_clean();
		header('Content-Type: text/javascript; charset=UTF-8');
		echo <<<EOL
function add_platform_toolbar() {
	if (document.body) {
		var wrapperId = 'platform-toolbar-wrapper';
		var wrapper = document.getElementById(wrapperId);
		if (wrapper == null) {
			wrapper = document.createElement('div');
			wrapper.id = wrapperId;
			wrapper.innerHTML = $outputJSON;
			document.body.insertBefore(wrapper, document.body.firstChild);
		}
	} else {
		setTimeout(add_platform_toolbar, 10);
	}
}
add_platform_toolbar();
EOL;
		break;
	case 'json':
		$output = ob_get_contents();
		$outputJSON = json_encode(utf8_encode($output));
		ob_end_clean();
		header('Content-Type: application/json; charset=UTF-8');
		echo $outputJSON;
		break;
	default:
		ob_end_flush();
		break;
}
?>