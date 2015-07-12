<?
require_once('class.phpmailer.php');


function send_mail($from,$to,$subject,$body,$fromName='',$toName='',$attach='',$fbfolder='',$smtpserver='',$smtpuser='',$smtppass='',$debug=''){

	$mail = new PHPMailer(true); // the true param means it will throw exceptions on errors, which we need to catch
	
	$mail->SMTPDebug = 1;	
	$mail->IsSMTP();
	$mail->SMTPAuth = true;
	$mail->IsHTML(true);
	$mail->ContentType = "text/html; charset=\"UTF-8\"";
	$mail->CharSet = "UTF-8";
		
    //$smtpserver = '';
    //$smtpuser = '';
    //$smtppass = '';
    $fromInner = $from;

	
	try {
	  $mail->AddReplyTo($from, "=?UTF-8?B?".base64_encode($fromName)."?=");
	  $mail->AddAddress($to, $toName);
	  $mail->SetFrom($fromInner, "=?UTF-8?B?".base64_encode($fromName)."?=");	  
	  $mail->Subject = '=?UTF-8?B?'.base64_encode(stripcslashes($subject)).'?=';
	  //$mail->AltBody = 'To view the message, please use an HTML compatible email viewer!'; // optional - MsgHTML will create an alternate automatically
	  $mail->MsgHTML(stripcslashes($body));
	  
	if($attach != '') //for attachements
	{
		$arr = explode (";", $attach);
		foreach( $arr as $oneFile) 
		{
			if($oneFile != '')
			{
				$arrName = explode (".", $oneFile);
				$pos = strpos($oneFile, 'graphics/');
				if ($pos === false) 
				{
					$posgib = strpos($oneFile, 'backup/');
					if ($posgib === false) 
					{
						$mail->AddAttachment($fbfolder.'uploads/'.$oneFile,$oneFile);      // attachment
					}
					else
					{
						$mail->AddAttachment($oneFile,str_replace('backup/','',$oneFile));
					}
				}
				else
				{
					$mail->AddAttachment($oneFile,str_replace('graphics/','',$oneFile));
				}
			}
		}
	}

		if($smtpserver != '')
		{
			$arr = explode (",", $smtpserver);
			
			if (count($arr) > 1) {
				$mail->SMTPSecure = 'ssl'; 
				$mail->Host = $arr[0];
				$mail->Port = $arr[1];  
				$mail->Username = $smtpuser;  
				$mail->Password = $smtppass;   
			} else {
				$mail->Host = $smtpserver;
				$mail->Username = $smtpuser;  
				$mail->Password = $smtppass;
			}
		}		
	
		$mail->send();
		} catch (phpmailerException $e) {
			return $e;
		} catch (Exception $e) {
			return $e;
		}
    return "1";
}


?>