;+----------------------------------+;
;|          + GRUPO 23 +            |;      
;+---------+------------------------+;
;|  78414  |  Ricardo Silva         |;
;|  78431  |  Eduardo Rodrigues     |;
;|  79100  |  Rui Matos             |;
;+---------+------------------------+;


(defconstant LINHAS 18)
(defconstant COLUNAS 10)

;====================================;
;============ TIPO ACCAO ============;      
;====================================;

; CRIA-ACCAO: INTEIRO X ARRAY --> ACCAO
(defun cria-accao (coluna peca)
  (cons coluna peca))

; ACCAO-COLUNA: ACCAO --> INTEIRO
(defun accao-coluna (accao)
  (first accao))

; ACCAO-PECA: ACCAO --> ARRAY
(defun accao-peca (accao)
  (rest accao))

;========================================;
;============ TIPO TABULEIRO ============;
;========================================;

; NOTA: O TABULEIRO E REPRESENTADO POR UM ARRAY 2D
; CRIA-TABULEIRO: {} --> TABULEIRO
(defun cria-tabuleiro()
  (make-array (list LINHAS COLUNAS)))

; COPIA-TABULEIRO: TABULEIRO --> TABULEIRO  
(defun copia-tabuleiro(tabuleiro)
  (let ((novo-tabuleiro))
    (setf novo-tabuleiro (cria-tabuleiro))
    (dotimes (linha LINHAS novo-tabuleiro)
      (dotimes (coluna COLUNAS)
        (setf (aref novo-tabuleiro linha coluna) (aref tabuleiro linha coluna))))))
  
; TABULEIRO-PREENCHIDO-P: TABULEIRO X INTEIRO X INTEIRO --> LOGICO
; devolve o valor logico verdade se a posicao estiver preenchida
; e falso caso contrario
(defun tabuleiro-preenchido-p (tabuleiro linha coluna)
  (cond ((aref tabuleiro linha coluna) t)
        (t nil)))


; TABULEIRO-ALTURA-COLUNA: TABULEIRO X INTEIRO --> INTEIRO
; devolve a altura de uma coluna, ou seja a posicao mais alta que esteja 
; preenchida dessa coluna somada de uma unidade
(defun tabuleiro-altura-coluna (tabuleiro coluna) 
  (let ((altura 0))
    (dotimes(linha LINHAS altura)
      (if (tabuleiro-preenchido-p tabuleiro linha coluna) (setf altura (1+ linha))))))

; TABULEIRO-LINHA-COMPLETA-P: TABULEIRO X INTEIRO --> BOOL
; devolve o valor logico verdade se todas as posicoes da linha recebida 
; estiverem preenchidas
(defun tabuleiro-linha-completa-p (tabuleiro linha)
  (let ((coluna 0))
    (loop while (and (< coluna COLUNAS) (tabuleiro-preenchido-p tabuleiro linha coluna))
      do 
      (incf coluna))
    (cond ((= coluna COLUNAS) t)
          (t nil))))

; TABULEIRO-PREENCHE!: TABULEIRO X INTEIRO X INTEIRO --> {}
; preenche a posicao dada com o valor verdadeiro
(defun tabuleiro-preenche! (tabuleiro linha coluna)
 
  (if (and (and (>= linha 0) (< linha LINHAS)) 
           (and (>= coluna 0) (< coluna COLUNAS)))
 
      (setf (aref tabuleiro linha coluna) T)))

;TABULEIRO-REMOVE-LINHA!: TABULEIRO X LINHA --> {}
(defun tabuleiro-remove-linha! (tabuleiro linha)
  (let ((coluna 0))
    ; limpa a linha
    (loop while (< coluna COLUNAS) 
      do 
        (setf (aref tabuleiro linha coluna) nil)
        (incf coluna))

    ; actualiza o tabuleiro deslocando todas as linhas para baixo
    (loop while (< linha (1- LINHAS)) 
      do
        (setf coluna 0)
        (loop while (< coluna COLUNAS)
          do
            (setf (aref tabuleiro linha coluna) (aref tabuleiro (1+ linha) coluna))
            (incf coluna))
      (incf linha))

    ; coloca a ultima linha a nil
    (setf coluna 0)
    (loop while (< coluna COLUNAS) 
      do
        (setf (aref tabuleiro (1- LINHAS) coluna) nil)
        (incf coluna))))


; TABULEIRO-TOPO-PREENCHIDO-P: TABULEIRO --> BOOL
; devolve o valor logico verdade se existir alguma
; posicao na linha do topo do tabuleiro (linha 17) que esteja preenchida
(defun tabuleiro-topo-preenchido-p (tabuleiro)
  (let ((coluna 0) (topo-preenchido nil))
    (loop while (and (< coluna COLUNAS) (not topo-preenchido)) 
      do
        (if (tabuleiro-preenchido-p tabuleiro (1- LINHAS) coluna) (setf topo-preenchido t))
        (incf coluna))
  topo-preenchido))

; TABULEIROS-IGUAIS-P: TABULEIRO X TABULEIRO --> LOGICO
(defun tabuleiros-iguais-p (tabuleiro1 tabuleiro2)
  (let ((linha 0) (coluna 0) (tabuleiros-iguais t))
    (loop while (and (< linha LINHAS) tabuleiros-iguais)
      do
      (setf coluna 0)
      (loop while(and (< coluna COLUNAS) tabuleiros-iguais)
        do
        (setf tabuleiros-iguais (eq (aref tabuleiro1 linha coluna) (aref tabuleiro2 linha coluna)))
        (incf coluna))

      (incf linha))
  tabuleiros-iguais))

; TABULEIRO->ARRAY: TABULEIRO --> ARRAY
(defun tabuleiro->array (tabuleiro)
  tabuleiro)

