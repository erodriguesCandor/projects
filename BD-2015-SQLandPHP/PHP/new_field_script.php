<?php 

	session_start();
	
	$userid = $_SESSION["userid"];
	$typecnt = $_REQUEST['typecnt'];
	$name = $_REQUEST['name'];
	
	try{
				
		$host = "db.ist.utl.pt";
		$user ="ist178414";
		$password = "123123";
		$dbname = $user;
		$db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
		$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		
		// FIND NEW CAMPOCNT
		$sql = "SELECT max(campocnt) +1 AS campocnt FROM campo;";
		$result = $db->query($sql);
		foreach($result as $row){
			$campocnt = $row['campocnt'];
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
		
		// INSERT NEW ROW INTO CAMPO
		$sql = "INSERT INTO campo (userid, typecnt, campocnt, idseq, ativo, nome, pcampocnt) VALUES ($userid, $typecnt, $campocnt, $idseq, 1, '$name', null);";
		$db->query($sql);		
		
		$db = null;
		
		header("Location: http://web.ist.utl.pt/ist178414/register_type.php?typecnt=$typecnt");		
		
	}
	catch (PDOException $e)
	{

			echo("<p>ERROR: {$e->getMessage()}</p>");
	}
?>