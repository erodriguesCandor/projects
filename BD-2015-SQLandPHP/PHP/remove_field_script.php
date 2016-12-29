<?php 

	session_start();
	
	$userid = $_SESSION["userid"];
	$campocnt = $_REQUEST['campocnt'];
	$typecnt = $_REQUEST['typecnt'];
	
	try{
				
		$host = "db.ist.utl.pt";
		$user ="ist178414";
		$password = "123123";
		$dbname = $user;
		$db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
		$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		
		// READ FIELD NAME
		$sql = "SELECT nome FROM campo WHERE campocnt = $campocnt;";
		$result = $db->query($sql);
		foreach($result as $row){
			$name = $row['nome'];
		}
		
		// FIND NEW CAMPOCNT
		$sql = "SELECT max(campocnt) +1 AS campocnt FROM campo;";
		$result = $db->query($sql);
		foreach($result as $row){
			$new_campocnt = $row['campocnt'];
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
		$sql = "INSERT INTO campo (userid, typecnt, campocnt, idseq, ativo, nome, pcampocnt) VALUES ($userid, $typecnt, $new_campocnt, $idseq, 0, '$name', $campocnt);";
		$db->query($sql);
		
		// UPDATE EXISTING ROW IN CAMPO
		$sql = "UPDATE campo SET ativo = 0 WHERE campocnt = $campocnt;";
		$db->query($sql);
		
		$db = null;
		
		header("Location: http://web.ist.utl.pt/ist178414/register_type.php?typecnt=$typecnt");		
					
	
	}
	catch (PDOException $e)
	{

			echo("<p>ERROR: {$e->getMessage()}</p>");
	}
?>