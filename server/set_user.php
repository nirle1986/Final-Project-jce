<?

require 'paptap_connect.php';

$employee_id		= $_REQUEST['employee_id'];
$user_id			= $_REQUEST['user_id'];
$reg_id				= $_REQUEST['reg_id'];
$first_name 		= $_REQUEST['first_name'];
$last_name 			= $_REQUEST['last_name'];

$isExist = dbGetVal("SELECT count(id) FROM users WHERE registration_id = $reg_id");

if($isExist > 0){
	$response["register"] = ;
	$response["message"] = "משתמש כבר קיים";
	echo json_encode($response);	
	exit;
}

$isExistID = dbGetVal("SELECT count(id) FROM users WHERE user_id = $user_id");
if ($isExistID > 0){
	$response["success"] 	= 0;
	$response["message"] 	= "תעודת זהות בשימוש";
	echo json_encode($response);
	exit;
}

$isExistEmplyee = dbGetVal("SELECT count(id) FROM users WHERE employee_id = $employee_id");
if ($isExistEmplyee > 0){
	$response["success"] 	= 0;
	$response["message"] 	= "מספר עובד בשימוש";
	echo json_encode($response);
	exit;
}

dbExecute("INSERT INTO users SET employee_id = $employee_id,
								 user_id = $user_id,
								 first_name = '$first_name',
								 last_name = '$last_name',
								 registration_id = '$reg_id',
								 manager = 0,
								 manager_id = 0
								 ");

$response["success"] 	= 1;
$response["user_level"] = 0;
$response["message"] 	= "ההתחברות בוצעה בהצלחה";
echo json_encode($response);

?> 
