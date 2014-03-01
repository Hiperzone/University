SELECT marcaAut.Marca, marcaAut.Modelo, autocarro.Matricula FROM 
percurso, autocarro, marcaAut WHERE 
(percurso.CodigoP=23 AND autocarro.Modelo = marcaAut.Modelo AND marcaAut.NmaxP >= percurso.NmaxP);



SELECT Marca, Matricula FROM 
(SELECT Marca, Matricula, (2012-anoMatricula) as Idade 
FROM marcaAut NATURAL INNER JOIN autocarro) as temp WHERE (Idade > 10);



SELECT DISTINCT CodigoP, NomeP FROM passoPercurso NATURAL INNER JOIN percurso WHERE
( paragem = 'Lg Luis de Camoes');


SELECT Minutos FROM passoPercurso WHERE( CodigoP=21 AND Paragem='Louredo');



SELECT DISTINCT percurso.CodigoP, NomeP FROM passoPercurso, percurso, passoPercurso as passoPercursoB 
WHERE ( percurso.CodigoP = passoPercurso.CodigoP AND passoPercurso.CodigoP = passoPercursoB.CodigoP AND
passoPercurso.Paragem = 'Malagueira' AND passoPercursoB.Paragem = 'Vista Alegre');



SELECT Nome FROM fezPercurso NATURAL INNER JOIN motorista WHERE
(CodigoP = 21 AND DataInicio between '2009-11-01 00:00:00' AND '2009-11-30 23:59:59');



SELECT DISTINCT count(NBi) FROM fezPercurso WHERE (CodigoP = 23);



SELECT count(*) FROM bilhete NATURAL INNER JOIN percurso WHERE 
(Paragem = 'Granito' AND CodigoP = 21 AND DataInicio = '2009-11-12 06:20:00');



SELECT Codigo, DataInicio FROM (
    (SELECT max(c) as c FROM 
    (SELECT CodigoP, DataInicio, count(*) as c FROM bilhete NATURAL INNER JOIN fezPercurso WHERE 
( DataInicio BETWEEN '2009-11-01 00:00:00' AND '2009-11-30 23:59:59') group by CodigoP, DataInicio) as r 
) as r3
    NATURAL INNER JOIN 
    (SELECT CodigoP, DataInicio, count(*) as c FROM bilhete NATURAL INNER JOIN fezPercurso WHERE 
( DataInicio BETWEEN '2009-11-01 00:00:00' AND '2009-11-30 23:59:59') group by CodigoP, DataInicio) as r2);



(SELECT Matricula FROM autocarro) EXCEPT (SELECT Matricula FROM autocarro NATURAL INNER JOIN fezPercurso WHERE 
( CodigoP = 21 ));



SELECT Nbi FROM ((SELECT max(total) as total FROM 
(SELECT NBi, count(NBi) as total FROM bilhete WHERE 
( DataInicio BETWEEN '2009-11-01 00:00:00' AND '2009-11-30 23:59:59') group by NBi) as r1 ) as r2 
NATURAL INNER JOIN (SELECT NBi, count(NBi) as total FROM bilhete WHERE 
( DataInicio BETWEEN '2009-11-01 00:00:00' AND '2009-11-30 23:59:59') group by NBi) as r3);



SELECT r.NBi FROM fezPercurso as r WHERE NOT EXISTS ((SELECT CodigoP FROM percurso) EXCEPT 
(SELECT t.CodigoP FROM fezPercurso as t WHERE (r.Nbi=t.Nbi)));



SELECT CodigoP FROM((SELECT CodigoP,count(CodigoP)as p FROM passoPercurso group by CodigoP)as r1 NATURAL INNER JOIN 
(SELECT max(p) as p FROM (SELECT CodigoP,count(CodigoP)as p FROM passoPercurso group by CodigoP)as r3) as r2);



SELECT (passageiros*50) as dinheiro FROM (SELECT count (*)as passageiros FROM 
(bilhete NATURAL INNER JOIN fezPercurso) WHERE (datainicio >= '2009-11-1 00:00:00' AND datafim < '2009-11-30 23:59:59'))as c;
