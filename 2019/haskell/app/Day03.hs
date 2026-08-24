module Main (main) where

import Data.IntMap.Strict (IntMap)
import qualified Data.IntMap.Strict as IM
import Data.List.Split (splitOn)

type Point = (Int, Int)

delta :: Char -> Point
delta 'R' = (1, 0)
delta 'L' = (-1, 0)
delta 'U' = (0, 1)
delta 'D' = (0, -1)
delta c = error ("unknown direction: " ++ show c)

keyShift, keyStride :: Int
keyShift = 1048576 -- 2^20, covers all wire coordinates comfortably
keyStride = 2097152 -- 2^21

pointKey :: Point -> Int
pointKey (x, y) = (x + keyShift) * keyStride + (y + keyShift)

keyPoint :: Int -> Point
keyPoint k = (k `div` keyStride - keyShift, k `mod` keyStride - keyShift)

splitOnComma :: String -> [String]
splitOnComma = splitOn ","

parseMoves :: String -> [(Char, Int)]
parseMoves line = [move tok | tok <- splitOnComma line]
  where
    move (d : n) = (d, read n)
    move [] = error "empty move"

traceWire :: [(Char, Int)] -> IntMap Int
traceWire = go (0, 0) 0 IM.empty
  where
    go _ _ acc [] = acc
    go (x, y) t acc ((dir, n) : rest) =
      let (dx, dy) = delta dir
          acc' =
            foldl'
              (\m k -> IM.insertWith min (pointKey (x + dx * k, y + dy * k)) (t + k) m)
              acc
              [1 .. n]
       in go (x + dx * n, y + dy * n) (t + n) acc' rest

solve :: IntMap Int -> IntMap Int -> (Int, Int)
solve traceA traceB = (part1, part2)
  where
    crossings =
      IM.delete (pointKey (0, 0)) (IM.intersectionWith (,) traceA traceB)
    part1 = minimum [abs x + abs y | (k, _) <- IM.toList crossings, let (x, y) = keyPoint k]
    part2 = minimum [sa + sb | (_, (sa, sb)) <- IM.toList crossings]

main :: IO ()
main = do
  input <- readFile "../input/d03.txt"
  let traces = map (traceWire . parseMoves) (filter (not . null) (lines input))
  case traces of
    [traceA, traceB] ->
      let (part1, part2) = solve traceA traceB
       in do
            putStrLn $ "part1: " ++ show part1
            putStrLn $ "part2: " ++ show part2
    _ -> error "expected exactly two wire paths"