; ARRAY->TABULEIRO: ARRAY --> TABULEIRO
(defun array->tabuleiro (array)
  (let((novo-tabuleiro (cria-tabuleiro)))
    (dotimes (linha LINHAS novo-tabuleiro)
      (dotimes (coluna COLUNAS)
        (if (aref array linha coluna) (tabuleiro-preenche! novo-tabuleiro linha coluna))))))


;====================================;
;=== FUNCOES AUXILIARES TABULEIRO ===;
;====================================;

; COLOCA-PECA!: TABULEIRO x ACCAO -> {}
; insere a peca no tabuleiro
(defun coloca-peca!(tabuleiro accao)
  (let  ((peca (accao-peca accao))
        (linha-base (calcula-linha-base tabuleiro accao))
        (coluna-base (accao-coluna accao)))
    (let  ((peca-linhas (array-dimension peca 0))
          (peca-colunas (array-dimension peca 1))
          (decrementa-linha-base t))
  
          ; decrementa a linha-base se houver espacos livres debaixo de todas as colunas da peca 
          (loop while (and decrementa-linha-base (> linha-base 0))
            do
            (dotimes (coluna peca-colunas)
              (if (espaco-preenchido-p tabuleiro (1- (+ linha-base (peca-linha peca coluna))) (+ coluna-base coluna))
                (setf decrementa-linha-base nil)))
                
            (if decrementa-linha-base (decf linha-base)))
        
          ; preenche os espacos correspondentes a peca no tabuleiro
          (dotimes (linha peca-linhas)
            (dotimes (coluna peca-colunas)
              (if (aref peca linha coluna) 
                   (tabuleiro-preenche! tabuleiro (+ linha-base linha) (+ coluna-base coluna))))))))

; CALCULA-LINHA-BASE: TABULEIRO X ACCAO --> INTEIRO
; determina a linha em que a peca pode ser colocada com base na altura das colunas e da posicao mais baixa da peca
(defun calcula-linha-base (tabuleiro accao)
  (let ((altura 0))
    (dotimes (coluna (array-dimension (accao-peca accao) 1) altura)
      (if (< altura (tabuleiro-altura-coluna tabuleiro (+ coluna (accao-coluna accao))))
       (setf altura (tabuleiro-altura-coluna tabuleiro (+ coluna (accao-coluna accao))))))))
       
       
; PECA-LINHA: PECA X INTEIRO --> INTEIRO
; recebe uma peca e uma coluna e devolve a primeira linha ocupada nessa coluna     
(defun peca-linha (peca coluna)
  (let ((linha 0) (resultado))
    (loop while (not resultado)
      do
      (if (aref peca linha coluna) (setf resultado linha) (incf linha)))
  resultado))

; ESPACO-PREENCHIDO-P: TABULEIRO X LINHA X COLUNA --> LOGICO
; devolve t a posicao estiver preenchida e falso se tiver livre ou fora do tabuleiro
(defun espaco-preenchido-p (tabuleiro linha coluna)
  (cond ((>= linha LINHAS) nil)
        (t (tabuleiro-preenchido-p tabuleiro linha coluna))))

;========================================;
;================ ESTADO ================;
;========================================;
(defstruct (estado
  (:constructor make-estado)
  (:constructor cria-estado))
  pontos
  pecas-por-colocar
  pecas-colocadas
  tabuleiro)

; COPIA-ESTADO: ESTADO -> ESTADO
(defun copia-estado (estado)
  (cria-estado
    :pontos (estado-pontos estado)
    :pecas-por-colocar (copy-list(estado-pecas-por-colocar estado))
    :pecas-colocadas (copy-list(estado-pecas-colocadas estado))
    :tabuleiro	(copia-tabuleiro(estado-tabuleiro estado))))

