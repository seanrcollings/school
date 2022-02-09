(defun fact (n)
    (if (eq n 0)
        1
        (* n (fact (- n 1)))))

(defun sum (n)
    (if (eq n nil)
        0
        (+ (first n) (sum (rest n)))))

(defun reverse  (list)
    (reverseK list nil))

(defun reverseK (list reversedList)
    (if (eq list nil)
        reversedList
        (reverseK (rest list) (cons (first list) reversedList))))


(reverse (quote (1 2 3)))









