require "matrix"

h = File.read_lines("input/input24.txt")
  .map(&.split(" @ ").map(&.split(/, +/).map(&.to_i64)))

rx = (200000000000000..400000000000000)
ry = (200000000000000..400000000000000)

def intersect_inside_the_area?(a, b, rx, ry)
  c1, v1 = a
  c2, v2 = b
  x1, y1, _ = c1
  x2, y2, _ = c2
  vx1, vy1, _ = v1
  vx2, vy2, _ = v2

  fx1 = vx1 > 0 ? (x1..) : (..x1)
  fy1 = vy1 > 0 ? (y1..) : (..y1)
  fx2 = vx2 > 0 ? (x2..) : (..x2)
  fy2 = vy2 > 0 ? (y2..) : (..y2)

  m1 = vy1 / vx1
  m2 = vy2 / vx2
  b1 = y1 - m1 * x1
  b2 = y2 - m2 * x2
  m = Matrix.rows([[-m1, 1], [-m2, 1]])
  c = Matrix.column(b1, b2)
  begin
    x, y = m.inverse * c
  rescue Matrix::NotRegular
    return false
  end
  x.in?(rx) && y.in?(ry) &&
    x.in?(fx1) && x.in?(fx2) &&
    y.in?(fy1) && y.in?(fy2)
end

res = h.combinations(2).map do |(a, b)|
  intersect_inside_the_area?(a, b, rx, ry)
end

ans = res.sum(0) { |e| e ? 1 : 0 }

puts "Part 1: #{ans}"

# Part 2 idea is based on the following:
# If we name the initial coordinates of the rock as p0 
# and speed of the rock as v0 (both vectors in 3D),
# the following equation should be true for all hailstones [i]:
# p0 + t[i] * v0 = p[i] + t[i] * v[i]
# or: (p0 - p[i]) = t[i] * (v[i] - v0)
# As t[i] is just a constant,
# then vectors (p0 - p[i]) and (v[i] - v0) are parallel,
# and therefore their cross-product should be equal to 0:
# (p0 - p[i]) x (v[i] - v0) = 0
# Taking the first 3 hailstones, you can have:
# (p0 - p[0]) x (v[0] - v0) = (p0 - p[1]) x (v[0] - v0)
# (p0 - p[0]) x (v[0] - v0) = (p0 - p[2]) x (v[2] = v0)
# and this gives you a system of 6 linear equations,
# so you can solve for p0 and v0.
# Formulas of the coefficients were solved by hand
# and used below to construct the matrixes of the linear system.

def coeffs(a, b)
  ac, av = a
  bc, bv = b
  acx, acy, acz = ac
  avx, avy, avz = av
  bcx, bcy, bcz = bc
  bvx, bvy, bvz = bv
  [[[0_i64, bvz - avz, avy - bvy, 0_i64, acz - bcz, bcy - acy],
    [avz - bvz, 0_i64, bvx - avx, bcz - acz, 0_i64, acx - bcx],
    [bvy - avy, avx - bvx, 0_i64, acy - bcy, bcx - acx, 0_i64]],
   [[bcy * bvz - bcz * bvy - acy * avz + acz * avy,
     bcz * bvx - bcx * bvz - acz * avx + acx * avz,
     bcx * bvy - bcy * bvx - acx * avy + acy * avx]]]
end

co1 = coeffs(h[0], h[1])
co2 = coeffs(h[0], h[2])

bb = co1[1][0].concat co2[1][0]

m = Matrix.rows(co1[0].concat co2[0])
b = Matrix.column(bb[0], bb[1], bb[2], bb[3], bb[4], bb[5])

c1, c2, c3, v1, v2, v3 = m.inverse * b

ans = (c1.round + c2.round + c3.round).to_i64
puts "Part 2: #{ans}"
