<html>
	<body>
	
		<h3>Novo campo</h3>
		<form action="new_field_script.php" method="post">
			
			<p><input type="hidden" name="typecnt" value="<?=$_REQUEST['typecnt']?>"></p>
			<p>Nome: <input type="text" name="name"/></p>
			<p><input type="submit" value="Concluir"/></p>
			
		</form>
		
	</body>
</html>
