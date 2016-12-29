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

; WORK IN PROGRESS
; PROCURA-PP: PROBLEMA --> LISTA
; procura em profundidade primeiro iterativa que devolve a lista das accoes que permitem atingir a solucao, se existir
(defun procura-pp (problema)
  (let ((estado-inicial (problema-estado-inicial problema))
        (estado-actual) 
        (novo-estado) 
        (solucao) 
        (estados-por-analisar (list)) 
        (lista-accoes) 
        (predecessores (make-hash-table :size 50)))
    
    (push estado-inicial estados-por-analisar)
    
    (loop while (and (not solucao) (not (null estados-por-analisar)))
      do
      (setf estado-actual (pop estados-por-analisar))
      ; verifica se e estado-objectivo, se for calcula a lista de accoes
      (cond ((funcall (problema-solucao problema) estado-actual) 
              (setf solucao (calcula-accoes estado-inicial estado-actual predecessores)))
              
            ; expande o estado, calculando o resultado de todas as accoes disponiveis
            (t  (setf lista-accoes (funcall (problema-accoes problema) estado-actual))   
                (dolist (accao (reverse lista-accoes))
                (setf novo-estado (funcall (problema-resultado problema) estado-actual accao))
                (push novo-estado estados-por-analisar)
                ; guarda info necessaria para o calculo das accoes
                (insere-estado-info novo-estado (cria-estado-info :estado-anterior estado-actual :accao accao) predecessores)))))
    
    solucao))

; CALCULA-ACCOES: ESTADO x ESTADO x HASH-TABLE --> LISTA
; funcao auxiliar
; calcula o conjunto de accoes que permitiu chegar a um certo estado-objectivo 
; a partir do estado inicial utilizando a informacao presente na lista de sucessores
(defun calcula-accoes (estado-inicial estado-actual predecessores)
  (let ((lista-accoes) (estado-info))
    (loop while(not(equalp estado-actual estado-inicial) )
      do
      (setf estado-info (obtem-estado-info estado-actual predecessores))
      (setf estado-actual (estado-info-estado-anterior estado-info))
      (setf lista-accoes (append (list (estado-info-accao estado-info)) lista-accoes)))
    lista-accoes))

; ; PROCURA-PP: PROBLEMA --> LISTA ACCOES
; ; procura em profundidade primeiro recursiva
; (defun procura-pp (problema)
;   (procura-pp-recursiva (problema-estado-inicial problema) (list) problema))

; ; PROCURA-PP-RECURSIVA: ESTADO X CAMINHO X PROBLEMA --> LISTA ACCOES
; ; funcao auxiliar que implementa uma procura profundidade primeiro  recursiva 
; ; adaptada para devolver as accoes necessarias para ir do estado-inicial ao
; ; estado-objectivo
; (defun procura-pp-recursiva (estado caminho problema)
;   (let ((solucao-encontrada nil) (lista-accoes) (accao))
;     (cond ((funcall (problema-solucao problema) estado) caminho)
;           (t  (setf lista-accoes (funcall (problema-accoes problema) estado))  
;               (loop while (and (not solucao-encontrada) (setf accao (pop accoes)))
;                 do
;                 (setf solucao-encontrada 
;                 (procura-pp-recursiva (funcall (problema-resultado problema) estado accao) 
;                 (append caminho (list accao)) problema)))
;               solucao-encontrada))))

(defun solucao1 (estado)
  (= estado 8))
  
(defun accoes1 (estado)
  (cond ((= estado 1) (list 1 2))
        ((= estado 2) (list 2 3))
        ((= estado 3) (list 3 4))
        (t nil)))
  
(defun resultado1 (estado accao)
  (+ estado accao))

; mini arvore de teste
; (defvar p4
;   (cria-problema :estado-inicial 1
;                 :solucao #'solucao1
;                 :accoes #'accoes1
;                 :resultado #'resultado1
;                 :custo-caminho #'custo-oportunidade))
                 

(defun insere-ordenada2 (lista elem)
  (cond ((or (null lista) (>= (first lista) elem))
          (push elem lista))
          
        (t (cons (first lista) (insere-ordenada2 (rest lista) elem)))))