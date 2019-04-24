-- Hunter Damron
-- Copyright 2019
--
-- NOTE: Submitting any part of this assignment for credit is plagiarism.
--   Referencing this material to any extent without citation is plagiarism.

import System.IO
import Data.Char
import Data.Set (Set, singleton, notMember, insert)

dictFile = "data/hw7/dict.txt"

loadDict :: IO [String]
loadDict = do
  contents <- readFile dictFile
  return . lines . (map toUpper) $ contents

loadBoard :: IO [[Char]]
loadBoard = do
  contents <- getContents
  return . (map $ (map head) . words) . lines $ contents

get2d :: (Int, Int) -> [[a]] -> a
get2d (i, j) l = l !! i !! j

inBounds :: (Int, Int) -> Int -> Set (Int, Int) -> Bool
inBounds (i, j) n used = i >= 0 && j >= 0 && i < n && j < n && (notMember (i, j) used)

neighbors :: (Int, Int) -> Int -> Set (Int, Int) -> [(Int, Int)]
neighbors (i, j) n used = [(ni,nj) | ni <- [i-1..i+1], nj <- [j-1..j+1],
                           ni /= i || nj /= j, inBounds (ni, nj) n used]

subdict :: Int -> Char -> [String] -> [String]
subdict i c = filter (\w -> length w > i && w !! i == c)

subsearchBoard :: String -> Set (Int, Int) -> [String] -> [[Char]] -> (Int, Int) -> [String]
subsearchBoard prev used dict board nij = let
  c = get2d nij board
  w = prev ++ [c]
  subd = subdict (length used) c dict
  nused = insert nij used
  nempty = not $ null subd
  match = if (nempty && (head subd) == w) then [w] else []
  submatches = if nempty then searchBoard w nused subd board nij else []
  in match ++ submatches

searchBoard :: String -> Set (Int, Int) -> [String] -> [[Char]] -> (Int, Int) -> [String]
searchBoard prev used dict board ij = concat $ map
  (subsearchBoard prev used dict board)
  (neighbors ij (length board) used)

searchFromij :: [String] -> [[Char]] -> (Int, Int) -> [String]
searchFromij dict board ij = let
  c = get2d ij board
  in searchBoard [c] (singleton ij) (subdict 0 c dict) board ij

startingMsg :: (Int, Int) -> String
startingMsg (i,j) = "Starting " ++ (show i) ++ " " ++ (show j)

foundMsg :: String -> String
foundMsg s = "Found Word: " ++ s

strFromij :: [String] -> [[Char]] -> (Int, Int) -> String
strFromij dict board ij = unlines $ [startingMsg ij] ++ (map foundMsg (searchFromij dict board ij))

strSearchBoard :: [String] -> [[Char]] -> String
strSearchBoard dict board = let
  n = length board
  in concat $ map (strFromij dict board) [(i,j) | i <- [0..n-1], j <- [0..n-1]]

main :: IO ()
main = do
  dict <- loadDict
  board <- loadBoard
  putStr $ strSearchBoard dict board
