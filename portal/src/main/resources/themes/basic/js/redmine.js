/**
 * Redmine JavaScript customizations
 * By Fabien Crespel <fabien@crespel.net>
 * Last Updated: 2013-01-17
 *
 * Note: JQuery is required for this to work
 */

function createSubmenu() {
    var menuList = jQuery("#main-menu > ul");
    var submenu = jQuery('<li class="submenu"><a class="menu" href="#"><img src="/redmine/images/arrow_expanded.png" /></a><ul></ul></li>');
    menuList.append(submenu);
}

function menuResize() {
    var menuList = jQuery("#main-menu > ul");
    var menuItems = menuList.find("> li");
    var menuWidth = menuList.innerWidth();
    var submenu = menuList.find(".submenu");
    var submenuWidth = submenu.outerWidth(true);
    var submenuList = submenu.find("ul");
    var submenuItems = submenuList.find("li");
    var submenuHasItems = submenuItems.length > 0;
    var totalWidth = submenuHasItems ? submenuWidth : 0;
    var splitFromIndex = 0;
    
    for (var i = 0; i < menuItems.length - 1; i++) {
        var menuItem = jQuery(menuItems[i]);
        var menuItemWidth = menuItem.outerWidth(true);
        menuItem.data("width", menuItemWidth);
        totalWidth += menuItemWidth;
        if (totalWidth > menuWidth) {
            totalWidth -= menuItemWidth;
            if (totalWidth + submenuWidth < menuWidth) {
                splitFromIndex = i;
            } else {
                splitFromIndex = i-1;
                totalWidth -= jQuery(menuItems[i-1]).data("width");
            }
            break;
        }
    }
    if (splitFromIndex > 0) {
        for (i = splitFromIndex; i < menuItems.length - 1; i++) {
            jQuery(menuItems[i])[submenuHasItems ? "prependTo" : "appendTo"](submenuList);
        }
        submenu.addClass("shown");
    } else if (submenuHasItems) {
        totalWidth -= submenuWidth;
        for (i = 0; i < submenuItems.length; i++){
            totalWidth += jQuery(submenuItems[i]).data("width");
            if (totalWidth + submenuWidth < menuWidth || i == submenuItems.length-1 && totalWidth < menuWidth) {
                jQuery(submenuItems[i]).insertBefore(submenu);
                i == submenuItems.length-1 && submenu.removeClass("shown");
            } else {
                break;
            }
        }
    }
}

jQuery(document).ready(function() {
    createSubmenu();
    menuResize();
    jQuery(window).resize(menuResize);
});
