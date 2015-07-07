<?php
	class Login_model extends CI_Model {


		function __construct()
		{
			// Call the Model constructor
			parent::__construct();
		}
		
		public function authentication($employee_id,$user_id,$reg_id)
		{	
			$this->db->select('*');
			$this->db->from('users');
			$this->db->where('employee_id', $employee_id);
			$this->db->where('user_id', $user_id);
			$this->db->limit(1);
			$query = $this->db->get();
			
			if ($query->num_rows() > 0)
			{
				$result = $query->result_array();
				
				return $result[0];
			}else{
				
				return false;
			}
		}
		
		
	}
?>