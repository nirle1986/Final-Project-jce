<?
require 'paptap_connect.php';
require 'gcm.php';


$req_id				= $_REQUEST['req_id'];
$decision			= $_REQUEST['decision'];
$manager_comments	= $_REQUEST['manager_comments'];

if ($req_id != ""){

	dbExecute("UPDATE requests SET req_manager_dec = $decision,
								   req_manager_comment = '$manager_comments'
				WHERE id = $req_id");


	$userRow = dbGetRow("SELECT requests.id req_id, 
								requests.user_id req_user_id, 
								from_date, to_date, 
								reason, send_comments, 
								first_name, 
								last_name,
								registration_id 
						FROM requests,users 
						WHERE requests.id = $req_id
						AND requests.user_id = users.user_id");

	$textToSend = "$req_id@@$decision@@$manager_comments";
	$reg_id = $userRow["registration_id"];

	$success = sendGCM($textToSend,$reg_id);

	if ($success){
		$response["decide"] = 1;
		$response["message"] = "ההחלטה נשלחה";
	}else{
		$response["decide"] = 0;
		$response["message"] = "לא נשלח פוש לעובד";	
	}


}else{

	$response["decide"] = 0;
	$response["message"] = "מזהה בקשה ריק";	
}

echo json_encode($response);	


function sendGCM($message,$registration_id){

	$gcmObject = new GCM();
	$gcmObject->setMessage("$message");
	$gcmObject->addRecepient($registration_id);

	if ($gcmObject->send())
			return '1';	
		else
			return '0';
}

//decision 0 = Not decided yet.
//decision 1 = Approved.
//decision 2 = Not Approved. or Declined

//http://www.nir-levi.com/app/decide_request.php?req_id=1&decision=1&manager_comments=LoLoLo


?>