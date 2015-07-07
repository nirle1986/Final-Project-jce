<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class getrequest extends CI_Controller {
	
	$user_data=true;
	
	if($user_data==true){
		$response["success"] = 1;
		$response["message"] = "ההודעה התקבלה";
		echo json_encode($response);
	}else{
		$response["success"] = 0;
		$response["message"] = "קבלת ההודעה נכשלה";
		echo json_encode($response);
		}

	
}

