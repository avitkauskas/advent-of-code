module Main (main) where

import Control.Monad.ST (ST, runST)
import Data.Array.ST (STUArray, newListArray, readArray, writeArray)

targetOutput :: Int
targetOutput = 19690720

runIntcode :: [Int] -> Int
runIntcode inputValues = runST $ do
  mem <- newListArray (0, length inputValues - 1) inputValues :: ST s (STUArray s Int Int)
  execute mem 0
  where
    execute :: STUArray s Int Int -> Int -> ST s Int
    execute mem instrPtr = do
      opcode <- readArray mem instrPtr
      if opcode == 99
        then readArray mem 0
        else do
          srcAddr1 <- readArray mem (instrPtr + 1)
          srcAddr2 <- readArray mem (instrPtr + 2)
          resultAddr <- readArray mem (instrPtr + 3)
          operand1 <- readArray mem srcAddr1
          operand2 <- readArray mem srcAddr2
          writeArray mem resultAddr (apply opcode operand1 operand2)
          execute mem (instrPtr + 4)

    apply :: Int -> Int -> Int -> Int
    apply 1 a b = a + b
    apply 2 a b = a * b
    apply opcode _ _ = error $ "unknown opcode " ++ show opcode

withNounVerb :: Int -> Int -> [Int] -> [Int]
withNounVerb noun verb (x0 : _ : _ : rest) = x0 : noun : verb : rest
withNounVerb _ _ _ = error "input too short"

findNounVerb :: [Int] -> (Int, Int)
findNounVerb inputValues = search 0 0
  where
    search noun verb
      | runIntcode (withNounVerb noun verb inputValues) == targetOutput = (noun, verb)
      | verb < 99 = search noun (verb + 1)
      | noun < 99 = search (noun + 1) 0
      | otherwise = error "no noun/verb found"

main :: IO ()
main = do
  input <- readFile "../input/d02.txt"
  let inputValues = map read (words (map (\c -> if c == ',' then ' ' else c) input)) :: [Int]
      part1 = runIntcode (withNounVerb 12 2 inputValues)
      (noun, verb) = findNounVerb inputValues
      part2 = 100 * noun + verb
  putStrLn $ "Part 1: Value at position 0: " ++ show part1
  putStrLn $ "Part 2: 100 * noun + verb = " ++ show part2
