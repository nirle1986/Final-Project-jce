<?php
require 'paptap_connect.php';

$employee_id		= $_REQUEST['employee_id'];
$user_id			= $_REQUEST['user_id'];
$reg_id				= $_REQUEST['reg_id'];
$device_id			= $_REQUEST['device_id'];

if ($device_id == ""){
	$response["success"] = 0;
	$response["message"] = "סיריאלי מכשיר ריק";
	echo json_encode($response);
	exit;
}

//echo "SELECT user_id, employee_id,manager FROM users WHERE device_id = '$device_id'";
//exit;


$userRow = dbGetRow("SELECT user_id, employee_id,manager FROM users WHERE device_id = '$device_id'");

if ($userRow != ""){
	
	if ($user_id == $userRow["user_id"] && $employee_id == $userRow["employee_id"]){

		$response["success"] 	= 1;
		$response["user_level"] = $userRow['manager'];
		$response["message"] 	= "ההתחברות בוצעה בהצלחה";
		echo json_encode($response);
		exit;
	}else{

		$response["success"] = 0;
		$response["message"] = "ההתחברות נכשלה";
		echo json_encode($response);
		exit;
	}
}else{


	$isExistID = dbGetVal("SELECT count(id) FROM users WHERE user_id = $user_id");
	if ($isExistID > 0){
		$response["success"] 	= 0;
		$response["user_level"] = 0;
		$response["message"] 	= "תעודת זהות בשימוש";
		echo json_encode($response);
		exit;
	}

	$isExistEmplyee = dbGetVal("SELECT count(id) FROM users WHERE employee_id = $employee_id");
	if ($isExistEmplyee > 0){
		$response["success"] 	= 0;
		$response["user_level"] = 0;
		$response["message"] 	= "מספר עובד בשימוש";
		echo json_encode($response);
		exit;
	}


	dbExecute("INSERT INTO users SET employee_id = $employee_id,
								 user_id = $user_id,
								 first_name = '',
								 last_name = '',
								 registration_id = '$reg_id',
								 device_id = '$device_id',
								 manager = 0,
								 manager_id = 0
								 ");

	$response["success"] 	= 1;
	$response["user_level"] = 0;
	$response["message"] 	= "ההתחברות בוצעה בהצלחה";
	echo json_encode($response);


}

//http://www.nir-levi.com/app/login.php?employee_id=12345&user_id=300140571&reg_id=APA91bGmyGOpYKfH0BQi5BCPJwFTMtQpAVXwwGC-GqZ-tiRtX14-jFokqSwxc7QU9WPQz5Ay4bhqZSqxbegg2WU1-IEqFnUZB7jJFKH3Ly6Nc39o2TQuHuNg2I4U0TjoYh5ABslMvE_pDir05g5eVtQaciPw5uAXRQ&device_id=e21ea876859fb176
