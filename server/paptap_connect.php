<?

class mysql {
	
	var $conn;
	
	function openConn()
	{
		$dbhost = 'localhost';
		$dbuser = 'bsel_db';
		$dbpass = 'Levi1986';
		$dbname = 'bsel';
		$this->conn = mysql_connect($dbhost, $dbuser, $dbpass) or die ('Error connecting to mysql');
		mysql_set_charset('utf8');
		mysql_select_db($dbname);
	}
	
	function closeConn()
	{
		mysql_close($this->conn);
	}
	
	function dbGetVal($query)
	{
		$result = mysql_query ($query);
        if($result === FALSE) 
		{
			$myFile = "error.txt";
			$myError = 'Invalid query: ' . mysql_error() . "\n".'Whole query: ' . $query;
			$fh = fopen($myFile, 'a');
			fwrite($fh, $myError);
			fclose($fh);	
		}
        
		$row = mysql_fetch_array($result);
		return $row[0];
	}
	
	function dbExecute($query)
	{
		$result = mysql_query ($query);
        if($result === FALSE) 
		{
			$myFile = "error.txt";
			$myError = 'Invalid query: ' . mysql_error() . "\n".'Whole query: ' . $query;
			$fh = fopen($myFile, 'a');
			fwrite($fh, $myError);
			fclose($fh);	
		}
        
		return mysql_affected_rows();
	}
	
	function dbGetTable($query)
	{
		$pResult = mysql_query($query);		
		if($pResult === FALSE) 
		{
			$myFile = "error.txt";
			$myError = 'Invalid query: ' . mysql_error() . "\n".'Whole query: ' . $query;
			$fh = fopen($myFile, 'a');
			fwrite($fh, $myError);
			fclose($fh);	
		}

		while ($aRow = mysql_fetch_array($pResult)) 
		{
			$aResult[] = $aRow;
		}
		return $aResult;
	}
	
	function dbGetRow($query)
	{
        $result = mysql_query($query);
        if($result === FALSE) 
		{
			$myFile = "error.txt";
			$myError = 'Invalid query: ' . mysql_error() . "\n".'Whole query: ' . $query;
			$fh = fopen($myFile, 'a');
			fwrite($fh, $myError);
			fclose($fh);	
		}
        
		$pResult = mysql_fetch_assoc($result);

		return $pResult;
	}
	
}

	function dbGetVal($query)
	{  
		$DB = new mysql();
		$DB->openConn();
		
		$val = $DB->dbGetVal($query);
		 
		$DB->closeConn();
		
		return $val;
	}
	
	function dbExecute($query)
	{  
		$DB = new mysql();
		$DB->openConn();
		
		$val = $DB->dbExecute($query);
		 
		$DB->closeConn();
		
		return $val;
	}
	
	function dbGetTable($query)
	{
		$DB = new mysql();
		$DB->openConn();
		
		$val = $DB->dbGetTable($query);
		 
		$DB->closeConn();
		
		return $val;
	}
	
	function dbGetRow($query)
	{
		$DB = new mysql();
		$DB->openConn();
		
		$val = $DB->dbGetRow($query);
		 
		$DB->closeConn();
		
		return $val;
	}
	
	function dbEscapeString($query)
	{
		$DB = new mysql();
		$DB->openConn();
		
		$val = mysql_real_escape_string($query);
		 
		$DB->closeConn();
		
		return $val;
	}
?>
