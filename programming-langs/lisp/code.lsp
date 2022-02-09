(defun append (list0 list1)
    (if (eq list0 nil)
        list1
        (if (atom list0)
            (cons list0 list1)
            (cons (first list0) (append (rest list0) list1)))))

(append nil nil )
(append nil (quote (5 8 10 1 )))
(append (cons 1 (cons 2 nil )) nil )
(append (quote (1 2 3 )) (quote (4 5 6 )))

(defun reverseA (list)
    (if (eq list nil)
        list
        (append (reverseA (rest list)) (cons (first list) nil))))

(reverseA nil )
(reverseA (quote (1 3 7 10 15 3 2 7 )))
(reverseA (cons 8 (cons 5 (cons 2 nil ))))

(defun length (lst)
    (if (eq lst nil)
        0
        (+ 1 (length (rest lst)))))

(length nil )
(length (cons True (cons False (cons False (cons False nil )))))
(length (quote (1 8 6 4 4 3 2 1 )))


(defun flatten (expr)
    (if (atom expr)
        expr
        (if (eq expr nil)
            nil
            (append (flatten (first expr)) (flatten (rest expr)))

        )
    )
)

(flatten nil )
(flatten 1 )
(flatten (quote (1 2 3 )))
(flatten (quote ((1 )(2 )(3 ))))
(flatten (quote (1 (7 8 (3 )(((4 ))(5 6 ))(9 10 0 (11 ))))))

(defun equal (expr0 expr1)
    (if (and (atom expr0) (atom expr1))
    (eq expr0 expr1)
    (if (or (and (atom expr0) (not (atom expr1)))
            (and (atom expr1) (not (atom expr0))))

        False
        (and (equal (first expr0) (first expr1)) (equal (rest expr0) (rest expr1))))))

(equal nil nil )
(equal nil 1 )
(equal (quote (1 ))(quote (2 3 4 5 )))
(equal (quote (1 (7 8 (3 )(((4 ))(5 6 ))(9 10 0 (11 ))))) (quote (1 (7 8 (3 )(((4 ))(5 6 ))(9 10 0 (11 ))))))

(defun find (value exp)
    (if (atom exp)
        (eq exp value)
        (or (find value (first exp)) (find value (rest exp)))))

(find 1 nil )
(find True (cons False (cons False nil )))
(find 111111 (quote (1 2 3 4 6 )))
(find 555 (quote (6 555 67 545 )))


(defun get (list index)
    (if (eq list nil)
        nil
        (if (eq index 0)
            (first list)
            (get (rest list) (- index 1)))))


(get nil 1 )
(get (cons 4 nil ) 0 )
(get (quote (1 b c d e ))3 )


(defun select (list start end)

)

(select (quote (0 1 2 3 4 5 6 7 8 )) 0 (/ (length (quote (0 1 2 3 4 5 6 7 8 ))) 2 ))
(select (quote (0 1 2 3 4 5 6 7 8 )) (/ (length (quote (0 1 2 3 4 5 6 7 8 ))) 2 ) (length (quote (0 1 2 3 4 5 6 7 8 ))))

(defun merge (list0 list1)
    (if (eq list0 nil)
        nil
        (if (< (first list0) (first list1))
        (cons (first list1) (cons (first list0) (merge (rest list0) (rest list1))))
        (cons (first list0) (cons (first list1) (merge (rest list0) (rest list1)))))))


(defun mergeSort (list)
    (if (eq list nil)
        nil
        (merge
            (mergeSort (select 0 (/ (length list) 2))
            (mergeSort (select (+ (/ (length list) 2) 1) (length list))))
        )
    )
)

(mergeSort nil )
(mergeSort (quote (1 )))
(mergeSort (quote (4 3 1 )))
(mergeSort (quote (6 3 2 1 5 7 10 )))

(defun countThrows (dice target)
    (if (eq dice 0)
        (if (eq target 0)
            1
            0)
        (if (eq target 0)
            0
        (countOne dice target))))

(defun countOne (dice target)
    (+ (countThrows (- dice 1) (- target 1))
    (+ (countThrows (- dice 1) (- target 2))
    (+ (countThrows (- dice 1) (- target 3))
    (+ (countThrows (- dice 1) (- target 4))
    (+ (countThrows (- dice 1) (- target 5))
    (countThrows (- dice 1) (- target 6))))))))


(countThrows 0 0 )
(countThrows 1 0 )
(countThrows 1 3 )
(countThrows 2 7 )
(countThrows 2 12 )
(countThrows 4 10 )
(countThrows 3 16 )
(countThrows 3 1 )
(countThrows 3 10 )
