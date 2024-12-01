(library (matrix)
  (export make-matrix matrix-ref matrix-set!)
  (import (rnrs base) (rnrs control))

  (define make-matrix
    (lambda (rows columns)
      (do ([m (make-vector rows)]
           [i 0 (+ i 1)])
          ((= i rows) m)
          (vector-set! m i (make-vector columns)))))

  (define matrix-ref
    (lambda (m i j)
      (vector-ref (vector-ref m i) j)))

  (define matrix-set!
    (lambda (m i j x)
      (vector-set! (vector-ref m i) j x))))
