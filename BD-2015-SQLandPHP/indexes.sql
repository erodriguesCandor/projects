/* Índices */

--a)

CREATE INDEX index regpagidx USING BTREE ON reg_pag(userid, pageid, regid);

--b)

CREATE INDEX regname USING HASH ON registo(regcounter, nome); 


/* Queries alvo da melhoria de desempenho */
SELECT AVG(count) FROM (SELECT pageid, COUNT(*) as count 
						FROM reg_pag 
						WHERE userid=743 
						GROUP BY pageid) as regcounts;

SELECT regcounter, nome FROM registo WHERE regcounter IN(	SELECT regid 
															FROM reg_pag 
															WHERE userid=743 AND pageid=96280);

/* Comandos adicionais usados para teste dos índices*/

set profiling=1;
show profiles;

/* Comandos para remover os índices criados caso necessário */

ALTER TABLE reg_pag DROP INDEX regpagidx;
ALTER TABLE registo DROP INDEX regname;