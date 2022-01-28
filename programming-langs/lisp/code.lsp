1
(< 1 2)
(eq 1 0)
(if True 1 0)
nil
(cons 1 nil)
(first (cons 1 nil))
(rest (cons 1 nil))


(defun fact (n)
    (if (eq n 0)
        1
        (* n (fact (- n 1)))))

(fact 5)

(eval (quote (+ 1 2)))

(defun sum (n)
    (if (eq n nil)
        0
        (+ (first n) (sum (rest n)))))



(sum (quote (1 2 3)))

