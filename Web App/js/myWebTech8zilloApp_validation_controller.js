function validateStreetAdress(){
	var streetAddress = $("#streetAddress").val();
	if(streetAddress=="" || streetAddress==null){
		$("#streetAddress").parent().addClass("has-error");
		$("#streetAddressMsg").css( "visibility", "visible" );
		$("#streetAddress").focus();
		return false;
	}
	else{
		$("#streetAddress").parent().removeClass("has-error");
		$("#streetAddressMsg").css( "visibility", "hidden" );
		return true;
	}
}

function validateCity(){
	var city = $("#city").val();
	if(city=="" || city==null){
		$("#city").parent().addClass("has-error");
		$("#cityMsg").css( "visibility", "visible" );
		$("#city").focus();		
		return false;
	}
	else{
		$("#city").parent().removeClass("has-error");
		$("#cityMsg").css( "visibility", "hidden" );
		return true;
	}
}

function validateState(){
	var state = $("#state").val();
	if(state=="" || state==null){
		$("#state").parent().addClass("has-error");
		$("#stateMsg").css( "visibility", "visible" );
		$("#state").focus();
		return false;
	}
	else{
		$("#state").parent().removeClass("has-error");
		$("#stateMsg").css( "visibility", "hidden" );
		return true;
	}
}

$("#streetAddress").keyup(function(){
	validateStreetAdress();
});

$("#city").keyup(function(){
	validateCity();
});

$("#state").keyup(function(){
	validateState();
});

$("#streetAddress").change(function(){
	validateStreetAdress();
});

$("#city").change(function(){
	validateCity();
});

$("#state").change(function(){
	validateState();
});