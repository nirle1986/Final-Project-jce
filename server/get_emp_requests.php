<? 
require 'paptap_connect.php';

$userId			= $_REQUEST['user_id'];

$requestsList = dbGetTable("SELECT requests.id req_id, requests.user_id req_user_id, from_date, to_date, reason, send_comments, first_name, last_name FROM requests,users 
						WHERE users.user_id = $userId 
						AND requests.user_id = users.user_id");

if (count($requestsList) > 0){

	$i=0;
	foreach ($requestsList as $requestRow) {
	
		$response[$i]["get_emp_request"] = 1;
		$response[$i]["req_id"] = $requestRow["req_id"];
		$response[$i]["from"] = $requestRow["from_date"];
		$response[$i]["to"] = $requestRow["to_date"];
		$response[$i]["reason"] = $requestRow["reason"];
		$response[$i]["comment"] = $requestRow["send_comments"];
		$response[$i]["first_name"] = $requestRow["first_name"];
		$response[$i]["last_name"] = $requestRow["last_name"];
		$i++;

	}

}else{
	$response["get_emp_requests"] = 0;
	$response["message"] = "לא נמצאו פגישות עבור עובד זה";
}

echo json_encode($response);

//http://www.nir-levi.com/app/get_emp_requests.php?user_id=300140571

?>

 