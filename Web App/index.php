<?php
	/**
	 * User: Nitin
	 * Date: 06/11/2014
	 * Time: 22:19
	 */
	date_default_timezone_set("America/Los_Angeles");

	$streetAddress=urlencode($_GET["streetAddress"]);
	$city=urlencode($_GET["city"]);
	$state=urlencode($_GET["state"]);
	$url = "http://www.zillow.com/webservice/GetDeepSearchResults.htm?zws-id=X1-ZWz1b2re0bhix7_2avji&address=".$streetAddress."&citystatezip=".$city."+". $state."&rentzestimate=true";

	$searchResults=simplexml_load_file($url);

	//message code and message text
	$msg_code = $searchResults->message->code;
	$msg_text = $searchResults->message->text;

	if($searchResults->message->code == 0){
		$result=$searchResults->response->results->result;
		
		$homedetails = $result->links->homedetails;
		$street = $result->address->street;
		$city = $result->address->city;
		$state = $result->address->state;
		$zipcode = $result->address->zipcode;
		
		$useCode="N/A";
		if(isset($result->useCode) &&  $result->useCode!="")
			$useCode=$result->useCode;			

		$lastSoldPrice="N/A";
		if(isset($result->lastSoldPrice) &&  $result->lastSoldPrice!="")
			$lastSoldPrice=number_format((float)$result->lastSoldPrice,2,".",",");

		$yearBuilt="N/A";
		if(isset($result->yearBuilt))
			$yearBuilt=$result->yearBuilt;

		$lastSoldDate="N/A";
		if(isset($result->lastSoldDate) &&  $result->lastSoldDate!="")
			$lastSoldDate=date("j-M-Y", strtotime($result->lastSoldDate));

		$lotSizeSqFt="N/A";
		if(isset($result->lotSizeSqFt) &&  $result->lotSizeSqFt!="")
			$lotSizeSqFt=number_format((float)$result->lotSizeSqFt,0,"",",");

		$estimateLastUpdate="N/A";
		if(isset($result->zestimate->{'last-updated'}) &&  $result->zestimate->{'last-updated'}!="")
			$estimateLastUpdate=date("j-M-Y", strtotime($result->zestimate->{'last-updated'}));
		
		$estimateAmount="N/A";
		if(isset($result->zestimate->amount) && $result->zestimate->amount!="")
			$estimateAmount=number_format((float)$result->zestimate->amount,2,".",",");

		$finishedSqFt="N/A";
		if(isset($result->finishedSqFt) &&  $result->finishedSqFt!="")
			$finishedSqFt=number_format((float)$result->finishedSqFt,0,"",",");

		$estimateValueChangeSign="N/A";
		$estimateValueChange="N/A";
		if(isset($result->zestimate->valueChange) &&  $result->zestimate->valueChange!=""){
			$estimateValueChange=str_replace('-', '', number_format((float)$result->zestimate->valueChange,2,".",","));
			if($result->zestimate->valueChange>0)
				$estimateValueChangeSign="+";
			else if($result->zestimate->valueChange<0)
				$estimateValueChangeSign="-";
		}

		$bathrooms="N/A";
		if(isset($result->bathrooms))
			$bathrooms=$result->bathrooms;

		$estimateValuationRangeLow="N/A";
		if(isset($result->zestimate->valuationRange->low) && $result->zestimate->valuationRange->low!="")
			$estimateValuationRangeLow=number_format((float)$result->zestimate->valuationRange->low,2,".",",");
			
		$estimateValuationRangeHigh="N/A";
		if(isset($result->zestimate->valuationRange->high) && $result->zestimate->valuationRange->high!="")
			$estimateValuationRangeHigh=number_format((float)$result->zestimate->valuationRange->high,2,".",",");

		$bedrooms="N/A";
		if(isset($result->bedrooms))
			$bedrooms=$result->bedrooms;

		$restimateLastUpdate="N/A";
		if(isset($result->rentzestimate->{'last-updated'}) && $result->rentzestimate->{'last-updated'}!="")
			$restimateLastUpdate=date("j-M-Y", strtotime($result->rentzestimate->{'last-updated'}));
			
		$restimateAmount="N/A";
		if(isset($result->rentzestimate->amount) && $result->rentzestimate->amount!="")
			$restimateAmount=number_format((float)$result->rentzestimate->amount,2,".",",");

		$taxAssessmentYear="N/A";
		if(isset($result->taxAssessmentYear))
			$taxAssessmentYear=$result->taxAssessmentYear;

		$restimateValueChange="N/A";
		$restimateValueChangeSign="N/A";
		if(isset($result->rentzestimate->valueChange) && $result->rentzestimate->valueChange!=""){
			$restimateValueChange=str_replace('-', '', number_format((float)$result->rentzestimate->valueChange,2,".",","));
			if($result->rentzestimate->valueChange>0){
				$restimateValueChangeSign="+";
			}
			else if($result->rentzestimate->valueChange<0){
				$restimateValueChangeSign="-";
			}
		}

		$taxAssessment="N/A";
		if(isset($result->taxAssessment) && $result->taxAssessment!="")
			$taxAssessment=number_format((float)$result->taxAssessment,2,".",",");

		$restimateValuationRangeLow="N/A";
		if(isset($result->rentzestimate->valuationRange->low) && $result->rentzestimate->valuationRange->low!="")
			$restimateValuationRangeLow=number_format((float)$result->rentzestimate->valuationRange->low,2,".",",");
		
		$restimateValuationRangeHigh="N/A";
		if(isset($result->rentzestimate->valuationRange->high) && $result->rentzestimate->valuationRange->high!="")
			$restimateValuationRangeHigh=number_format((float)$result->rentzestimate->valuationRange->high,2,".",",");
		
		$url = "http://www.zillow.com/webservice/GetChart.htm?zws-id=X1-ZWz1b2re0bhix7_2avji&unit-type=percent&zpid=".$result->zpid."&width=600&height=300&chartDuration=1year";
		$imageResults=simplexml_load_file($url);
		$one_year_url = $imageResults->response->url;
		
		$url = "http://www.zillow.com/webservice/GetChart.htm?zws-id=X1-ZWz1b2re0bhix7_2avji&unit-type=percent&zpid=".$result->zpid."&width=600&height=300&chartDuration=5years";
		$imageResults=simplexml_load_file($url);
		$five_years_url = $imageResults->response->url;
		
		$url = "http://www.zillow.com/webservice/GetChart.htm?zws-id=X1-ZWz1b2re0bhix7_2avji&unit-type=percent&zpid=".$result->zpid."&width=600&height=300&chartDuration=10years";
		$imageResults=simplexml_load_file($url);
		$ten_years_url = $imageResults->response->url;

		//Construct JSON output object
		$JSON_str_output="{
			\"message\" :{
							\"code\" : \"$msg_code\",
							\"text\" : \"$msg_text\"
						},
			\"header\" 	:{
							\"homedetails\" : \"$homedetails\",
							\"street\" : \"$street\",
							\"city\" : \"$city\",
							\"state\" : \"$state\",
							\"zipcode\" : \"$zipcode\"
						},
			\"content\" :{
							\"useCode\" : \"$useCode\",
							\"lastSoldPrice\": \"$lastSoldPrice\",
							\"yearBuilt\": \"$yearBuilt\",
							\"lastSoldDate\": \"$lastSoldDate\",
							\"lotSizeSqFt\": \"$lotSizeSqFt\",
							\"estimateLastUpdate\": \"$estimateLastUpdate\",
							\"estimateAmount\": \"$estimateAmount\",
							\"finishedSqFt\": \"$finishedSqFt\",
							\"estimateValueChange\": \"$estimateValueChange\",
							\"estimateValueChangeSign\" : \"$estimateValueChangeSign\",
							\"bathrooms\": \"$bathrooms\",
							\"estimateValuationRangeLow\": \"$estimateValuationRangeLow\",
							\"estimateValuationRangeHigh\": \"$estimateValuationRangeHigh\",
							\"bedrooms\": \"$bedrooms\",
							\"restimateLastUpdate\": \"$restimateLastUpdate\",
							\"restimateAmount\": \"$restimateAmount\",
							\"taxAssessmentYear\": \"$taxAssessmentYear\",
							\"restimateValueChange\": \"$restimateValueChange\",
							\"restimateValueChangeSign\" : \"$restimateValueChangeSign\",
							\"taxAssessment\": \"$taxAssessment\",
							\"restimateValuationRangeLow\": \"$restimateValuationRangeLow\",
							\"restimateValuationRangeHigh\": \"$restimateValuationRangeHigh\",
							\"imgn\": \"http://www-scf.usc.edu/~csci571/2014Spring/hw6/down_r.gif\",
							\"imgp\":\"http://www-scf.usc.edu/~csci571/2014Spring/hw6/up_g.gif\"
						},
			\"chart\" : {
							\"oneYearChartURL\" : \"$one_year_url\",
							\"fiveYearsChartURL\" : \"$five_years_url\",
							\"tenYearsChartURL\" : \"$ten_years_url\"
						}
		}";
	}
	else{
		if($searchResults->message->code == 508)
			$msg_text = "No exact match found -- Verify that the given address is correct.";
		$JSON_str_output="{
			\"message\" :{
							\"code\" : \"$msg_code\",
							\"text\" : \"$msg_text\"
						}
		}";
	}
	header('Content-Type: application/json');
	header('Access-Control-Allow-Origin: *');
	echo $JSON_str_output;
?>