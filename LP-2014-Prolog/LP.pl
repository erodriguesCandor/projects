%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
%
%       GRUPO NUM:132
%       ALUNOS: 78431 Eduardo Rodrigues
%		79027 Tiago Raposo
%		79100 Rui Matos
%
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Pistas
% As pistas sao obtidas de acordo com a posicao(Linha,Coluna) que
% cada peca podera vir a ocupar dentro do tabuleiro.
% Existem pistas com a mesma definicao visto que a sua da descricao
% resultam as mesmas posicoes possiveis para a peca.

% Predicado trioLeft/4 : esta pista pode ocupar duas posicoes
% diferentes dentro do tabuleiro. Portanto, a peca,
% independentemente da sua posicao na pista, pode ocupar duas posicoes
% no tabuleiro : a coordenada indicada, e a posicao que
% corresponde a linha indicada, na coluna do meio. Caso
% Se aplique o raciocinio para cada uma das coordenadas da pista,
% vemos que e o que se verifica.
trioLeft(Peca, Linha, Coluna, Tabuleiro) :-
        (coloca(Peca, Linha, Coluna, Tabuleiro);
         coloca(Peca, Linha, middle, Tabuleiro)).

% Predicado trioRight/4 : tal como a pista trioLeft, esta pista resulta
% nas mesmas posicoes possiveis para a peca, consoante a coordenada
% fornecida. Por isso, foi definida a custa do predicado trioLeft.
trioRight(Peca, Linha, Coluna, Tabuleiro) :-
          trioLeft(Peca, Linha, Coluna, Tabuleiro).

% Predicado cobra/4 : tal como a pista trioLeft, esta pista resulta
% nas mesmas posicoes possiveis para a peca, consoante a coordenada
% fornecida. Por isso, foi definida a custa do predicado trioLeft.
cobra(Peca, Linha, Coluna, Tabuleiro) :-
      trioLeft(Peca, Linha, Coluna, Tabuleiro).

% Predicado cantoTopLeft/4 : esta pista pode ocupar quatro posicoes
% diferentes dentro do tabuleiro, e portanto, a peca,
% independentemente da sua posicao na pista, pode ocupar quatro posicoes
% no tabuleiro: a coordenada indicada; a posicao que
% corresponde a linha indicada, na coluna do meio; a posicao que
% corresponde a coluna indicada, na linha do meio; e a posicao que
% corresponde a posicao central do tabuleiro. Caso
% Se aplique o raciocinio para cada uma das coordenadas da pista,
% vemos que e o que se verifica.
cantoTopLeft(Peca, Linha, Coluna, Tabuleiro) :-
            (coloca(Peca, Linha, Coluna, Tabuleiro);
             coloca(Peca, Linha, middle, Tabuleiro);
             coloca(Peca, center, Coluna, Tabuleiro);
             coloca(Peca, center, middle, Tabuleiro)).

% Predicado cantoTopRight/4 : tal como a pista cantoTopLeft, esta pista
% resulta nas mesmas posicoes possiveis para a peca, consoante a
% coordenada fornecida. Por isso, foi definida a custa do predicado
% cantoTopLeft.
cantoTopRight(Peca, Linha, Coluna, Tabuleiro) :-
	      cantoTopLeft(Peca, Linha, Coluna, Tabuleiro).

% Predicado cantoBottomLeft/4 : tal como a pista cantoTopLeft, esta
% pista resulta nas mesmas posicoes possiveis para a peca, consoante a
% coordenada fornecida. Por isso, foi definida a custa do predicado
% cantoTopLeft.
cantoBottomLeft(Peca, Linha, Coluna, Tabuleiro) :-
	        cantoTopLeft(Peca, Linha, Coluna, Tabuleiro).

% Predicado cantoBottomRight/4 : tal como a pista cantoTopLeft, esta
% pista resulta nas mesmas posicoes possiveis para a peca, consoante a
% coordenada fornecida. Por isso, foi definida a custa do predicado
% cantoTopLeft.
cantoBottomRight(Peca, Linha, Coluna, Tabuleiro) :-
	         cantoTopLeft(Peca, Linha, Coluna, Tabuleiro).

% Predicado tSimples/4 : esta pista pode ocupar duas posicoes
% diferentes dentro do tabuleiro, e portanto, a peca,
% independentemente da sua posicao na pista, pode ocupar duas posicoes
% no tabuleiro: a coordenada indicada e a posicao que
% corresponde a coluna indicada, na linha do meio. Caso
% Se aplique o raciocinio para cada uma das coordenadas da pista,
% vemos que e o que se verifica.
tSimples(Peca, Linha, Coluna, Tabuleiro) :-
	(coloca(Peca, Linha, Coluna, Tabuleiro);
         coloca(Peca, center, Coluna, Tabuleiro)).

% Predicado tInvertido/4 : tal como a pista tSimples, esta
% pista resulta nas mesmas posicoes possiveis para a peca, consoante a
% coordenada fornecida. Por isso, foi definida a custa do predicado
% tSimples.
tInvertido(Peca, Linha, Coluna, Tabuleiro) :-
	   tSimples(Peca, Linha, Coluna, Tabuleiro).

% Predicado tLeft/4 : tal como a pista trioLeft, esta
% pista resulta nas mesmas posicoes possiveis para a peca, consoante a
% coordenada fornecida. Por isso, foi definida a custa do predicado
% trioLeft.
tLeft(Peca, Linha, Coluna, Tabuleiro) :-
      trioLeft(Peca, Linha, Coluna, Tabuleiro).

