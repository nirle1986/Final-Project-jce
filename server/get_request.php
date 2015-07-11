<? 
require 'paptap_connect.php';

$requestId 			= $_REQUEST['request_id'];

$requestRow = dbGetRow("SELECT requests.id req_id, 
								requests.user_id req_user_id, 
								from_date, to_date, 
								reason, send_comments, 
								first_name, 
								last_name,
								registration_id 
						FROM requests,users 
						WHERE requests.id = $requestId
						AND requests.user_id = users.user_id");

if ($requestRow != ""){

	$response["get_request"] = 1;
	$response["req_id"] = $requestId;
	$response["from"] = $requestRow["from_date"];
	$response["to"] = $requestRow["to_date"];
	$response["reason"] = $requestRow["reason"];
	$response["comment"] = $requestRow["send_comments"];
	$response["first_name"] = $requestRow["first_name"];
	$response["last_name"] = $requestRow["last_name"];



}else{
	$response["get_request"] = 0;
	$response["message"] = "מזהה בקשה שגוי";
}

echo json_encode($response);

?>

 