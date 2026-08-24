module Main (main) where

fuel :: Int -> Int
fuel mass = mass `div` 3 - 2

totalFuel :: Int -> Int
totalFuel = go 0
  where
    go acc m
      | f <= 0 = acc
      | otherwise = go (acc + f) f
      where
        f = fuel m

main :: IO ()
main = do
  input <- readFile "../input/d01.txt"
  let masses = map read (lines input) :: [Int]
      part1 = sum (map fuel masses)
      part2 = sum (map totalFuel masses)
  putStrLn $ "part1: " ++ show part1
  putStrLn $ "part2: " ++ show part2
