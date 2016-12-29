<?php 
	session_start();
	$userid = $_SESSION["userid"];
	$typecnt = $_REQUEST['typecnt'];
?>

<html>
	<body>
			<?php
				try{
				
					$host = "db.ist.utl.pt";
					$user ="ist178414";
					$password = "123123";
					$dbname = $user;
					$db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
					$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					
					
					// READ REGISTER TYPE NAME
					$sql = "SELECT nome FROM tipo_registo WHERE typecnt = $typecnt;";
					$result = $db->query($sql);
					foreach($result as $row){
						echo("<h2>{$row['nome']}</h2>");
					}
					
					
					// READ ACTIVE REGISTER TYPE FIELDS
					$sql = "SELECT nome, campocnt FROM campo WHERE typecnt = $typecnt AND userid = $userid AND ativo = 1;";
					$result = $db->query($sql);
					
					// BUILD FIELDS TABLE
					echo("<table border=\"0\" cellspacing=\"10\" style=\"display: inline-block\">\n");
					echo("<tr>\n");
					echo("<td><font size=\"5\"> Campos </font></td>\n");
					
					// NEW FIELD BUTTON
					echo("<td>\n");
					echo("<form action=\"new_field.php?typecnt=$typecnt\" method=\"post\">");
					echo("<p><input type=\"submit\" value=\"Criar campo\"/></p>");
					echo("</form>");
					echo("</td>\n");
					echo("</tr>\n");
					
					foreach($result as $row)
					{
							echo("<tr>\n");	
							
							// FIELD NAME
							echo("<td>{$row['nome']}</td>\n");
							
							// REMOVE FIELD BUTTON
							echo("<td>\n");
							echo("<form action=\"remove_field_script.php?typecnt=$typecnt&campocnt={$row['campocnt']}\" method=\"post\">");
							echo("<p><input type=\"submit\" value=\"Remover campo\"/></p>");
							echo("</form>");
							echo("</td>\n");
							
							echo("</tr>\n");
					}
					echo("</table>\n");
					
					
					
					$db = null;
			}
			catch (PDOException $e){

				echo("<p>ERROR: {$e->getMessage()}</p>");
			}
		?>
	</body>
</html>