$("#submit").click(function(){
	var validFlag = true;
	if(!validateStreetAdress())
		validFlag = false;
	if(!validateCity())
		validFlag = false;
	if(!validateState())
		validFlag = false;
	
	if(validFlag){
		$.ajax({
			type: "GET",
			url: "index.php",
			data: { streetAddress : $("#streetAddress").val(),
					city : $("#city").val(),
					state : $("#state").val()
					},
			dataType: "JSON",
			success: function(data) {
						var output = "<p class=\"error-msg pos-center\">"+data.message.text+"</p>";
						if(data.message.code == 0){
							output = "<ul id=\"mainTab\" class=\"nav nav-tabs\" role=\"tablist\">";
							output += "		<li role=\"presentation\" class=\"active\"><a id=\"basic-info-tab\" data-toggle=\"tab\" role=\"tab\" href=\"#basic-info\">Basic Info</a></li>";
							output += "		<li role=\"presentation\"><a id=\"historical-z-tab\" data-toggle=\"tab\" role=\"tab\" href=\"#historical-z\">Historical Zestimates</a></li>";
							output += "</ul>";
							
							output += "<div class=\"tab-content\">";
							
							output += "<div id=\"basic-info\" class=\"tab-pane fade active in\" role=\"tabpanel\">";
							output += "<div class=\"tab1-table-container bg-color-white\">";
							output += "<div class=\"table-responsive\"><table class=\"table table-striped\">";
							output += "<tr><td colspan=4>";
							
							var address = data.header.street+", "+data.header.city+", "+data.header.state+"-"+data.header.zipcode;
							output += "See more details for <a target=_blank href=\""+data.header.homedetails+"\" class=\"my-links\" id=\"addressHeader\">"+address+"</a> on Zillow<button name=\"fb\" id=\"fb\" class=\"btn btn-primary pull-right\" onclick=\"shareOnFB()\">Share on <b>facebook</b></button>";
							output += "</td></tr>";
							
							var useCode = data.content.useCode;
							var lastSoldPrice = (data.content.lastSoldPrice!="N/A")?"$"+data.content.lastSoldPrice:"N/A";
							output += "<tr><td>Property Type:</td><td id=\"propertyType\" class=\"col-right\">"+useCode+"</td><td>Last Sold Price:</td><td id=\"lastSoldPrice\" class=\"col-right\">"+lastSoldPrice+"</td></tr>";
							
							var yearBuilt = data.content.yearBuilt;
							var lastSoldDate = data.content.lastSoldDate;
							output += "<tr><td>Year Built:</td><td id=\"yearBuilt\" class=\"col-right\">"+yearBuilt+"</td><td>Last Sold Date:</td><td id=\"lastSoldDate\" class=\"col-right\">"+lastSoldDate+"</td></tr>";
							
							var lotSizeSqFt = (data.content.lotSizeSqFt!="N/A")?data.content.lotSizeSqFt+" sq. ft.":"N/A";
							var estimateLastUpdate = data.content.estimateLastUpdate;
							var estimateAmount = (data.content.estimateAmount!="N/A")?"$"+data.content.estimateAmount:"N/A";
							output += "<tr><td>Lot Size:</td><td id=\"lotSize\" class=\"col-right\">"+lotSizeSqFt+"</td><td>Zestimate &reg; Property Estimate as of "+estimateLastUpdate+":"+"</td><td id=\"zPropEstAmt\" class=\"col-right\">"+estimateAmount+"</td></tr>";
							
							var finishedSqFt = (data.content.finishedSqFt!="N/A")?data.content.finishedSqFt+" sq. ft.":"N/A";
							var estimateValueChangeImg = (data.content.estimateValueChangeSign=="+")?"<img src='"+data.content.imgp+"' alt=up_arrow width=10 height=14 />":((data.content.estimateValueChangeSign=="-")?"<img src='"+data.content.imgn+"' alt=down_arrow width=10 height=14 />":"");
							var estimateValueChange = (data.content.estimateValueChange!="N/A")?"$"+data.content.estimateValueChange:"N/A";
							output += "<tr><td>Finished Area:</td><td id=\"finishedArea\" class=\"col-right\">"+finishedSqFt+"</td><td>30 Days Overall Change:</td><td id=\"overallChange\" class=\"col-right\">"+estimateValueChangeImg+estimateValueChange+"</td></tr>";
							
							var bathrooms = data.content.bathrooms;
							var estimateValuationRangeLow = data.content.estimateValuationRangeLow;
							var estimateValuationRangeHigh = data.content.estimateValuationRangeHigh;
							var estimateValuationRange = "N/A";
							if(estimateValuationRangeLow!="N/A")
								if(estimateValuationRangeHigh!="N/A")
									estimateValuationRange = "$"+estimateValuationRangeLow+" - $"+estimateValuationRangeHigh;
								else
									estimateValuationRange = "$"+estimateValuationRangeLow;
							else
								if(estimateValuationRangeHigh!="N/A")
									estimateValuationRange = "$"+estimateValuationRangeHigh;
								else
									estimateValuationRange = "N/A";
							output += "<tr><td>Bathrooms:</td><td id=\"bathrooms\" class=\"col-right\">"+bathrooms+"</td><td>All Time Property Range:</td><td id=\"propRange\" class=\"col-right\">"+estimateValuationRange+"</td></tr>";
							
							var bedrooms = data.content.bedrooms;
							var restimateLastUpdate = data.content.restimateLastUpdate;
							var restimateAmount = (data.content.restimateAmount!="N/A")?"$"+data.content.restimateAmount:"N/A";
							output += "<tr><td>Bedrooms:</td><td id=\"bedrooms\" class=\"col-right\">"+bedrooms+"</td><td>Rent Zestimate &reg; Valuation as of "+restimateLastUpdate+":"+"</td><td id=\"rzPropEstAmt\" class=\"col-right\">"+restimateAmount+"</td></tr>";
							
							var taxAssessmentYear = data.content.taxAssessmentYear;
							var restimateValueChangeImg = (data.content.restimateValueChangeSign=="+")?"<img src='"+data.content.imgp+"' alt=up_arrow width=10 height=14 />":((data.content.restimateValueChangeSign=="-")?"<img src='"+data.content.imgn+"' alt=down_arrow width=10 height=14 />":"");
							var restimateValueChange = (data.content.restimateValueChange!="N/A")?"$"+data.content.restimateValueChange:"N/A";
							output += "<tr><td>Tax Assessment Year:</td><td id=\"taxAssessmentYear\" class=\"col-right\">"+taxAssessmentYear+"</td><td>30 Days Rent Change:</td><td id=\"rentChange\" class=\"col-right\">"+restimateValueChangeImg+restimateValueChange+"</td></tr>";
							
							var taxAssessment = (data.content.taxAssessment!="N/A")?"$"+data.content.taxAssessment:"N/A";
							var restimateValuationRangeLow = data.content.restimateValuationRangeLow;
							var restimateValuationRangeHigh = data.content.restimateValuationRangeHigh;
							var estimateValuationRange = "N/A";
							if(restimateValuationRangeLow!="N/A")
								if(restimateValuationRangeHigh!="N/A")
									estimateValuationRange = "$"+restimateValuationRangeLow+" - $"+restimateValuationRangeHigh;
								else
									estimateValuationRange = "$"+restimateValuationRangeLow;
							else
								if(restimateValuationRangeHigh!="N/A")
									estimateValuationRange = "$"+restimateValuationRangeHigh;
								else
									estimateValuationRange = "N/A";
							output += "<tr><td>Tax Assessment:</td><td id=\"taxAssessment\" class=\"col-right\">"+taxAssessment+"</td><td>All Time Rent Range:</td><td id=\"rentRange\" class=\"col-right\">"+estimateValuationRange+"</td></tr>";
								
							output += "</table></div>";
							output += "</div>";//<div class=\"tab1-table-container\">
							output += "</div>";//<div id=\"basic-info\" class=\"tab-pane fade active in\" role=\"tabpanel\">
							
							output += "<div id=\"historical-z\" class=\"tab-pane fade in\" role=\"tabpanel\">";
							
							output += "<div id=\"carousel-historical-charts\" class=\"carousel slide bg-color-white\" data-ride=\"carousel\">";

							output += "	<ol class=\"carousel-indicators\">";
							output += "		<li data-target=\"#carousel-historical-charts\" data-slide-to=\"0\" class=\"active\"></li>";
							output += "		<li data-target=\"#carousel-historical-charts\" data-slide-to=\"1\"></li>";
							output += "		<li data-target=\"#carousel-historical-charts\" data-slide-to=\"2\"></li>";
							output += "	</ol>";

							output += "	<div class=\"carousel-inner\" role=\"listbox\">";
							output += "		<div class=\"item active\">";
							output += "			<img id=\"oneYearChart\" class=\"chart\" src=\""+data.chart.oneYearChartURL+"\" alt=\"Historical Zestimate for the past 1 year\">";
							output += "			<div class=\"carousel-caption my-chart-caption\">";
							output += "				<h4>Historical Zestimate for the past 1 year</h4>";
							output += "				<p>"+address+"</p>";
							output += "			</div>";
							output += "		</div>";
							output += "		<div class=\"item\">";
							output += "			<img id=\"fiveYearsChart\" class=\"chart\" src=\""+data.chart.fiveYearsChartURL+"\" alt=\"Historical Zestimate for the past 5 years\">";
							output += "			<div class=\"carousel-caption my-chart-caption\">";
							output += "				<h4>Historical Zestimate for the past 5 years</h4>";
							output += "				<p>"+address+"</p>";
							output += "			</div>";
							output += "		</div>";
							output += "		<div class=\"item\">";
							output += "			<img id=\"tenYearsChart\" class=\"chart\" src=\""+data.chart.tenYearsChartURL+"\" alt=\"Historical Zestimate for the past 10 years\">";
							output += "			<div class=\"carousel-caption my-chart-caption\">";
							output += "				<h4>Historical Zestimate for the past 10 years</h4>";
							output += "				<p>"+address+"</p>";
							output += "			</div>";
							output += "		</div>";
							output += "	</div>";

							output += "	<a class=\"left carousel-control\" href=\"#carousel-historical-charts\" role=\"button\" data-slide=\"prev\">";
							output += "		<span class=\"glyphicon glyphicon-chevron-left\"></span>";
							output += "		<span class=\"sr-only\">Previous</span>";
							output += "	</a>";
							output += "	<a class=\"right carousel-control\" href=\"#carousel-historical-charts\" role=\"button\" data-slide=\"next\">";
							output += "		<span class=\"glyphicon glyphicon-chevron-right\"></span>";
							output += "		<span class=\"sr-only\">Next</span>";
							output += "	</a>";
							output += "</div>";//<div id=\"carousel-historical-charts\" class=\"carousel slide\" data-ride=\"carousel\">
							
							output += "</div>";//<div id=\"historical-z\" class=\"tab-pane fade in\" role=\"tabpanel\">
							
							output += "</div>";//<div class=\"tab-content\">
							
							output += "<p class=\"pos-center my-text-color-white\">&copy; Zillow, Inc., 2006-2014. Use is subject to <a target=_blank href=\"http://www.zillow.com/corp/Terms.htm\" class=\"my-text-color my-links\">Terms of Use</a><br/><a target=_blank href=\"http://www.zillow.com/wikipages/What-is-a-Zestimate/\" class=\"my-text-color my-links\">What's a Zestimate?</a></p>";
						}
						$("#content").html(output);
						$('#historical-z-tab').click(function (e) {
							$('.carousel').carousel();
						});
					},
			error: function(){
						$("#content").html("<p class=\"error-msg pos-center\">No exact match found -- Verify that the given address is correct.</p>");
					}
		});
	}
});