/* Data Warehouse */

DROP TABLE IF EXISTS d_tempo;

CREATE TABLE d_tempo (
    timeid INT NOT NULL,
    dia TINYINT(1) NOT NULL,
    mes TINYINT(1) NOT NULL,
    ano SMALLINT NOT NULL
) ENGINE=InnoDB;

delimiter //
DROP PROCEDURE IF EXISTS timedimbuild//
CREATE PROCEDURE timedimbuild (p_start_date DATE, p_end_date DATE)
BEGIN
    DECLARE v_full_date DATE;
    DELETE FROM d_tempo;
    SET v_full_date = p_start_date;
    WHILE v_full_date < p_end_date DO
        INSERT INTO d_tempo (
            timeid ,
            dia ,
            mes ,
            ano
        ) VALUES (
	    	DATE_FORMAT(v_full_date,'%Y%m%d'),
            DAY(v_full_date),
            MONTH(v_full_date),
            YEAR(v_full_date)           
        );
        SET v_full_date = DATE_ADD(v_full_date, INTERVAL 1 DAY);
    END WHILE;
END;
//
delimiter ;

LOCK TABLES d_tempo WRITE;  

CALL timedimbuild('1975-01-01', '2010-01-01');

UNLOCK TABLES;

DROP TABLE IF EXISTS d_utilizador;

CREATE TABLE d_utilizador (
    userid INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    pais VARCHAR(45) NOT NULL,
    categoria VARCHAR(45) NOT NULL
) ENGINE=InnoDB;

LOCK TABLES d_utilizador WRITE, utilizador READ;

INSERT INTO d_utilizador(userid, email, nome, pais, categoria)
SELECT userid, email, nome, pais, categoria
FROM utilizador;

UNLOCK TABLES;

DROP TABLE IF EXISTS login_attempts;

CREATE TABLE login_attempts (
    timeid INT NOT NULL,
    userid INT NOT NULL,
    attempts INT NOT NULL 
);

LOCK TABLES login_attempts WRITE, login READ, d_tempo READ, d_utilizador READ;

INSERT INTO login_attempts(timeid, userid, attempts)
SELECT DATE_FORMAT(moment, '%Y%m%d'), userid, COUNT(*)
FROM login
GROUP BY DATE_FORMAT(moment, '%Y%m%d'), userid;

UNLOCK TABLES;

/* Query pedida */

/*
SELECT categoria, ano, mes, AVG(attempts) as avg_attempts
FROM login_attempts NATURAL JOIN d_tempo NATURAL JOIN d_utilizador 
WHERE pais='Portugal' 
GROUP BY categoria, ano, mes WITH ROLLUP;
*/