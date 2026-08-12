module Main where

fuel :: Int -> Int
fuel mass = mass `div` 3 - 2

totalFuel :: Int -> Int
totalFuel mass = go mass 0
  where
    go m acc
      | f <= 0 = acc
      | otherwise = go f (acc + f)
      where
        f = fuel m

main :: IO ()
main = do
  input <- readFile "../input/d01.txt"
  let masses = map read $ lines input :: [Int]
  let part1 = sum $ map fuel masses
  let part2 = sum $ map totalFuel masses
  putStrLn $ "part1: " ++ show part1
  putStrLn $ "part2: " ++ show part2
