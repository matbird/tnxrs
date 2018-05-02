$(document).ready(function(){
	setCurrentNavMenu();
});

function setCurrentNavMenu(){
	var url = location.pathname,navMenus = $(".tx-nav-menu-box a");
	if(url === '/'){
		navMenus.eq(0).addClass("tx-nav-menu-current");
	}else if(!url.indexOf('/category')){
		navMenus.eq(1).addClass("tx-nav-menu-current");
	}else if(!url.indexOf('/feedback')){
		navMenus.eq(2).addClass("tx-nav-menu-current");
	}else if(!url.indexOf('/about')){
		navMenus.eq(3).addClass("tx-nav-menu-current");
	}
}

/*
	采用问好挂参的方式为a标签追加returnUrl
*/
function appendReturnUrl(target){
	var returnUrl;
	var currentUrl = location.pathname;
	if(currentUrl.indexOf("/login") != 0 && currentUrl.indexOf("/reg") != 0){
		returnUrl = "?returnUrl="+currentUrl;
		var link = $(target);
		link.attr("href",link.attr("href")+returnUrl);
	}
}

/*
	退出登录
*/
function logout(){
	if(confirm('确定要退出登录?')){
		location.href = "/logout";
	}
}