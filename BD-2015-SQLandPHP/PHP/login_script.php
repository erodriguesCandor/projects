<?php
		session_start();
		$email = $_REQUEST['email'];
		$pass = $_REQUEST['pass'];
		try{
		
			$host = "db.ist.utl.pt";
			$user ="ist178414";
			$password = "123123";
			$dbname = $user;
			$db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
			$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

			// READ USERID AND PASSWORD FROM UTILIZADOR
			$sql = "SELECT userid, password FROM utilizador WHERE email = '$email';";
			$result = $db->query($sql);
			$empty = 1;
						
			foreach($result as $row){
				$saved_pass = $row['password'];
				$_SESSION["userid"] = $row['userid'];
				$userid = $row['userid'];
				$empty = 0;
			}
			
			// USER DOES NOT EXIST IF RESULT == NULL
			// FAIL
			if($empty == 1){
				header('Location: http://web.ist.utl.pt/ist178414/user_not_found.php');
			} 

			// FIND NEW CONTADOR_LOGIN
			$sql = "SELECT max(contador_login) +1 as contador_login FROM login;";
			$result = $db->query($sql);
			foreach($result as $row){
					$contador_login = $row['contador_login'];
			}
			
			// TIMESTAMP
			$date = new DateTime();
			$timestamp = $date->format("Y-m-d H:i:s");
			
			// SUCESS
			if($pass === $saved_pass){
				
				// UPDATE LOGIN TABLE
				$sql = "INSERT INTO login (contador_login, userid, sucesso, moment) VALUES ($contador_login, $userid, 1, '$timestamp');";
				$db->query($sql);
				
				header('Location: http://web.ist.utl.pt/ist178414/main_page.php');		
			}
			// FAIL
			else{
			
				// UPDATE LOGIN TABLE
				$sql = "INSERT INTO login (contador_login, userid, sucesso, moment) VALUES ($contador_login, $userid, 0, '$timestamp');";
				$db->query($sql);
				header('Location: http://web.ist.utl.pt/ist178414/wrong_password.php');		
			}
								
						
				$db = null;
		}
		catch (PDOException $e)
		{
	 
				echo("<p>ERROR: {$e->getMessage()}</p>");
		}
?>
	