/*query a - quais os utilizadores que falharam mais vezes do que tiveram sucesso*/

SELECT nome FROM utilizador WHERE userid in (SELECT userid FROM ((
  SELECT userid, count(*) as s FROM login WHERE sucesso=1 group by userid ) as s1 NATURAL JOIN 
  (SELECT userid, count(*) as f FROM login WHERE sucesso=0 group by userid) as s2) WHERE s<f);


/*query b - quais os registos que aparecem em todas as paginas de um utilizador
substituir em "some_userid" o userid do utilizador sobre o qual se pretende
obter a informacao*/ 


SELECT R.regid
FROM reg_pag R
WHERE R.userid =*some_userid* and not exists(
  SELECT RP.regid
  FROM reg_pag RP
  WHERE not exists(
	SELECT R2.regid
	FROM reg_pag R2
	WHERE	
		R.pageid=R2.pageid and R2.regid = RP.regid and R.userid=*some_userid*));

/*QUERY C - QUAIS OS UTILIZADORES QUE TEM O MAIOR NUMERO MEDIO DE REGISTOS
POR PAGINA*/

SELECT nome
FROM utilizador
NATURAL JOIN 
  (SELECT userid, MAX(t2.r/t1.p)
   FROM ((SELECT userid, count(distinct pageid) as p FROM reg_pag GROUP BY userid) as t1
    NATURAL JOIN
	  (SELECT userid, count(distinct regid) as r FROM reg_pag GROUP BY userid) as t2)
  ) as t;
  




/*query d - quais os utilizadores, que em todas as suas paginas, tem registos de todos
os tipos de registos que criaram*/

SELECT U.nome FROM utilizador U 
  WHERE not exists (
    SELECT TR.typecnt 
    FROM tipo_registo TR 
    WHERE U.userid = TR.userid and not exists(
      SELECT RG.typeid FROM reg_pag RG WHERE RG.typeid=TR.typecnt and RG.userid=TR.userid and RG.userid = U.userid))
   and U.userid in( SELECT userid FROM reg_pag);
