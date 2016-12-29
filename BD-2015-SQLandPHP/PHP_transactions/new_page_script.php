<?php 

	session_start();
	
	$userid = $_SESSION["userid"];
	$name = $_REQUEST['name'];
	
	try{
				
		$host = "db.ist.utl.pt";
		$user ="ist178414";
		$password = "123123";
		$dbname = $user;
		$db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
		$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$db->beginTransaction();
		// FIND NEW PAGECOUNTER
		$sql = "SELECT max(pagecounter) +1 AS pagecounter FROM pagina;";
		$result = $db->query($sql);
		foreach($result as $row){
			$new_pagecounter = $row['pagecounter'];
		}
		
		// FIND NEW IDSEQ
		$sql = "SELECT max(contador_sequencia) +1 AS contador_sequencia FROM sequencia;";
		$result = $db->query($sql);
		foreach($result as $row){
			$idseq = $row['contador_sequencia'];
		}
		
		
		// INSERT NEW ROW INTO SEQUENCIA
		$date = new DateTime();
		$timestamp = $date->format("Y-m-d H:i:s");
		$sql = "INSERT INTO sequencia (contador_sequencia, moment, userid) VALUES ($idseq, '$timestamp', $userid);";
		$db->query($sql);	
		
		// INSERT NEW ROW INTO PAGINA
		$sql = "INSERT INTO pagina (userid, pagecounter, nome, idseq, ativa, ppagecounter) VALUES ($userid, $new_pagecounter, '$name', $idseq, 1, null);";
		$db->query($sql);
		$db->commit();
	
		$db = null;
		
		header('Location: http://web.ist.utl.pt/ist178414/main_page.php');		
					
		
		
	}
	catch (PDOException $e)
	{
			$db->rollBack();
			echo("<p>ERROR: {$e->getMessage()}</p>");
	}
?>