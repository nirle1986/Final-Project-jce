<?

require 'paptap_connect.php';
require 'gcm.php';

$user_id			= $_REQUEST['user_id'];
$from_date			= $_REQUEST['from_date'];
$to_date			= $_REQUEST['to_date'];
$reason				= $_REQUEST['reason'];
$send_comment		= $_REQUEST['send_comments'];
		

dbExecute("INSERT INTO requests SET user_id = $user_id,
								from_date = STR_TO_DATE('$from_date','%d/%m/%Y'),
								to_date = STR_TO_DATE('$to_date','%d/%m/%Y'),
								reason = '$reason',
								send_comments = '$send_comment'");





$newRequestID = dbGetVal("SELECT MAX(id) request_id FROm requests LIMIT 1");

if ($newRequestID > 0){
	$manager = find_manager_id($user_id);
	$managerId = $manager["manager_id"];
	$managerDeviceReg = $manager["reg_id"];

	$textToSend = "$newRequestID@@$reason@@$send_comment";

	$success = sendGCM($textToSend,$managerDeviceReg);

	if ($success){
		$response["upload"] = 1;
		$response["message"] = "הבקשה נשלחה";
	}else{
		$response["upload"] = 0;
		$response["message"] = "הבקשה נכשלה";	
	}
	
}else{
	$response["upload"] = 2;
	$response["message"] = "הבקשה נכשלה";	
}

echo json_encode($response);	

//http://www.nir-levi.com/app/set_request.php?user_id=300140571&from_date=01/08/2015&to_date=09/08/2015&reason=rr&send_comment=efkdlkj


function find_manager_id($user_id){

	$managerRow = dbGetRow("SELECT manager_id, reg_id FROM (
								(SELECT manager_id FROM users 
								 WHERE user_id = '$user_id'
								 LIMIT 1) U,
								(SELECT user_id, registration_id reg_id FROM users) MAN
						    ) WHERE U.manager_id = MAN.user_id");

	if ($managerRow != ""){
		$retManager["manager_id"] = $managerRow["manager_id"];
		$retManager["reg_id"] = $managerRow["reg_id"];
	}else{
		return false;
	}

	return $retManager;
}

function sendGCM($message,$registration_id){

	$gcmObject = new GCM();
	$gcmObject->setMessage("$message");
	$gcmObject->addRecepient($registration_id);

	if ($gcmObject->send())
			return '1';	
		else
			return '0';
}

?> 
