<?php 
	session_start();
	$userid = $_SESSION["userid"];
	$pagecounter = $_REQUEST['pagecounter'];
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
					
					$db->beginTransaction();
					// READ PAGE NAME
					$sql = "SELECT nome FROM pagina WHERE pagecounter = $pagecounter;";
					$result = $db->query($sql);
					foreach($result as $row){
						$page_name = $row['nome'];
					}
				
					echo("<table border=\"0\" cellspacing=\"10\" style=\"display: inline-block\">\n");
					echo("<tr>\n");
					echo("<td><font size=\"5\"> $page_name </font></td>\n");
					
					// NEW REGISTER BUTTON
					echo("<td>\n");
					echo("<form action=\"new_register.php?pagecounter=$pagecounter\" method=\"post\">");
					echo("<p><input type=\"submit\" value=\"Criar registo\"/></p>");
					echo("</form>");
					echo("</td>\n");
					
					
					// READ ID AND NAME OF ACTIVE REGISTERS IN THE PAGE
					$sql = "SELECT regcounter, nome, typecounter FROM registo WHERE regcounter in (SELECT regid as regcounter FROM reg_pag WHERE userid = $userid AND pageid = $pagecounter AND ativa = 1) AND ativo = 1;";
					$result = $db->query($sql);
					foreach($result as $row){
						$regcounter = $row['regcounter'];
						$regname = $row['nome'];
						$typecounter = $row['typecounter'];
						
							echo("<tr>");
							echo("<td><font size=\"4\">$regname</font></td>");
							echo("</tr>");
							
							// READ FIELD NAME FOR EACH REGISTER
							$sql = "SELECT nome, campocnt FROM campo WHERE userid = $userid AND typecnt = $typecounter AND ativo = 1 ORDER BY campocnt;";
							$result3 = $db->query($sql);
							foreach($result3 as $row){
								$name = $row['nome'];
								$campocnt = $row['campocnt'];
								echo("<tr>");
								echo("<td><font size=\"3\">$name:</font></td>");
								
								// READ FIELD VALUES FOR EACH FIELD
								$sql = "SELECT valor FROM valor WHERE regid = $regcounter AND campoid = $campocnt AND ativo = 1;";
								$result4 = $db->query($sql);
								$db->commit();
								foreach($result4 as $row){
									$value = $row['valor'];
									echo("<td>$value</td>");
								}
							
								echo("</tr>");
							}
							
							
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
        