<?php 
	session_start();
	$userid = $_SESSION["userid"];
	$pagecounter = $_REQUEST['pagecounter'];
?>


<html>
	<body>
	
		<h3>Novo registo</h3>
		
		<?php
			
			try{
				$host = "db.ist.utl.pt";
				$user ="ist178414";
				$password = "123123";
				$dbname = $user;
				$db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
				$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
				
				echo("<p>Escolha um tipo de registo</p>");
				
				// READ REGISTER TYPES
				$sql = "SELECT typecnt, nome FROM tipo_registo WHERE userid = $userid AND ativo = 1;";
				$result = $db->query($sql);
				foreach($result as $row){
					$typecnt = $row['typecnt'];
					$name = $row['nome'];		
					echo("<form action=\"new_register2.php\" method=\"post\">");
					echo("<p><input type=\"hidden\" name=\"pagecounter\" value=\"$pagecounter\"/></p>");
					echo("<p><input type=\"hidden\" name=\"typecnt\" value=\"$typecnt\"/></p>");
					echo("<p><input type=\"submit\" value=\"$name\"/></p>");
					echo("</form>");
				}
				
				
			
				$db = null;
			}
			catch (PDOException $e){

				echo("<p>ERROR: {$e->getMessage()}</p>");
			}
		
		
		?>
	
	</body>
</html>
