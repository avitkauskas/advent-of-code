module Main (main) where

import Control.Monad.ST (ST, runST)
import Data.Array.ST (STUArray, newListArray, readArray, writeArray)
import Data.List.Split (splitOn)

targetOutput :: Int
targetOutput = 19690720

runIntcode :: [Int] -> Int
runIntcode program = runST $ do
  mem <- newArrayST program
  execute mem 0
  where
    newArrayST :: [Int] -> ST s (STUArray s Int Int)
    newArrayST xs =
      newListArray (0, length xs - 1) xs :: ST s (STUArray s Int Int)

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

-- The output is affine in (noun, verb): out n v = base + dn*n + dv*v,
-- so three runs determine noun/verb instead of brute-forcing all 10^4 pairs.
findNounVerb :: [Int] -> (Int, Int)
findNounVerb inputValues = case candidates of
  (nv : _) -> nv
  [] -> error "no noun/verb found"
  where
    base = runIntcode (withNounVerb 0 0 inputValues)
    dn = runIntcode (withNounVerb 1 0 inputValues) - base
    dv = runIntcode (withNounVerb 0 1 inputValues) - base
    diff = targetOutput - base
    candidates =
      [ (noun, verb)
      | verb <- [0 .. 99]
      , let rem' = diff - dv * verb
      , rem' >= 0
      , rem' `mod` dn == 0
      , let noun = rem' `div` dn
      , noun <= 99
      ]

main :: IO ()
main = do
  input <- readFile "../input/d02.txt"
  let inputValues = map read (splitOn "," (filter (/= '\n') input)) :: [Int]
      part1 = runIntcode (withNounVerb 12 2 inputValues)
      (noun, verb) = findNounVerb inputValues
      part2 = 100 * noun + verb
  putStrLn $ "part1: " ++ show part1
  putStrLn $ "part2: " ++ show part2
