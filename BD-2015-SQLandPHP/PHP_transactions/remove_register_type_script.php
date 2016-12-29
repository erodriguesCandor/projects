<?php 

	session_start();
	
	$userid = $_SESSION["userid"];
	$typecnt = $_REQUEST['typecnt'];
	
	try{
				
		$host = "db.ist.utl.pt";
		$user ="ist178414";
		$password = "123123";
		$dbname = $user;
		$db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
		$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

		$db->beginTransaction();
		// READ TIPO_REGISTO NAME
		$sql = "SELECT nome FROM tipo_registo WHERE typecnt = $typecnt;";
		$result = $db->query($sql);
		foreach($result as $row){
			$name = $row['nome'];
		}
		
		// FIND NEW TYPECNT
		$sql = "SELECT max(typecnt) +1 AS typecnt FROM tipo_registo;";
		$result = $db->query($sql);
		foreach($result as $row){
			$new_typecnt = $row['typecnt'];
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
		
		// INSERT NEW ROW INTO TIPO_REGISTO
		$sql = "INSERT INTO tipo_registo (userid, typecnt, nome, idseq, ativo, ptypecnt) VALUES ($userid, $new_typecnt, '$name', $idseq, 0, $typecnt);";
		$db->query($sql);
		
		// UPDATE EXISTING ROW IN TIPO_REGISTO
		$sql = "UPDATE tipo_registo SET ativo = 0 WHERE typecnt = $typecnt;";
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