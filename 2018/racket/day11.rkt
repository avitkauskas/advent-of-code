#lang racket

(require threading racket/unsafe/ops)

(define grid-serial 7803)
(define grid-size 300)

(define (cell-power idx)
  (let ((x (add1 (remainder idx grid-size)))
        (y (add1 (quotient idx grid-size))))
    (let ((rack-id (+ x 10)))
      (~> rack-id
          (* y)
          (+ grid-serial)
          (* rack-id)
          (quotient 100)
          (remainder 10)
          (- 5)))))

(define grid
  (build-vector (* grid-size grid-size) cell-power))

(define (xy->idx x y)
  (+ x (* grid-size y)))

(define (square-power x y size)
  (for*/fold
    [(power 0)]
    [(i (in-range x (+ x size)))
     (j (in-range y (+ y size)))]
    (+ power (unsafe-vector-ref grid (xy->idx i j)))))

(define (max-power size)
  (for*/fold
    [(x 1) (y 1) (power (square-power 0 0 size))]
    [(i (in-inclusive-range 0 (- grid-size size)))
     (j (in-inclusive-range 0 (- grid-size size)))]
    (let [(pow (square-power i j size))]
      (if (> pow power)
        (values (add1 i) (add1 j) pow)
        (values x y power)))))

(define (total-power)
  (for/fold
    [(x 1) (y 1) (size 1) (power (square-power 0 0 1))]
    [(s (in-inclusive-range 1 grid-size))]
    (displayln s)
    (let-values (((i j pow) (max-power s)))
      (if (> pow power)
        (values i j s pow)
        (values x y size power)))))

(define (main)
  (call-with-values (lambda () (max-power 3)) (lambda (x y _) (displayln (list x y))))
  (call-with-values (lambda () (total-power)) (lambda (x y s _) (displayln (list x y s)))))
