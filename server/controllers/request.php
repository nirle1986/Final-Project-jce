<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Request extends CI_Controller {

	function __construct()
	{
		parent::__construct();

		$this->load->model('request_model');
			
	}
	
	public function index()
	{
		$this->employee_request_vacation();
	}
	
	public function employee_request_vacation()
	{
		
		$log_output = fopen("logs.txt");
	
	
		$user_id			= $this->input->post('user_id');
		$from_date			= $this->input->post('from_date');
		$to_date			= $this->input->post('to_date');
		$reason				= $this->input->post('reason');
		$send_comment		= $this->input->post('send_comments');
		
		$user_data = $this->request_model->save_request($user_id,$from_date,$to_date,$reason,$send_comment);
		
		$response["upload"] = 1;
		$response["message"] = "הבקשה נשלחה";
		echo json_encode($response);
	}
	
}

?> 