% Predicado tRight/4 : tal como a pista trioLeft, esta
% pista resulta nas mesmas posicoes possiveis para a peca, consoante a
% coordenada fornecida. Por isso, foi definida a custa do predicado
% trioLeft.
tRight(Peca, Linha, Coluna, Tabuleiro) :-
       trioLeft(Peca, Linha, Coluna, Tabuleiro).

% Predicado diagonalGrave/4 : tal como a pista cantoTopLeft, esta
% pista resulta nas mesmas posicoes possiveis para a peca, consoante a
% coordenada fornecida. Por isso, foi definida a custa do predicado
% cantoTopLeft.
diagonalGrave(Peca, Linha, Coluna, Tabuleiro) :-
	      cantoTopLeft(Peca, Linha, Coluna, Tabuleiro).

% Predicado diagonalAguda/4 : tal como a pista cantoTopLeft, esta
% pista resulta nas mesmas posicoes possiveis para a peca, consoante a
% coordenada fornecida. Por isso, foi definida a custa do predicado
% cantoTopLeft.
diagonalAguda(Peca, Linha, Coluna, Tabuleiro) :-
	      cantoTopLeft(Peca, Linha, Coluna, Tabuleiro).


% Predicado check/2 : check(Tabuleiro, TabuleiroCompleto)
% check recebe como argumentos um tabuleiro (este pode estar
% completo ou incompleto de acordo com as pistas dadas) e devolve
% um tabuleiro completo. Este predicado utiliza um
% predicado auxiliar (check_aux) em que um dos argumentos e
% a lista total de pecas a colocar.
% NOTA: Verificar esquema do funcionamento no fim.

check(Tab, TabF) :- check_aux([peca(quadrado, azul),
			       peca(quadrado, vermelho),
			       peca(quadrado, amarelo),
			       peca(triangulo, azul),
			       peca(triangulo, vermelho),
			       peca(triangulo, amarelo),
			       peca(circulo, azul),
			       peca(circulo, vermelho),
			       peca(circulo, amarelo)],
			       Tab, TabF).


% Predicado check_aux/3 : check(ListaT, Tab, TabF)
% check_aux verifica se existem posicoes por preencher no
% tabuleiro (Tab), preenchendo-as caso existam. Para isso recorre
% a predicados auxiliares, sendo TabF o tabuleiro final preenchido.

check_aux(ListaT, Tab, Tab) :- missing(Tab, ListaT, []).
check_aux(ListaT, Tab, TabF) :- missing(Tab, ListaT, ListaF),
				fill_all(ListaT, ListaF, Tab, TabF).


% Predicado escolhe/3 : escolhe(Lista1, E, Lista2)
% -Este predicado foi retirado das aulas teoricas-
% escolhe remove o elemento E da Lista 1, obtendo-se a Lista2.

escolhe([P|R], P, R).
escolhe([P|R], E, [P|S]) :- escolhe(R, E, S).


% Predicado missing/3 : missing(Tabuleiro, ListaT, ListaF)
% missing determina a lista de pecas (ListaF) que faltam no Tabuleiro.

missing([], L, L).
missing([P|R], ListaT, ListaF) :-
	remove(ListaT, P, F1),
	missing(R, F1, ListaF).


% Predicado id/2 : id(Lista, E)
% id percorre uma lista, verificando se E e um elemento dessa lista
% (excluindo os casos em que E e uma variavel anonima).

id([P|_], E) :- E==P, !.
id([_|R], E) :- id(R, E).


% Predicado remove/3 : remove(ListaT, E, ListaF)
% remove retira o elemento E da ListaT, obtendo-se a ListaF. Exclui os
% casos em que E e uma variavel anonima.

remove(ListaT, E, ListaF) :- id(ListaT, E),
		          escolhe(ListaT, E, ListaF).

remove(ListaT, E, ListaT) :- \+id(ListaT, E).


% Predicado fill_all/4 :
% fill_all(ListaT, ListaF, Tabuleiro, TabuleiroCompleto)
% fill_all preenche o Tabuleiro com os elementos em falta (ListaF),
% com recurso ao predicado auxiliar fill.

fill_all(ListaT, [F1|RF], Tab, TabF) :- fill(ListaT, F1, Tab, TabF),
					fill_all(ListaT, RF, Tab, TabF).
fill_all(_, [], Tab, Tab).


% Predicado fill/4 : fill(ListaT, E, Tabuleiro, TabuleiroCompleto)
% fill preenche a primeira posicao vazia encontrada do TabuleiroCompleto
% com o elemento E.

fill(_, _, [], []).
fill(ListaT, E, [T1|_], [E|_]):- \+id(ListaT, T1).
fill(ListaT, E, [T1|RTab], [T1|RTabF]) :- id(ListaT, T1),
					  fill(ListaT, E, RTab, RTabF).


%  Esquema de funcionamento do predicado check
%  de acordo com todos os predicados auxiliares
%  e a hierarquia entre estes.
%
% | check(Tab, TabF)
% |    | check_aux(ListaT, Tab, TabF)
% |         | missing(ListaT,Tab,ListaF)
% |              | remove
% |                   | id
% |		      |	escolhe
% |         | fill_all(ListaT,ListaF,Tab,TabF)
% |	         | fill
% |		      | id



