<?php 
	session_start();
	$userid = $_SESSION["userid"];
?>



<html>
	<body>
    <h2>P&#225;gina principal</h2>
			<?php
				try{
				
					$host = "db.ist.utl.pt";
					$user ="ist178414";
					$password = "123123";
					$dbname = $user;
					$db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
					$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
					
					$db->beginTransaction();
					// READ ACTIVE PAGES
					$sql = "SELECT nome, pagecounter FROM pagina WHERE userid = $userid AND ativa = 1;";
					$result = $db->query($sql);
					
					// BUILD PAGES TABLE
					echo("<table border=\"0\" cellspacing=\"10\" style=\"display: inline-block\">\n");
					echo("<tr>\n");
					echo("<td><font size=\"5\"> P&#225;ginas </font></td>\n");
					
					// NEW PAGE BUTTON
					echo("<td>\n");
					echo("<form action=\"new_page.php\" method=\"post\">");
					echo("<p><input type=\"submit\" value=\"Criar p&#225;gina\"/></p>");
					echo("</form>");
					echo("</td>\n");
					
					echo("</tr>\n");
					foreach($result as $row)
					{
							echo("<tr>\n");	
							// HIPERLINK FOR PAGE
							echo("<td><a href=\"page.php?pagecounter={$row['pagecounter']}\">{$row['nome']}</a></td>\n");
							
							// REMOVE PAGE BUTTON
							echo("<td>\n");
							echo("<form action=\"remove_page_script.php?pagecounter={$row['pagecounter']}\" method=\"post\">");
							echo("<p><input type=\"submit\" value=\"Remover p&#225;gina\"/></p>");
							echo("</form>");
							echo("</td>\n");
							
							echo("</tr>\n");
					}
					echo("</table>\n");
					
					// READ ACTIVE REGISTER TYPES
					$sql = "SELECT nome, typecnt FROM tipo_registo WHERE userid = $userid AND ativo = 1;";
					$result = $db->query($sql);
					$db->commit();
					// BUILD PAGES REGISTER TYPES TABLE
					echo("<table border=\"0\" cellspacing=\"10\" style=\"display: inline-block\">\n");
					echo("<tr>\n");
					echo("<td><font size=\"5\"> Tipos de registos </font></td>\n");
					
					// NEW REGISTER TYPE BUTTON
					echo("<td>\n");
					echo("<form action=\"new_register_type.php\" method=\"post\">");
					echo("<p><input type=\"submit\" value=\"Criar tipo de registo\"/></p>");
					echo("</form>");
					echo("</td>\n");
					
					echo("</tr>\n");
					foreach($result as $row)
					{
							echo("<tr>\n");
							
							// HIPERLINK FOR REGISTER_TYPE
							echo("<td><a href=\"register_type.php?typecnt={$row['typecnt']}\">{$row['nome']}</a></td>\n");							
							
							// REMOVE REGISTER TYPE BUTTON
							echo("<td>\n");
							echo("<form action=\"remove_register_type_script.php?typecnt={$row['typecnt']}\" method=\"post\">");
							echo("<p><input type=\"submit\" value=\"Remover tipo de registo\"/></p>");
							echo("</form>");
							echo("</td>\n");						
							
							echo("</tr>\n");
					}
					echo("</table>\n");
					
					$db = null;
			}
			catch (PDOException $e){
				$db->rollBack();
				echo("<p>ERROR: {$e->getMessage()}</p>");
			}
		?>
	</body>
</html>
        