<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Login extends CI_Controller {
	
	
	public function gsm($message,$registration_id)
	{
    // simple loading
    // note: you have to specify API key in config before
        $this->load->library('gcm');

    // simple adding message. You can also add message in the data,
    // but if you specified it with setMesage() already
    // then setMessage's messages will have bigger priority
        $this->gcm->setMessage("$message".date('d.m.Y H:s:i'));

    // add recepient or few
        $this->gcm->addRecepient($registration_id);

    // set additional data
        $this->gcm->setData(array(
            'some_key' => 'some_val'
        ));

    // also you can add time to live
        $this->gcm->setTtl(500);
    // and unset in further
        $this->gcm->setTtl(false);

    // set group for messages if needed
        $this->gcm->setGroup('Test');
    // or set to default
        $this->gcm->setGroup(false);

    // then send
        if ($this->gcm->send())
            echo 'Success for all messages';
        else
            echo 'Some messages have errors';

    // and see responses for more info
        print_r($this->gcm->status);
        print_r($this->gcm->messagesStatuses);

    die(' Worked.');
	
			
	}
}