; Hunter Damron
; Copyright 2019
;
; NOTE: Submitting any part of this assignment for credit is plagiarism.
;   Referencing this material to any extent without citation is plagiarism.

(use '[clojure.java.io :only (reader)])
(use '[clojure.string :only (split upper-case join)])
(import java.io.BufferedReader)

; Size of board
(def N 5)
(def DICTFILE "data/hw7/dict.txt")

; Load dictionary from file
(def dict (map upper-case (with-open [scan (reader DICTFILE)] (reduce conj [] (line-seq scan)))))

; Load board from stdin as 2d array of chars
(def board (to-array-2d (map #(map first (split % #" " N)) (take N (line-seq (BufferedReader. *in*))))))

; Gets indices +/- 1 from i
(defn neighbors-1d [i] (map (partial + i) (range -1 2)))

; Gets 2d indices +/- 1 from i,j
(defn neighbors-2d [i j] (for [ni (neighbors-1d i) nj (neighbors-1d j)] [ni nj]))

; Gets all 2d indices around i j which are in bounds and not already used
(defn neighbors [i j N used] (filter
  #(apply (fn [ni nj] (and
    (or (not= ni i) (not= nj j))
    (>= ni 0) (>= nj 0) (< ni N) (< nj N)
    (not (contains? used [ni nj])))) %)
  (neighbors-2d i j)))

; Gets words in dict which have character c at index i
(defn subdict [dict i c] (filter #(= (get % i) c) dict))

; Searches board recursively for boggle words in dict
(defn search-board [prev used i j dict board]
  (map #(apply (fn [ni nj]
    (let
      [ c (aget board ni nj)
        w (join [prev (str c)])
        subd (subdict dict (count prev) c)
        keeper (and (not (empty? subd)) (= (first subd) w))
        psubd (if keeper (rest subd) subd)
        sub-matches (if-not (empty? psubd) (search-board w (conj used [ni nj]) ni nj psubd board) []) ]
      (keep identity (conj sub-matches (when keeper (first subd)))))) %)
    (neighbors i j N used)))

; Wrapper around search-board for a single start position i j
(defn search-from-ij [i j dict board]
  (let [c (aget board i j)] (flatten (search-board (str c) #{[i j]} i j (subdict dict 0 c) board))))

; Loops over start positions and writes the found words from each
(doseq [i (range N) j (range N)]
  (let [matches (map #(join ["Found Word: " %]) (search-from-ij i j dict board))]
    (println (format "Starting %d %d" i j))
    (when-not (empty? matches) (println (join \newline matches)))))
