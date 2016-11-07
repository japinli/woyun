function setCookie(name, value, expire) {
	var date = new Date();
	date.setDate(date.getDate() + expire);
	document.cookie = name + "=" + escape(value) + ((expire == null) ? "" : ";expires=" + date.toGMTString());
}

function getCookie(name) {
	if (document.cookie.length > 0) {
		start = document.cookie.indexOf(name + "=");
		if (start != -1) {
			start = start + name.length + 1;
			end = document.cookie.indexOf(";", start);
			if (end == -1) {
				return unescape(document.cookie.substring(start));
			} else {
				return unescape(document.cookie.substring(start, end));
			}
			
		}
	}
	
	return "";
}