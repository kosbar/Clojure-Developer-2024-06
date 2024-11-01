(ns otus-02.homework.square-code
  (:require [clojure.string :as string])
  (:require [clojure.math :as math]))

;; Реализовать классический метод составления секретных сообщений, называемый `square code`.
;; Выведите закодированную версию полученного текста.

;; Во-первых, текст нормализуется: из текста удаляются пробелы и знаки препинания,
;; также текст переводится в нижний регистр.
;; Затем нормализованные символы разбиваются на строки.
;; Эти строки можно рассматривать как образующие прямоугольник при печати их друг под другом.

;; Например,
"If man was meant to stay on the ground, god would have given us roots."
;; нормализуется в строку:
"ifmanwasmeanttostayonthegroundgodwouldhavegivenusroots"

;; Разбиваем текст в виде прямоугольника.
;; Размер прямоугольника (rows, cols) должен определяться длиной сообщения,
;; так что c >= r и c - r <= 1, где c — количество столбцов, а r — количество строк.
;; Наш нормализованный текст имеет длину 54 символа
;; и представляет собой прямоугольник с c = 8 и r = 7:
"ifmanwas"
"meanttos"
"tayonthe"
"groundgo"
"dwouldha"
"vegivenu"
"sroots  "

;; Закодированное сообщение получается путем чтения столбцов слева направо.
;; Сообщение выше закодировано как:
"imtgdvsfearwermayoogoanouuiontnnlvtwttddesaohghnsseoau"

;; Полученный закодированный текст разбиваем кусками, которые заполняют идеальные прямоугольники (r X c),
;; с кусочками c длины r, разделенными пробелами.
;; Для фраз, которые на n символов меньше идеального прямоугольника,
;; дополните каждый из последних n фрагментов одним пробелом в конце.
"imtgdvs fearwer mayoogo anouuio ntnnlvt wttddes aohghn  sseoau "

;; Обратите внимание, что если бы мы сложили их,
;; мы могли бы визуально декодировать зашифрованный текст обратно в исходное сообщение:

"imtgdvs"
"fearwer"
"mayoogo"
"anouuio"
"ntnnlvt"
"wttddes"
"aohghn "
"sseoau "

(defn add-spaces [amount line]
  (apply str (concat line (repeat amount " "))))

(defn encode-string [input]
  ;; убираем знаки препинания, пробелы и переводим в нижний регистр
  ;; а после переводим строку в вектор (line)
    (let [inner (-> input
                  (string/lower-case)
                  (string/replace #"[^a-z]" ""))
          size (count inner)
          rows (int (math/sqrt (count inner)))
          columns (inc rows)
          line (->> inner
                    ;; добавляем пробелы к концу строки
                   (add-spaces (- (* columns rows) size))
                   (vec))]

      (string/join " " (map #(apply str %)
                            (partition rows
                                       (replace line
                                                (vec (loop [start 0
                                                            rows 0
                                                            l ()
                                                            max (count line)
                                                            step columns]
                                                       (if (> rows (dec step))
                                                         l
                                                         (recur (inc start) (inc rows) (concat l (range start max step)) max step))))))))))
