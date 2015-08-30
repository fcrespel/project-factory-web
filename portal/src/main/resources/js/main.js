$(function () {
	$('[data-toggle="popover"]').popover({
		html: true,
		trigger: 'hover focus',
		container: 'body',
		placement: 'auto left',
		delay: { show: 0, hide: 100 }
	}).on({
		'show.bs.popover': function(e) {
			var $this = $(this);
			var popover = $this.data('bs.popover');
			var $tip = $(popover.tip());
			$tip.on({
				'mouseenter': function(ev) {
					clearTimeout(popover.timeout);
				},
				'mouseleave': function(ev) {
					popover.leave(popover);
				}
			});
		}
	});
});
