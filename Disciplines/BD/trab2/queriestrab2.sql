a)
SELECT NomeA FROM Alunos NATURAL INNER JOIN Inscritos NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas_Instrumento WHERE (NomeInst = 'Flauta de Bisel');

b)
SELECT Count(*) FROM Alunos_Iniciacao NATURAL INNER JOIN Inscritos NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas_Instrumento WHERE (NomeInst = 'Piano');

c)
SELECT BiA, NomeA FROM Alunos NATURAL INNER JOIN Alunos_Integrado NATURAL INNER JOIN Aval_integrado NATURAL INNER JOIN Disciplinas NATURAL INNER JOIN Disciplinas_Instrumento WHERE (NomeInst = 'Piano' AND Periodo = '1' AND NotaP > 3);

d)
SELECT Count(*) FROM Alunos NATURAL INNER JOIN Inscritos NATURAL INNER JOIN Turma NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplina_Coro NATURAL INNER JOIN Aulas_Turma WHERE (NomeA = 'Pedro' AND DataA BETWEEN '2012-11-1 00:00:00' AND '2012-11-30 23:59:59');

e)
SELECT BiA, NomeA, NomeD, DataA FROM Alunos NATURAL INNER JOIN Inscritos NATURAL INNER JOIN Turma NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas NATURAL INNER JOIN Aulas_Turma EXCEPT (SELECT BiA, NomeA, NomeD, DataA FROM Alunos NATURAL INNER JOIN Presencas NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas);

f)
SELECT Count(*) FROM Professores NATURAL INNER JOIN Professores_Turma NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas NATURAL INNER JOIN Disciplina_Coro;

g)
SELECT NomeP FROM Professores EXCEPT ( SELECT NomeP FROM Professores NATURAL INNER JOIN Professores_Turma NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas_Instrumento WHERE  (NomeInst = 'Piano') );

h)
SELECT Count(*) FROM Alunos NATURAL INNER JOIN Presencas NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplina_Coro WHERE ( NomeA = 'Miguel');

i)
SELECT DataA FROM Alunos NATURAL INNER JOIN Inscritos NATURAL INNER JOIN Turma NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Aulas_Turma NATURAL INNER JOIN Disciplinas_Instrumento WHERE ( NomeA = 'Catarina' AND NomeInst = 'Piano' ) EXCEPT (SELECT DataA FROM Alunos NATURAL INNER JOIN Presencas NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas_Instrumento WHERE ( NomeA = 'Catarina' AND NomeInst = 'Piano') );

J)
SELECT TelefoneP FROM Professores NATURAL INNER JOIN Professores_Telefones NATURAL INNER JOIN Professores_Turma NATURAL INNER JOIN Aulas_Turma NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplina_IniciacaoMusical WHERE ( DataA = '2012-12-05 17:00:00');

k)
SELECT NomeP FROM ( SELECT NomeP, Count(NomeP) as c FROM ( SELECT DISTINCT NomeP, NomeInst FROM Professores NATURAL INNER JOIN Professores_Turma NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas_Instrumento) as q1 Group By NomeP) as q2 WHERE (c >= 2);
 

l)
SELECT NomeA, CodD, NomeD, Faltas, notaP FROM Alunos NATURAL INNER JOIN Aval_Integrado NATURAL INNER JOIN Disciplinas
NATURAL INNER JOIN (
    SELECT BiA, CodD, Count(*) as Faltas FROM (
       SELECT BiA, CodD, DataA FROM Alunos NATURAL INNER JOIN Inscritos NATURAL INNER JOIN Turma NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas NATURAL INNER JOIN Aulas_Turma EXCEPT (SELECT BiA, CodD, DataA FROM Alunos NATURAL INNER JOIN Presencas NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas)) as q2
    Group By BiA, CodD ) as q3
UNION
SELECT NomeA, CodD, NomeD, Faltas, notaP FROM Alunos NATURAL INNER JOIN Aval_Supletivo NATURAL INNER JOIN Disciplinas
NATURAL INNER JOIN (
    SELECT BiA, CodD, Count(*) as Faltas FROM (
       SELECT BiA, CodD, DataA FROM Alunos NATURAL INNER JOIN Inscritos NATURAL INNER JOIN Turma NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas NATURAL INNER JOIN Aulas_Turma EXCEPT (SELECT BiA, CodD, DataA FROM Alunos NATURAL INNER JOIN Presencas NATURAL INNER JOIN Turma_Disciplina NATURAL INNER JOIN Disciplinas)) as q2
    Group By BiA, CodD ) as q3;
