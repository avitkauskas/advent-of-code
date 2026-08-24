module Main (main) where

import Data.List (group)

range :: (Int, Int)
range = (125730, 579381)

digits :: Int -> [Int]
digits = map (\c -> fromEnum c - fromEnum '0') . show

neverDecreases :: [Int] -> Bool
neverDecreases ds = and (zipWith (<=) ds (drop 1 ds))

hasAdjacent :: [Int] -> Bool
hasAdjacent = any ((>= 2) . length) . group

hasExactDouble :: [Int] -> Bool
hasExactDouble = any ((== 2) . length) . group

countValid :: ([Int] -> Bool) -> Int
countValid extra =
  length
    [ n
    | n <- [fst range .. snd range]
    , let ds = digits n
    , neverDecreases ds
    , extra ds
    ]

main :: IO ()
main = do
  putStrLn $ "part1: " ++ show (countValid hasAdjacent)
  putStrLn $ "part2: " ++ show (countValid hasExactDouble)
