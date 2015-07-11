<? 
require 'paptap_connect.php';

$manager_user_id 			= $_REQUEST['user_id'];

$emplyoees = dbGetTable("SELECT * FROM users WHERE manager_id = $manager_user_id");

if (count($emplyoees) >0 ){

	$requestsList = dbGetTable("SELECT requests.id req_id, requests.user_id req_user_id, from_date, to_date, reason, send_comments, first_name, last_name FROM requests,users 
						WHERE requests.user_id = users.user_id");

	$i=0;
	foreach ($emplyoees as $oneEmplyee) {
		
		$emp_user_id = $oneEmplyee["user_id"];
		$emp_first_name = $oneEmplyee["first_name"];
		$emp_last_name = $oneEmplyee["last_name"];

		if (count($requestsList)>0){

			foreach ($requestsList as $requestRow) {
				
				if ($requestRow["req_user_id"] == $emp_user_id){

					$response[$i]["get_manager_request"] = 1;
					$response[$i]["req_id"] = $requestRow["req_id"];
					$response[$i]["from"] = $requestRow["from_date"];
					$response[$i]["to"] = $requestRow["to_date"];
					$response[$i]["reason"] = $requestRow["reason"];
					$response[$i]["comment"] = $requestRow["send_comments"];
					$response[$i]["emp_user_id"] = $emp_user_id;
					$response[$i]["emp_first_name"] = $emp_first_name;
					$response[$i]["emp_last_name"] = $emp_last_name;
					$i++;
				}
			}
		}else{

			$response["get_manager_requests"] = 0;
			$response["message"] = "לא נמצאו בקשות";
			echo json_encode($response);
			exit;
		}

	}

	//List is full here
	echo json_encode($response);
	exit;




}else{

	$response["get_manager_requests"] = 0;
	$response["message"] = "אין מנהל כזה";
	echo json_encode($response);
	exit;
}



?>

 