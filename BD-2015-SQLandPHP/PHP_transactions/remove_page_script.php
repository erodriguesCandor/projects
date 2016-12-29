<?php 

	session_start();
	
	$userid = $_SESSION["userid"];
	$pagecounter = $_REQUEST['pagecounter'];
	
	try{
				
		$host = "db.ist.utl.pt";
		$user ="ist178414";
		$password = "123123";
		$dbname = $user;
		$db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
		$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$db->beginTransaction();
		// READ PAGE NAME
		$sql = "SELECT nome FROM pagina WHERE pagecounter = $pagecounter;";
		$result = $db->query($sql);
		foreach($result as $row){
			$name = $row['nome'];
		}
		
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
		$sql = "INSERT INTO pagina (userid, pagecounter, nome, idseq, ativa, ppagecounter) VALUES ($userid, $new_pagecounter, '$name', $idseq, 0, $pagecounter);";
		$db->query($sql);
		
		// UPDATE EXISTING ROW IN PAGINA
		$sql = "UPDATE pagina SET ativa = 0 WHERE pagecounter = $pagecounter;";
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