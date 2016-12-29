<?php 

	session_start();
	$userid = $_SESSION["userid"];
	$pagecounter = $_REQUEST['pagecounter'];
	$typecnt = $_REQUEST['typecnt'];
	$name = $_REQUEST['name'];
	
	try{
		

		print $typecnt;
		print $pagecounter;
		print $name;

		
		$host = "db.ist.utl.pt";
		$user ="ist178414";
		$password = "123123";
		$dbname = $user;
		$db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
		$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	
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
		
		// FIND NEW REGOUNTER
		$sql = "SELECT max(regcounter) +1 AS regcounter FROM registo;";
		$result = $db->query($sql);
		foreach($result as $row){
			$regcounter = $row['regcounter'];
		}
		print $typecnt;
		echo("ola");
		// INSERT NEW ROW INTO REGISTO
		$sql = "INSERT INTO registo (userid, typecounter, regcounter, nome, ativo, idseq, pregcounter) VALUES ($userid, $typecnt, $regcounter, '$name', 1, $idseq, null);";
		$db->query($sql);
				
		echo("ola");
		
		// COMPUTE NEW IDSEQ
		$idseq = $idseq + 1;
		
		// INSERT NEW ROW INTO SEQUENCIA
		$date = new DateTime();
		$timestamp = $date->format("Y-m-d H:i:s");
		$sql = "INSERT INTO sequencia (contador_sequencia, moment, userid) VALUES ($idseq, '$timestamp', $userid);";
		$db->query($sql);	
		
				echo("ola");
		// FIND NEW IDREGPAG
		$sql = "SELECT max(idregpag) +1 AS idregpag FROM reg_pag;";
		$result = $db->query($sql);
		foreach($result as $row){
			$idregpag = $row['idregpag'];
		}
		
		// INSERT NEW ROW INTO REG_PAG
		$sql = "INSERT INTO reg_pag (idregpag, userid, pageid, typeid, regid, idseq, ativa, pidregpag) VALUES ($idregpag, $userid, $pagecounter, $typecnt, $regcounter, $idseq, 1, null);";
		$db->query($sql);
	echo("ola");
	
		// READ REGISTER FIELDS
			$sql = "SELECT campocnt FROM campo WHERE userid = $userid AND typecnt = $typecnt AND ativo = 1 ORDER BY campocnt;";
			$result = $db->query($sql);
			$counter = 0;
			foreach($result as $row){
				
				$campocnt = $row['campocnt'];
				// COMPUTE NEW IDSEQ
				$idseq = $idseq + 1;
				
				// INSERT NEW ROW INTO SEQUENCIA
				$date = new DateTime();
				$timestamp = $date->format("Y-m-d H:i:s");
				$sql = "INSERT INTO sequencia (contador_sequencia, moment, userid) VALUES ($idseq, '$timestamp', $userid);";
				$db->query($sql);	
				
				$valor = $_REQUEST["$counter"];
				
				// INSERT NEW ROW INTO VALOR
				$sql = "INSERT INTO valor (userid, typeid, regid, campoid, valor, idseq, ativo, pcampoid) VALUES ($userid, $typecnt, $regcounter, $campocnt, '$valor', $idseq, 1, null);";
				$db->query($sql);
				
				$counter = $counter + 1;
			}
			
		
		
		
		$db = null;
		
		header("Location: http://web.ist.utl.pt/ist178414/page.php?pagecounter=$pagecounter");		
					
		
		
	}
	catch (PDOException $e)
	{

			echo("<p>ERROR: {$e->getMessage()}</p>");
	}
?>