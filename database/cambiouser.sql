ALTER TABLE usuarios ADD COLUMN alias VARCHAR(100);

UPDATE usuarios SET alias = 'jperez' WHERE id = 1;
UPDATE usuarios SET alias = 'aramirez' WHERE id = 2;
UPDATE usuarios SET alias = 'cgomez' WHERE id = 3;
UPDATE usuarios SET alias = 'mtorrez' WHERE id = 4;
UPDATE usuarios SET alias = 'lfernandez' WHERE id = 5;

ALTER TABLE usuarios MODIFY COLUMN alias VARCHAR(100) NOT NULL;

ALTER TABLE usuarios ADD CONSTRAINT unique_alias_constraint UNIQUE (alias);


ALTER TABLE usuarios ADD COLUMN passw int;

UPDATE usuarios SET passw = '837856' WHERE id = 1;
UPDATE usuarios SET passw = '738926' WHERE id = 2;
UPDATE usuarios SET passw = '825698' WHERE id = 3;
UPDATE usuarios SET passw = '738956' WHERE id = 4;
UPDATE usuarios SET passw = '623785' WHERE id = 5;

ALTER TABLE usuarios MODIFY COLUMN passw int NOT NULL;

ALTER TABLE usuarios ADD CONSTRAINT unique_passw_constraint UNIQUE (passw);