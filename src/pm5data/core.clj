(ns pm5data.core
  (:gen-class)
  (:require [clojure.java.io :as io]))

(defn read-file [file-path]
  (with-open [reader (io/input-stream file-path)]
    (let [length (.length (io/file file-path))
          buffer (byte-array length)]
      (.read reader buffer 0 length)
      buffer)))

(defn read-log-bin []
  (read-file (io/file "LogDataStorage.bin")))

(defn read-byte [content index]
  (get content (inc index)))

(defn read-word [content index]
  (+ (* 256 (get content index)) (get content (inc index))))

(defn decode []
  (let [content (pm5data.core/read-log-bin)]
    {:time-of-day [{:hour   (read-byte content 0x09)
                    :minute (read-byte content 0x0a)}
                   ]
     :results
     (for [i (range 4)]
       (let [offset (* 32 i)]
         {:total-tenths-second (read-word content (+ 0x16 offset))
          :total-meters        (read-word content (+ 0x1a offset))
          :meters              (read-word content (+ 0x12 offset))

          :strokes-per-minute  (read-word content (+ 0x14 offset))})
       )}))

#_ (decode)
