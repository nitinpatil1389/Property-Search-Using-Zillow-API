function shareOnFB(){
var sign="";
if($("#overallChange img").attr("src")){
	if($("#overallChange img").attr("src").match(/down/))
		sign="-";
	else if($("#overallChange img").attr("src").match(/up/))
		sign="+";
}
if($("#oneYearChart").attr("src"))
	FB.ui({
			'method': 'feed',
			'name': $("#addressHeader").html(),
			'link': $("#addressHeader").attr("href"),
			'picture': $("#oneYearChart").attr("src"),
			'caption': 'Property information from Zillow.com',
			'description': 'Last Sold Price: '+$("#lastSoldPrice").html()+',  30 Days Overall Change: '+sign+$("#overallChange").text()
		}, function(response) {
			if (response && !response.error_code) {
				alert('Posted Successfully');
			} else {
				alert('Error while posting');
			}
		});
else
	FB.ui({
			'method': 'feed',
			'name': $("#addressHeader").html(),
			'link': $("#addressHeader").attr("href"),
			'caption': 'Property information from Zillow.com',
			'description': 'Last Sold Price: '+$("#lastSoldPrice").html()+',  30 Days Overall Change: '+sign+$("#overallChange").text()
		}, function(response) {
			if (response && !response.error_code) {
				alert('Posted Successfully');
			} else {
				alert('Error while posting');
			}
		});	
}

window.fbAsyncInit = function() {
	FB.init({
	  appId      : '1545630032345144',
	  version    : 'v2.2'
	});
};

(function(d, s, id){
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id)) {
		return;
	}
	js = d.createElement(s); 
	js.id = id;
	js.src = "//connect.facebook.net/en_US/sdk.js";
	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));