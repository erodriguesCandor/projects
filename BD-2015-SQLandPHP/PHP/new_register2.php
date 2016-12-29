<?php 
	session_start();
	$userid = $_SESSION["userid"];
	$pagecounter = $_REQUEST['pagecounter'];
	$typecnt = $_REQUEST['typecnt'];
?>


<html>
	<body>
	
		<h3>Novo registo</h3>
			<?php
				try{
					echo("<form action=\"new_register_script.php\" method=\"post\">");
					
					echo("<p><input type=\"hidden\" name=\"pagecounter\" value=\"$pagecounter\"/></p>");
					echo("<p><input type=\"hidden\" name=\"typecnt\" value=\"$typecnt\"/></p>");
					echo("<p>Nome: <input type=\"text\" name=\"name\"/></p>");
				
					$host = "db.ist.utl.pt";
					$user ="ist178414";
					$password = "123123";
					$dbname = $user;
					$db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
					$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			
			
					// READ REGISTER FIELDS
					$sql = "SELECT campocnt, nome FROM campo WHERE userid = $userid AND typecnt = $typecnt AND ativo = 1 ORDER BY campocnt;";
					$result = $db->query($sql);
					$counter = 0;
					foreach($result as $row){
						
						$campocnt = $row['campocnt'];
						$name = $row['nome'];
						echo("<p>$name: <input type=\"text\" name=\"$counter\"/></p>");
						$counter = $counter + 1;
					}
			
			
			
					$db = null;
				}
				catch (PDOException $e){

					echo("<p>ERROR: {$e->getMessage()}</p>");
				}
			
			?>
			
			<p><input type="submit" value="Concluir"/></p>
			
		</form>
		
	
	</body>
</html>
