;;print "HELLO WORLD\n"
(print "HELLO WORLD!\n")

;;add up three numbers and print the result
(print (+ 1 2 3))
(print "\n")


;;define a function to add up a list of numbers and use it to print a the sum of the lsit
(defun sum (x) 
  (if (equal x nil) 0
    (+ (car x) (sum (cdr x)))
    )
  )
(print (sum (cons 1 (cons 2 (cons 3 nil)))))
(print "\n")





