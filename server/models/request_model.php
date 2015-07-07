<?php
	class Request_model extends CI_Model {


		function __construct()
		{
			// Call the Model constructor
			parent::__construct();
		}
		
		public function save_request($user_id,$from_date,$to_date,$reason,$send_comment)
		{	
			
			
			$data = array(
			   'user_id' 		=> 	$user_id,
			   'from_date' 		=> 	date('Y-m-d',strtotime($from_date)),
			   'to_date' 		=> 	date('Y-m-d',strtotime($to_date)),
			   'reason' 		=> 	utf8_encode($reason),
			   'send_comments' 	=> 	utf8_encode($send_comment) 
			);

			$this->db->insert('requests', $data);

		}
		
		
	}
?>