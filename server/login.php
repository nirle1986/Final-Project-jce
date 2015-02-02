<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Login extends CI_Controller {

	public function index()
	{
		$this->authentication();
	}
/*received date from the android app*/	
	public function authentication()
	{
		
		$employee_id		= $this->input->post('employee_id');
		$user_id			= $this->input->post('user_id');
	
/*check if user is able to login */	
		if($this->check_login($employee_id,$user_id)){
			
			echo "valid";
		}else{
			echo "not valid";
		}
	
	}

/*login the user- check if user is exist in the database*/	
	public function check_login($employee_id,$user_id)
	{
		
		$query = $this->db->get_where('aauth_users', array('employee_id' => $employee_id,'user_id' => $user_id));
		
		if ($query->num_rows() > 0){
			
			return true;
		}else{
			
			return false;
		}
			
	}

}