; ESTADOS-IGUAIS-P: ESTADO X ESTADO -> LOGICO
(defun estados-iguais-p (estado1 estado2)
  (AND (typep estado1 'estado) 
       (typep estado2 'estado) 
       (eq (estado-pontos estado1) (estado-pontos estado2))
       (equalp (estado-pecas-por-colocar estado1) 
               (estado-pecas-por-colocar estado2))
       (equalp (estado-pecas-colocadas estado1) 
               (estado-pecas-colocadas estado2))
       (tabuleiros-iguais-p (estado-tabuleiro estado1)
                            (estado-tabuleiro estado2))))
                            
; ESTADO-FINAL-P: ESTADO -> LOGICO
; verifica se o tabuleiro esta completo ou se nao ha mais pecas por colocar
; o tabuleiro esta completo quando existe algum espaco ocupado na ultima linha
(defun estado-final-p (estado)
  (OR(tabuleiro-topo-preenchido-p (estado-tabuleiro estado)) 
     (null (estado-pecas-por-colocar estado))))

;====================================;
;=========== TIPO PROBLEMA ==========;
;====================================;

(defstruct (problema
  (:constructor make-problema)
  (:constructor cria-problema))
  estado-inicial
  solucao 
  accoes
  resultado
  custo-caminho)
  

; SOLUCAO: ESTADO --> LOGICO
; devolve o valor logico verdade se o estado recebido corresponder a uma solucao, 
; e falso caso contrario. e uma solucao se nao houver mais pecas para colocar e 
; se nao houver pecas na ultima linha 
(defun solucao (estado)
  (and  (not (tabuleiro-topo-preenchido-p (estado-tabuleiro estado))) 
        (null (estado-pecas-por-colocar estado))))

; ACCOES: ESTADO --> LISTA DE ACCOES
; devolve uma lista de accoes correspondendo a todas as accoes validas que 
; podem ser feitas com a proxima peca a ser colocada
(defun accoes (estado)
  (cond ((estado-final-p estado) nil)
        ( t
         (let ((prox-peca (first (estado-pecas-por-colocar estado))) (lista (list)))
         (cond  ((eq prox-peca 'i)
                 (setf lista (append (cria-lista peca-i0 10)
                                     (cria-lista peca-i1 7))))
      
                ((eq prox-peca 'l)
                 (setf lista (append (cria-lista peca-l0 9)
                                     (cria-lista peca-l1 8)
                                     (cria-lista peca-l2 9)
                                     (cria-lista peca-l3 8))))
      
                 ((eq prox-peca 'j)
                  (setf lista (append (cria-lista peca-j0 9)
                                      (cria-lista peca-j1 8)
                                      (cria-lista peca-j2 9)
                                      (cria-lista peca-j3 8))))
      
                 ((eq prox-peca 'o)
                  (setf lista (cria-lista peca-o0 9)))
      
                 ((eq prox-peca 's)
                  (setf lista (append (cria-lista peca-s0 8)
                                      (cria-lista peca-s1 9))))
      
                 ((eq prox-peca 'z)
                  (setf lista (append (cria-lista peca-z0 8)
                                      (cria-lista peca-z1 9))))
      
                 ((eq prox-peca 't)
                  (setf lista (append (cria-lista peca-t0 8)
                                      (cria-lista peca-t1 9)
                                      (cria-lista peca-t2 8)
                                      (cria-lista peca-t3 9)))))
          lista))))

; funcao auxiliar da funcao accoes
; CRIA-LISTA: LISTA X INTEIRO X ARRAY --> LISTA
; devolve todas as accoes possiveis para uma determinada peca
(defun cria-lista (peca possibilidades)
  (let((nova-lista (list)))
    (dotimes(i possibilidades nova-lista)
      (setf nova-lista(append nova-lista (list (cria-accao i peca)))))))

; RESULTADO: ESTADO X ACCAO --> ESTADO
; devolve um novo estado que resulta de aplicar a accao recebida no estado original
(defun resultado (estado accao)
  (let ((novo-estado (copia-estado estado))
        (linha (1- LINHAS)) (linhas-rem 0))

    ; actualiza as listas de pecas
    (setf (estado-pecas-colocadas novo-estado) 
     (append (list (first (estado-pecas-por-colocar novo-estado))) 
             (estado-pecas-colocadas novo-estado)))
              
    (setf (estado-pecas-por-colocar novo-estado) 
     (rest (estado-pecas-por-colocar novo-estado)))

    ; coloca a peca no tabuleiro
     (coloca-peca! (estado-tabuleiro novo-estado) accao)
    
    ; verifica se o topo do tabuleiro esta preenchido
    (cond ((tabuleiro-topo-preenchido-p (estado-tabuleiro novo-estado)) 
    novo-estado)
          (t
            ; remove as linhas preenchidas
            (loop while (>= linha 0)
              do
                (cond ((tabuleiro-linha-completa-p (estado-tabuleiro novo-estado) linha)
                    (tabuleiro-remove-linha! (estado-tabuleiro novo-estado) linha)
                    (incf linhas-rem)))
                (decf linha))
 
            ; calcula os pontos
            (cond ((= linhas-rem 1) (incf (estado-pontos novo-estado) 100))
                  ((= linhas-rem 2) (incf (estado-pontos novo-estado) 300))
                  ((= linhas-rem 3) (incf (estado-pontos novo-estado) 500))
                  ((= linhas-rem 4) (incf (estado-pontos novo-estado) 800)))
          novo-estado))))


; QUALIDADE: ESTADO --> INTEIRO
; retorna um valor de qualidade que corresponde ao valor negativo dos pontos
; ganhos ate ao momento
(defun qualidade (estado)
  (- (estado-pontos estado)))


; ; CUSTO-OPORTUNIDADE: ESTADO --> INTEIRO
; ; devolve o custo de oportunidade de todas as accoes realizadas ate ao momento
; ; assumindo que e sempre possivel fazer o maximo de pontos por cada peca colocada
; ; custo oportunidade = pontos possiveis - pontos conseguidos
(defun custo-oportunidade (estado)
  (let ((acumulador (- (estado-pontos estado))) (pecas (estado-pecas-colocadas estado)))
    (dolist (peca pecas acumulador)
        (cond ((eq peca 'i) (incf acumulador 800))
  
              ((or (eq peca 'l) (eq peca 'j)) (incf acumulador 500))
      
              (t (incf acumulador 300))))))
      
    
(defun custo-peca (peca)
  (cond (nil 0)
        ((eq peca 'i)  800)
  
        ((or (eq peca 'l) (eq peca 'j)) 500)
        (t 300)))
  




     
; CUSTO-OPORTUNIDADE: ESTADO --> INTEIRO
; devolve o custo de oportunidade de todas as accoes realizadas ate ao momento
; assumindo que e sempre possivel fazer o maximo de pontos por cada peca colocada
; custo oportunidade = pontos possiveis - pontos conseguidos
; (defun custo-oportunidade-estimativa (estado)
;   (let ((acumulador 0) (pecas (estado-pecas-colocadas estado)) (comprimento-lista))
;     (setf comprimento-lista (list-length pecas))
;     (dotimes (peca )
;         (incf acumulador 400))
     
       
;     (- acumulador (estado-pontos estado))))
     




;====================================;
;============= PROCURAS =============;
;====================================;


; PROCURA-PP: PROBLEMA --> LISTA
; procura em profundidade primeiro iterativa que devolve a lista das accoes que permitem atingir a solucao, se existir
; utiliza uma fila LIFO para guardar os estados nao expandidos 
(defun procura-pp (problema)
  (let ((estado-actual) 
        (novo-estado) 
        (solucao (list)) 
        (fila (list)) 
        (lista-accoes) 
        (predecessores (make-hash-table :size 50)))
    
    (push (problema-estado-inicial problema) fila)
    
    (loop while (and (not solucao) (not (null fila))) 
      do
      (setf estado-actual (pop fila))
      ; verifica se e estado-objectivo, se for calcula a lista de accoes
      (cond ((funcall (problema-solucao problema) estado-actual) 
              (setf solucao (calcula-accoes (problema-estado-inicial problema) estado-actual predecessores)))
              
            ; expande o estado, calculando o resultado de todas as accoes disponiveis
            (t  (setf lista-accoes  (funcall (problema-accoes problema) estado-actual))   
                (dolist (accao lista-accoes)
                (setf novo-estado (funcall (problema-resultado problema) estado-actual accao))
                (push novo-estado fila)
                ; guarda info necessaria para o calculo das accoes
                (insere-estado-info novo-estado (cria-estado-info :estado-anterior estado-actual :accao accao) predecessores)))))
    
    solucao))

; PROCURA-A*: PROBLEMA X HEURISTICA --> LISTA ACCOES
; procura em A*

(defun procura-A* (problema heuristica)
  (let ((estados-por-analisar (list))
        (predecessores (make-hash-table :size 1000))
        (f_n (make-hash-table :size 1000))
        (novo-estado)
        (accoes (list))
        (estado-actual)
        (solucao)
        (estado-inicial (problema-estado-inicial problema))
        (custo-caminho (problema-custo-caminho problema))
        (resultado (problema-resultado problema)))
        
        (push estado-inicial estados-por-analisar)
        (setf (gethash estado-inicial f_n) (funcall heuristica estado-inicial))
        
        
        (loop while (and (not solucao) (not (null estados-por-analisar)))
          do
          ; analisa o estado que esta em primeiro lugar na fila
          (setf estado-actual (pop estados-por-analisar))
          
          
          ; verifica se o estado e o estado-objectivo
          ; se for, calcula o conjunto de accoes que levaram a esse estado
          (cond ((funcall (problema-solucao problema) estado-actual)
                  (setf solucao (calcula-accoes estado-inicial estado-actual predecessores)))
            
          
                ;Gera todas as accoes possiveis para o estado actual
                ;Aplica todas as accoes ao estado actual de forma a gerar os seus sucessores
                ;Calcula o valor de f_n para cada no gerado
                (t 
                (setf accoes (funcall (problema-accoes problema) estado-actual))
                (dolist (accao accoes)
                  (setf novo-estado (funcall resultado estado-actual accao))
                  (setf (gethash novo-estado f_n) (+ (funcall custo-caminho novo-estado) (funcall heuristica novo-estado)))
                  (setf estados-por-analisar (lista-insere-ordenada! novo-estado estados-por-analisar f_n (gethash novo-estado f_n)))
                  (insere-estado-info novo-estado (cria-estado-info :estado-anterior estado-actual :accao accao) predecessores)))
                
          )
       )
      solucao))
      
; procura A* optimizada       
(defun procura-A*-best (problema heuristica)
  (let ((estados-por-analisar (list))
        (predecessores (make-hash-table :size 1000))
        (novo-estado)
        (solucao (problema-solucao problema))
        (accoes (list))
        (estado-analisar)
        (estado-actual)
        (proxima-peca)
        (heuristica-actual) 
        (custo-caminho-actual)
        (caminho-solucao)
        (melhor-estado)
        (melhor-custo)
        (melhor-estado2)
        (melhor-custo2)
        (melhor-estado3)
        (melhor-custo3)
        (melhor-estado4)
        (melhor-custo4)
        (melhor-estado5)
        (melhor-custo5)
        
        (pontos-heuristica)
        (estado-inicial (problema-estado-inicial problema))
        
        (pontos-proxima-peca)
        (pontos-totais)
        (resultado (problema-resultado problema)))
        
        (push (cons estado-inicial (cons (funcall heuristica estado-inicial) 0)) estados-por-analisar)
       
        
        (loop while (and (not caminho-solucao) (not (null estados-por-analisar)))
          do
          ; analisa o estado que esta em primeiro lugar na fila
          (setf estado-analisar (pop estados-por-analisar))
          (setf estado-actual (car estado-analisar))
          (setf heuristica-actual (cadr estado-analisar))
          (setf custo-caminho-actual (cddr estado-analisar))
         
          ; verifica se o estado e o estado-objectivo
          ; se for, calcula o conjunto de accoes que levaram a esse estado
          (cond ((funcall solucao estado-actual)
                  (setf caminho-solucao (calcula-accoes estado-inicial estado-actual predecessores)))
            
          
                ;Gera todas as accoes possiveis para o estado actual
                ;Aplica todas as accoes ao estado actual de forma a gerar os seus sucessores
                ;Calcula o valor de f_n para cada no gerado
                (t
                (setf proxima-peca (first (estado-pecas-por-colocar estado-actual)))
                (setf pontos-proxima-peca (custo-peca proxima-peca))
                (setf pontos-totais (+ custo-caminho-actual pontos-proxima-peca))
                (setf accoes (funcall (problema-accoes problema) estado-actual))
                (setf melhor-custo 9999999999)
                (setf melhor-custo2 9999999999)
                (setf melhor-custo3 9999999999)
                (setf melhor-custo4 9999999999)
                (setf melhor-custo5 9999999999)
          
                (dolist (accao accoes)
                  (setf novo-estado (funcall resultado estado-actual accao))
                  (setf pontos-heuristica  (+ (funcall heuristica novo-estado) pontos-totais))
                  (cond ((< pontos-heuristica melhor-custo) 
                        (setf melhor-custo pontos-heuristica) 
                        (setf melhor-estado (cons novo-estado (cons pontos-heuristica pontos-totais)))
                        (insere-estado-info novo-estado (cria-estado-info :estado-anterior estado-actual :accao accao) predecessores))
                        
                        ((< pontos-heuristica melhor-custo2) 
                        (setf melhor-custo2 pontos-heuristica) 
                        (setf melhor-estado2 (cons novo-estado (cons pontos-heuristica pontos-totais)))
                        (insere-estado-info novo-estado (cria-estado-info :estado-anterior estado-actual :accao accao) predecessores))
                      
                        
                         ((< pontos-heuristica melhor-custo3) 
                        (setf melhor-custo3 pontos-heuristica) 
                        (setf melhor-estado3 (cons novo-estado (cons pontos-heuristica pontos-totais)))
                        (insere-estado-info novo-estado (cria-estado-info :estado-anterior estado-actual :accao accao) predecessores))
                        
                         ((< pontos-heuristica melhor-custo4) 
                        (setf melhor-custo4 pontos-heuristica) 
                        (setf melhor-estado4 (cons novo-estado (cons pontos-heuristica pontos-totais)))
                        (insere-estado-info novo-estado (cria-estado-info :estado-anterior estado-actual :accao accao) predecessores))
                        
                         ((< pontos-heuristica melhor-custo5) 
                        (setf melhor-custo5 pontos-heuristica) 
                        (setf melhor-estado5 (cons novo-estado (cons pontos-heuristica pontos-totais)))
                        (insere-estado-info novo-estado (cria-estado-info :estado-anterior estado-actual :accao accao) predecessores))
                    
                        )
                
 
                )

                (if melhor-estado5
                (push melhor-estado5 estados-por-analisar))
                (if melhor-estado4
                (push melhor-estado4 estados-por-analisar))
                (if melhor-estado3
                (push melhor-estado3 estados-por-analisar))
                (if melhor-estado2
                (push melhor-estado2 estados-por-analisar))
                (if melhor-estado
                (push melhor-estado estados-por-analisar)))
                
                
          )
       )
      caminho-solucao))      
      
      
(defun procura-best (array lista-pecas)
  (let ((problema)
        (estado-inicial))
        
    
    (setf estado-inicial (cria-estado
                           :pontos 0
                           :pecas-por-colocar lista-pecas
                           :pecas-colocadas nil
                           :tabuleiro (array->tabuleiro array)))
  
    (setf problema (cria-problema
                     :estado-inicial estado-inicial
                     :solucao #'solucao
                     :accoes #'accoes
                     :resultado #'resultado
                     :custo-caminho #'custo-oportunidade))
   
    (procura-A*-best problema #'heuristica-final))) 
    
    
;=============================================;
;====== FUNCOES AUXILIARES DAS PROCURAS ======;
;=============================================;
      
; estrutura auxiliar que guarda informacao necessaria para calcular a lista de accoes que foi feita desde o estado-inicial ate ao estado-objectivo 
(defstruct (estado-info
  (:constructor make-estado-info)
  (:constructor cria-estado-info))
  estado-anterior
  accao)

; funcoes auxiliares para inserir/obter estado-info a partir de hash-table
; INSERE-ESTADO-INFO: ESTADO X ESTADO-INFO X HASH-TABLE --> {}
(defun insere-estado-info (estado estado-info hash-table)
  (setf (gethash estado hash-table) estado-info))

; OBTEM-ESTADO-INFO: ESTADO X HASH-TABLE --> ESTADO-INFO  
(defun obtem-estado-info (estado hash-table)
 (gethash estado hash-table))
        
        
; CALCULA-ACCOES: ESTADO x ESTADO x HASH-TABLE --> LISTA
; funcao auxiliar
; calcula o conjunto de accoes que permitiu chegar a um certo estado-objectivo a partir do estado-inicial utilizando a informacao presente na lista de sucessores
(defun calcula-accoes (estado-inicial estado-actual predecessores)
  (let ((lista-accoes) (estado-info))
    (loop while(not (equalp estado-actual estado-inicial) )
      do
      (setf estado-info (obtem-estado-info estado-actual predecessores))
      (setf estado-actual (estado-info-estado-anterior estado-info))
      (setf lista-accoes (cons (estado-info-accao estado-info) lista-accoes)))
      
    lista-accoes))
  
; INSERE-ORDENADA!: ESTADO X LISTA X HASHTABLE X CUSTO --> LISTA
; insere um elemento ordenado pelo seu custo numa lista
(defun lista-insere-ordenada!(elemento lista f_n custo)
    
  (cond ((or (null lista) (>= (gethash (first lista) f_n) custo))
          ; insere o elemento quando a lista for vazia ou o proximo elemento tiver um custo igual ou superior
          (push elemento lista))

        (t (cons (first lista) (lista-insere-ordenada! elemento (rest lista) f_n custo)))))
        
        
; INSERE-ORDENADA!: ESTADO X LISTA X HASHTABLE X CUSTO --> LISTA
; insere um elemento ordenado pelo seu custo numa lista

(defun lista-insere-ordenada-iter (elemento lista f_n custo)
  (let ((nova-lista) (para nil))
  
    (loop while (and (not para) (not (null lista)))
      do
      (cond ((>= (gethash (first lista) f_n) custo)
              (push elemento lista)
              (setf nova-lista (append nova-lista lista))
              (setf para t))
            
            (t (setf nova-lista (append nova-lista (list (pop lista))))))   
    
    
    )
    (if (not para) (setf nova-lista (append nova-lista (list elemento)) ))
    nova-lista
  )
  

)    

;===============================================================;
;====== FUNCOES PARA CALCULO DE PERFORMANCE E OPTIMIZACAO ======;
;===============================================================;
        

; devolve o estado-objectivo em vez da lista de accoes        
(defun procura-A*-teste(problema heuristica)
  (let ((estados-por-analisar (list))
        ; (f_n (make-hash-table :size 10000))
        (novo-estado)
        (solucao (problema-solucao problema))
        (accoes (list))
        (estado-analisar)
        (estado-actual)
        (proxima-peca)
        (heuristica-actual) 
        (custo-caminho-actual)
        (estado-objectivo)
        (melhor-estado)
        (melhor-custo)
        (melhor-estado2)
        (melhor-custo2)
        (melhor-estado3)
        (melhor-custo3)
        (melhor-estado4)
        (melhor-custo4)
        (melhor-estado5)
        (melhor-custo5)
        
        (pontos-heuristica)
        (estado-inicial (problema-estado-inicial problema))
        
        (pontos-proxima-peca)
        (pontos-totais)
        (resultado (problema-resultado problema)))
        
        (push (cons estado-inicial (cons (funcall heuristica estado-inicial) 0)) estados-por-analisar)
       
        
        (loop while (and (not estado-objectivo) (not (null estados-por-analisar)))
          do
          ; analisa o estado que esta em primeiro lugar na fila
          (setf estado-analisar (pop estados-por-analisar))
          (setf estado-actual (car estado-analisar))
          (setf heuristica-actual (cadr estado-analisar))
          (setf custo-caminho-actual (cddr estado-analisar))
         
          ; verifica se o estado e o estado-objectivo
          ; se for, calcula o conjunto de accoes que levaram a esse estado
          (cond ((funcall solucao estado-actual)
                  (setf estado-objectivo estado-actual))
            
          
                ;Gera todas as accoes possiveis para o estado actual
                ;Aplica todas as accoes ao estado actual de forma a gerar os seus sucessores
                ;Calcula o valor de f_n para cada no gerado
                (t
                (setf proxima-peca (first (estado-pecas-por-colocar estado-actual)))
                (setf pontos-proxima-peca (custo-peca proxima-peca))
                (setf pontos-totais (+ custo-caminho-actual pontos-proxima-peca))
                (setf accoes (funcall (problema-accoes problema) estado-actual))
                (setf melhor-custo 9999999999)
                (setf melhor-custo2 9999999999)
                (setf melhor-custo3 9999999999)
                (setf melhor-custo4 9999999999)
                (setf melhor-custo5 9999999999)
          
                (dolist (accao accoes)
                  (setf novo-estado (funcall resultado estado-actual accao))
                  (setf pontos-heuristica  (+ (funcall heuristica novo-estado) pontos-totais))
                  (cond ((< pontos-heuristica melhor-custo) 
                        (setf melhor-custo pontos-heuristica) 
                        (setf melhor-estado (cons novo-estado (cons pontos-heuristica pontos-totais))))
                        
                        ((< pontos-heuristica melhor-custo2) 
                        (setf melhor-custo2 pontos-heuristica) 
                        (setf melhor-estado2 (cons novo-estado (cons pontos-heuristica pontos-totais))))
                      
                        
                         ((< pontos-heuristica melhor-custo3) 
                        (setf melhor-custo3 pontos-heuristica) 
                        (setf melhor-estado3 (cons novo-estado (cons pontos-heuristica pontos-totais))))
                        
                         ((< pontos-heuristica melhor-custo4) 
                        (setf melhor-custo4 pontos-heuristica) 
                        (setf melhor-estado4 (cons novo-estado (cons pontos-heuristica pontos-totais))))
                        
                         ((< pontos-heuristica melhor-custo5) 
                        (setf melhor-custo5 pontos-heuristica) 
                        (setf melhor-estado5 (cons novo-estado (cons pontos-heuristica pontos-totais))))
                    
                        )
                
 
                )

                (if melhor-estado5
                (push melhor-estado5 estados-por-analisar))
                (if melhor-estado4
                (push melhor-estado4 estados-por-analisar))
                (if melhor-estado3
                (push melhor-estado3 estados-por-analisar))
                (if melhor-estado2
                (push melhor-estado2 estados-por-analisar))
                (if melhor-estado
                (push melhor-estado estados-por-analisar)))
                
                
          )
       )
      estado-objectivo))        
        
; INSERE-ORDENADA: ESTADO X LISTA X HASHTABLE X CUSTO --> LISTA
; insere um elemento ordenado pelo seu custo numa lista
(defun lista-insere-ordenada-best(elemento lista)
 
  (cond ((or (null lista) (>= (cadr (first lista)) (cadr elemento)))
          ; insere o elemento quando a lista for vazia ou o proximo elemento tiver um custo igual ou superior
          (push elemento lista))

        (t (cons (first lista) (lista-insere-ordenada-best elemento (rest lista))))))

; TEMPERA-SIMULADA
; http://katrinaeg.com/simulated-annealing.html
; utilizamos para maximizar o desempenho da funcao heuristica-final que corresponde a uma combinacao de varias heuristicas
; um estado e uma lista que contem 6 pesos, cada peso corresponde ao coeficiente que multiplica por uma heuristica
; desempenho e calculado com numero de pontos e numero de linhas
(defun tempera-simulada (estado-inicial sucessor avaliacao aceitacao)
  (let  ((iteracao 0)
        (temperatura 1.0)
        (decremento-temperatura 0.0001)
        (estado-actual estado-inicial)
        (desempenho-actual (funcall avaliacao estado-inicial))
        (novo-estado)
        (novo-desempenho)
        (probabilidade-aceitacao))
        
    (loop while (and (< iteracao 500000) (> temperatura 0.00001))
      do
      (setf novo-estado (funcall sucessor estado-actual))
      ; precisa de alteracoes nos argumentos da funcao teste
      (setf novo-desempenho (funcall avaliacao novo-estado))
      (setf probabilidade-aceitacao (funcall aceitacao desempenho-actual novo-desempenho temperatura))
      (cond ((> probabilidade-aceitacao (random 1.0))
          (setf estado-actual novo-estado)
          (setf desempenho-actual novo-desempenho)))
      
      ; decrementa a temperatura
      (setf temperatura (- temperatura decremento-temperatura))
      (incf iteracao))
    
    estado-actual))
    
(defun superheuristica (estado-pesos)
  #'(lambda (estado) (+   (* (aref estado-pesos 0) 0) 
                          (* (aref estado-pesos 1) (bloco-mais-alto estado)) 
                          (* (aref estado-pesos 2) (media-altura-colunas estado)) 
                          (* (aref estado-pesos 3) (linhas-vazias estado)) 
                          (* (aref estado-pesos 4) 0))))

(defun avaliacao (estado-pesos)

  (teste2 (superheuristica estado-pesos))

)


    
; recebe uma lista de 6 elementos e devolve uma nova lista random    
(defun sucessor (estado)
  (let  ((novo-estado (make-array 5))
        (peso))
        
    (dotimes (elemento 5 novo-estado)
      (setf peso (aref estado elemento))
      (setf peso (+ peso (* (expt -1 (1+ (random 2))) (random 3.0))))
      (setf (aref novo-estado elemento) peso))))
      
(defun sucessor2 ()
  (let  ((novo-estado (make-array 5)))
        
    (dotimes (elemento 5 novo-estado)
      (setf (aref novo-estado elemento) (* (expt -1 (1+ (random 2))) (random 10.0)))
    )
  )
)
      
; devolve uma probabilidade
; >1 se o desempenho novo for melhor que o actual 
; quanto pior for a nova solucao em relacao a actual menor a probabilidade
; quanto menor for a temperatura menor e a probabilidade para uma solucao pior 
(defun aceitacao (desempenho-actual novo-desempenho temperatura)
  (exp (/ (- novo-desempenho desempenho-actual) temperatura)))
  
; gera tabuleiros e listas de pecas com dificuldade incremental e testa a procura-best
; gera output no ficheiro
; utilizacao: - alterar as heuristicas/funcoes na procura-best, escolher numero de iteracoes e nome de ficheiro e correr

(defun teste (numero-tabuleiros heuristica nome-ficheiro)
  (let  ((tabuleiro)
        (lista-pecas)
        (estado-final)
        (output))
  
    (dotimes (tabuleiro-n numero-tabuleiros)
      ; a cada iteracao o tabuleiro fica mais preenchido
      (setf tabuleiro (cria-tabuleiro-aleatorio 1.0 (- 1 (/ tabuleiro-n numero-tabuleiros))))
      (dotimes (lista-pecas-n 3)
        ; a cada iteracao acrescentam-se 1 pecas a lista de pecas por colocar
        (setf lista-pecas (random-pecas (+ lista-pecas-n 4)))
        (setf estado-final (procura-best2 (tabuleiro->array tabuleiro) lista-pecas heuristica))
        
        (cond ((not (null estado-final))
            (setf output (concatenate 'string (write-to-string (1+ tabuleiro-n))
                                              "  "
                                              (write-to-string (- 1.0 (/ tabuleiro-n numero-tabuleiros)))
                                              "  "
                                              (write-to-string (+ lista-pecas-n 4))
                                              "  "
                                              (write-to-string (estado-pontos estado-final))
                                              "~%"))
    
            (with-open-file (str (concatenate 'string "testes/" nome-ficheiro ".txt")
                          :direction :output
                          :if-exists :append
                          :if-does-not-exist :create)
            (format str output)))
          (t
            (setf output (concatenate 'string (write-to-string (1+ tabuleiro-n))
                                              "  "
                                               (write-to-string (- 1 (/ tabuleiro-n numero-tabuleiros)))
                                              "  "
                                              (write-to-string (+ lista-pecas-n 4))
                                              "  "
                                              "FAILED"
                                              
                                              "~%"))
    
            (with-open-file (str (concatenate 'string "testes/" nome-ficheiro ".txt")
                          :direction :output
                          :if-exists :append
                          :if-does-not-exist :create)
            (format str output))))))))
            
            
(defun teste2 (heuristica)
  (let  ((tabuleiro)
        (lista-pecas)
        (estado-final)
        (pontos 0))
  
    
      ; a cada iteracao o tabuleiro fica mais preenchido
      (setf tabuleiro '#2A((T T T T NIL NIL T T T T)(T T T NIL NIL NIL T T T T)(T T T NIL NIL NIL T T T T)(T T T NIL NIL NIL T T T T)(NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)
      (NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)(NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)(NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)(NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)
      (NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)(NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)(NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)(NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)
      (NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)(NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)(NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)(NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)
      (NIL NIL NIL NIL NIL NIL NIL NIL NIL NIL)))
      
        ; a cada iteracao acrescentam-se 1 pecas a lista de pecas por colocar
        (setf lista-pecas '(t i l l))
        (setf estado-final (procura-best2 (tabuleiro->array tabuleiro) lista-pecas heuristica))
        
        (cond ((not (null estado-final))
            (setf pontos (estado-pontos estado-final)))
           
          (t
            (setf pontos 0)))
            
            
            pontos))            
 

;=====================================;
;============ HEURISTICAS ============;
;=====================================;

; HEURISTICA-FINAL: ESTADO --> INTEIRO
; heuristica que combina varias heuristicas para obter um melhor desempenho
;valor dos coeficientes calculado com base em testes e uso de algoritmo de optimizacao tempera simulada
(defun heuristica-final (estado)
    (+    (* 1.6028517 (irregularidades estado)) 
          (* -2.0820208 (bloco-mais-alto estado)) 
          (* -0.66504406 (media-altura-colunas estado)) 
          (* 0.7125199 (linhas-vazias estado)) 
          (* 8.79986 (buracos-totais estado))
          (* 4.750551 (espacos-livres-totais estado))))


; ESPACOS-LIVRES-TOPO: ESTADO --> INTEIRO
; devolve o numero de espacos livres desde a ultima posicao preenchida de cada coluna ate ao topo do tabuleiro
(defun espacos-livres-topo (estado) 
  (let ((espacos 0) (tabuleiro (estado-tabuleiro estado)))
  
    (dotimes (coluna COLUNAS (- espacos))
      (incf espacos (- (1- LINHAS) (tabuleiro-altura-coluna tabuleiro coluna))))))


; LINHAS-VAZIAS: ESTADO --> INTEIRO
; devolve o numero de linhas vazias do tabuleiro
(defun linhas-vazias (estado)
  (let ((tabuleiro (estado-tabuleiro estado)) (linha (1- LINHAS)))
  
      (loop while (and (>= linha 0) (tabuleiro-linha-vazia-p tabuleiro linha))
        do
        (decf linha))
        
      (- (- (1- LINHAS) linha))))

; COLUNAS-VAZIAS: ESTADO --> INTEIRO
; devolve o somatorio de colunas vazias presentes no tabuleiro      
(defun colunas-vazias (estado)      
  (let ((tabuleiro (estado-tabuleiro estado)) (colunas-vazias 0))
  
      (dotimes (coluna COLUNAS (- colunas-vazias))
      (if (= 0 (tabuleiro-altura-coluna tabuleiro coluna))
        (incf colunas-vazias)))))
        
; ESPACOS-LIVRES-TOTAIS: ESTADO --> INTEIRO
; espacos livres totais no tabuleiro
(defun espacos-livres-totais (estado)
  (let ((espacos-livres 0) (tabuleiro (estado-tabuleiro estado)))
    (dotimes (linha LINHAS (- espacos-livres ))
      (dotimes (coluna COLUNAS)
        (if (not (tabuleiro-preenchido-p tabuleiro linha coluna)) (incf espacos-livres))))))

; TABULEIRO-LINHA-VAZIA-P: TABULEIRO X LINHA --> INTEIRO
; verifica se para uma dada linha de um tabuleiro esta se encontra vazia
(defun tabuleiro-linha-vazia-p (tabuleiro linha)
  (let ((coluna 0) (linha-vazia t))
    
    (loop while (and linha-vazia (< coluna COLUNAS))
      do
      (if (tabuleiro-preenchido-p tabuleiro linha coluna)
          (setf linha-vazia nil))
      (incf coluna))
   linha-vazia))
  
  
; BURACOS-TOTAIS: ESTADO --> INTEIRO
;devolve o numero de buracos vazios num tabuleiro
(defun buracos-totais (estado)
  (let ((buracos 0) (altura-coluna) (tabuleiro (estado-tabuleiro estado)))
    
    (dotimes (coluna COLUNAS buracos) 
      (setf altura-coluna (tabuleiro-altura-coluna tabuleiro coluna))
      
      (dotimes (linha altura-coluna)
        (if (not (tabuleiro-preenchido-p tabuleiro linha coluna))
              (incf buracos))))))


; MEDIA-ALTURA-COLUNAS: ESTADO --> INTEIRO
; devolve a media de alturas do tabuleiro
(defun media-altura-colunas (estado)
  (let ((soma 0) (tabuleiro (estado-tabuleiro estado)))
    (dotimes (coluna COLUNAS  (/ soma COLUNAS))
      (incf soma (1- (tabuleiro-altura-coluna tabuleiro coluna))))))

; LINHAS-COMPLETAS: ESTADO --> INTEIRO
;devolve o somatorio do numero de linhas completas num tabuleiro
(defun linhas-completas (estado)
  (let ((linhas-completas 0) (tabuleiro (estado-tabuleiro estado)))
    (dotimes (linha LINHAS (- linhas-completas))
      (if (tabuleiro-linha-completa-p tabuleiro linha) (incf linhas-completas)))))

; IRREGULARIDADES: ESTADO --> INTEIRO
; pretende medir a acidentacao do topo do tabuleiro, quanto mais irregular for pior
(defun irregularidades (estado)
  (let ((nivel-irregularidade 0) (tabuleiro (estado-tabuleiro estado)))
    (dotimes (coluna (1- COLUNAS) nivel-irregularidade)
      (incf nivel-irregularidade (abs (- (tabuleiro-altura-coluna tabuleiro coluna) (tabuleiro-altura-coluna tabuleiro (1+ coluna)))))))) 


; ALTURA-COLUNAS-REGIOES: ESTADO --> INTEIRO
;devolve o somatorio da multiplicacao de cada altura de coluna pelo valor da regiao
;a que a sua altura esta associada, sendo que o tabuleiro foi dividido por altura em quatro regioes diferentes
(defun altura-colunas-regioes (estado)
  (let ((resultado 0) (altura) (tabuleiro (estado-tabuleiro estado)))
    (dotimes (coluna COLUNAS resultado)
      
      (setf altura (1- (tabuleiro-altura-coluna tabuleiro coluna)))
      (cond ((< altura 6) (incf resultado altura))
            ((< altura 11) (incf resultado (* altura 3)))
            ((< altura 15) (incf resultado (* altura 7)))
            (t (incf resultado (* altura 15)))))))


; BLOCO-MAIS-ALTO: ESTADO --> INTEIRO
; devolve a altura do bloco mais alto do tabuleiro
(defun bloco-mais-alto (estado)
    (let ((max-bloco 0) (tabuleiro (estado-tabuleiro estado)))
      (dotimes (coluna COLUNAS max-bloco)
        (if (> (tabuleiro-altura-coluna tabuleiro coluna) max-bloco) (setf max-bloco (tabuleiro-altura-coluna tabuleiro coluna))))))

(load "utils.fas")


