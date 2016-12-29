/* Triggers que implementam a Restrição de Integridade */

delimiter //

DROP PROCEDURE IF EXISTS chkseq;
CREATE PROCEDURE chkseq (seq_nr INT)
BEGIN
    DECLARE seq_count INT;

	SET seq_count = (SELECT count(*) FROM pagina WHERE idseq=seq_nr) + 
			 (SELECT count(*) FROM registo WHERE idseq=seq_nr) +
			 (SELECT count(*) FROM tipo_registo WHERE idseq=seq_nr) +
			 (SELECT count(*) FROM campo WHERE idseq=seq_nr) +
			 (SELECT count(*) FROM valor WHERE idseq=seq_nr);
	IF (seq_count!=1)
	THEN
		SIGNAL sqlstate '45001' set message_text = "Something Went Wrong!";
	END IF;
END;

CREATE OR REPLACE TRIGGER before_pagina_insert 
BEFORE INSERT ON pagina
FOR EACH ROW
CALL chkseq(NEW.idseq);

CREATE OR REPLACE TRIGGER before_registo_insert 
BEFORE INSERT ON registo
FOR EACH ROW
CALL chkseq(NEW.idseq);

CREATE OR REPLACE TRIGGER before_tipo_registo_insert 
BEFORE INSERT ON tipo_registo
FOR EACH ROW
CALL chkseq(NEW.idseq);

CREATE OR REPLACE TRIGGER before_campo_insert 
BEFORE INSERT ON campo
FOR EACH ROW
CALL chkseq(NEW.idseq);

CREATE OR REPLACE TRIGGER before_valor_insert 
BEFORE INSERT ON valor
FOR EACH ROW
CALL chkseq(NEW.idseq);

CREATE OR REPLACE TRIGGER before_pagina_update
BEFORE UPDATE ON pagina
FOR EACH ROW
CALL chkseq(NEW.idseq);

CREATE OR REPLACE TRIGGER before_registo_update
BEFORE UPDATE ON registo
FOR EACH ROW
CALL chkseq(NEW.idseq);

CREATE OR REPLACE TRIGGER before_tipo_registo_update
BEFORE UPDATE ON tipo_registo
FOR EACH ROW
CALL chkseq(NEW.idseq);

CREATE OR REPLACE TRIGGER before_campo_update
BEFORE UPDATE ON campo
FOR EACH ROW
CALL chkseq(NEW.idseq);

CREATE OR REPLACE TRIGGER before_valor_update
BEFORE UPDATE ON valor
FOR EACH ROW
CALL chkseq(NEW.idseq);

delimiter;