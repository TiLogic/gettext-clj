(ns tilogic.gettext.plurals)

;; @see https://unicode-org.github.io/cldr-staging/charts/41/supplemental/language_plural_rules.html, http://unicode.org/reports/tr35/tr35-numbers.html#Operands and https://unicode.org/reports/tr35/tr35-numbers.html#51-plural-rules-syntax 
;; "`and` binds more tightly than `or`. So X `or` Y `and` Z is interpreted as (X `or` (Y `and` Z))."

(defn- in-nat-subset?
  "Determine if integer `n` is within the subset of natural numbers {`start`, `start+1`,... `end`}."
  [n start end]
  (and (>= n start)
       (<= n end)))

;; none
(defn- plural1
  [_]
  0)

;; one: n = 1
(defn- plural2a
  [n]
  (if (= n 1)
    0
    1))

;; one: i = 0,1 / one: n = 0..1
(defn- plural2b
  [n]
  (if (in-nat-subset? n 0 1)
    0
    1))

;; one: i = 1,2,3 or i % 10 != 4,6,9
(defn- plural2c
  [n]
  (if (or (in-nat-subset? n 1 3)
          (and  (not= (mod n 10) 4)
                (not= (mod n 10) 6)
                (not= (mod n 10) 9)))
    0
    1))

;; one: i % 10 = 1 and i % 100 != 11
(defn- plural2d
  [n]
  (if (and (= (mod n 10) 1)
           (not= (mod n 100) 11))
    0
    1))

;; one: i % 10 = 1 and i % 100 != 11
;; few: i % 10 = 2..4 and i % 100 != 12..14 
(defn- plural3a
  [n]
  (if (and (= (mod n 10) 1)
           (not= (mod n 100) 11))
    0
    (if (and (in-nat-subset? (mod n 10) 2 4)
             (not (in-nat-subset? (mod n 100) 12 14)))
      1
      2)))

;; one: i = 1 and v=0
;; few: i = 2..4 and v=0
(defn- plural3b
  [n]
  (if (= n 1)
    0
    (if (in-nat-subset? n 2 4)
      1
      2)))

;; one: n % 10 = 1 and n % 100 != 11..19
;; few: n % 10 = 2..9 and n % 100 != 11..19
(defn- plural3c
  [n]
  (if (and (= (mod n 10) 1)
           (not (in-nat-subset? (mod n 100) 11 19)))
    0
    (if (and (in-nat-subset? (mod n 10) 2 9)
             (not (in-nat-subset? (mod n 100) 11 19)))
      1
      2)))

;; zero: n % 10 = 0 or n % 100 = 11..19
;; one: n % 10 = 1 and n % 100 != 11 
(defn- plural3d
  [n]
  (if (or (= (mod n 10) 0)
          (in-nat-subset? (mod n 100) 11 19))
    0
    (if (and (= (mod n 10) 1)
             (not= (mod n 100) 11))
      1
      2)))

;; one: i = 1 
;; few: n = 0 or n % 100 = 2..19
(defn- plural3e
  [n]
  (if (= n 1)
    0
    (if (or (= n 0)
            (in-nat-subset? (mod n 100) 2 19))
      1
      2)))

;; one: n % 10 = 1 and n % 100 != 11
;; few: n % 10 = 2..4 and n % 100 != 12..14
(defn- plural3f
  [n]
  (if (and (= (mod n 10) 1)
           (not= (mod n 100) 11))
    0
    (if (and (in-nat-subset? (mod n 10) 2 4)
             (not (in-nat-subset? (mod n 100) 12 14)))
      1
      2)))

;; one: i = 1 
;; few: i % 10 = 2..4 and i % 100 != 12..14
(defn- plural3g
  [n]
  (if (= n 1)
    0
    (if (and (in-nat-subset? (mod n 10) 2 4)
             (not (in-nat-subset? (mod n 100) 12 14)))
      1
      2)))


;; one: i % 10 = 1 and i % 100 != 11
;; few: i % 10 = 2..4 and i % 100 != 12..14
(defn- plural3h
  [n]
  (if (and (= (mod n 10) 1)
           (not= (mod n 100) 11))
    0
    (if (and (in-nat-subset? (mod n 10) 2 4)
             (not (in-nat-subset? (mod n 100) 12 14)))
      1
      2)))


;; one: n = 1,11 
;; two: n = 2,12
;; few: n = 3..10,13..19
(defn- plural4a
  [n]
  (if (or (= n 1)
          (= n 11))
    0
    (if (or (= n 2)
            (= n 12))
      1
      (if (or (in-nat-subset? n 3 10)
              (in-nat-subset? n 13 19))
        2
        3))))

;; one: n = 1
;; few: n = 0 or n % 100 = 2..10
;; many: n % 100 = 11..19
(defn- plural4b
  [n]
  (if (= n 1)
    0
    (if (or (= n 0)
            (in-nat-subset? (mod n 100) 2 10))
      1
      (if (in-nat-subset? (mod n 100) 11 19)
        2
        3))))

;; one: i % 100 = 1
;; two: i % 100 = 2
;; few: i % 100 = 3..4
(defn- plural4c
  [n]
  (if (= (mod n 100) 1)
    0
    (if (= (mod n 100) 2)
      1
      (if (in-nat-subset? (mod n 100) 3 4)
        2
        3))))

;; one: i = 1
;; two: i = 2
;; many: n != 0..10 and n % 10 = 0
(defn- plural4d
  [n]
  (if (= n 1)
    0
    (if (= n 2)
      1
      (if (and (not (in-nat-subset? n 0 10))
               (= (mod n 10) 0))
        2
        3))))

;; one: n = 1
;; two: n = 2
;; few: n = 3..6
;; many: n = 7..10
(defn- plural5
  [n]
  (if (= n 1)
    0
    (if (= n 2)
      1
      (if (in-nat-subset? n 3 6)
        2
        (if (in-nat-subset? n 7 10)
          3
          4)))))

;; zero: n = 0
;; one: n = 1
;; two: n = 2
;; few: n % 100 = 3..10
;; many: n % 100 = 11..99
(defn- plural6
  [n]
  (if (= n 0)
    0
    (if (= n 1)
      1
      (if (= n 2)
        2
        (if (in-nat-subset? (mod n 100) 3 10)
          3
          (if (in-nat-subset? (mod n 100) 11 99)
            4
            5))))))

(def plurals
  {:bm plural1
   :bo plural1
   :dz plural1
   :id plural1
   :ig plural1
   :ii plural1
   :jp plural1
   :jv plural1
   :km plural1
   :ko plural1
   :lo plural1
   :ms plural1
   :my plural1
   :sg plural1
   :su plural1
   :kr plural1
   :th plural1
   :vi plural1
   :wo plural1
   :yue plural1
   :yo plural1
   :zh plural1
   :zh-hans plural1
   :zh-hant plural1
   :af plural2a
   :am plural2a
   :an plural2a
   :as plural2a
   :ast plural2a
   :az plural2a
   :bg plural2a
   :bn plural2a
   :ca plural2a
   :chr plural2a
   :da plural2a
   :de plural2a
   :en plural2a
   :el plural2a
   :et plural2a
   :es plural2a
   :eu plural2a
   :fa plural2a
   :fi plural2a
   :fo plural2a
   :fy plural2a
   :gl plural2a
   :gsw plural2a
   :gu plural2a
   :ha plural2a
   :hi plural2a
   :hu plural2a
   :ia plural2a
   :io plural2a
   :it plural2a
   :ka plural2a
   :kk plural2a
   :kl plural2a
   :kn plural2a
   :ku plural2a
   :ky plural2a
   :lb plural2a
   :lij plural2a
   :ml plural2a
   :mn plural2a
   :mr plural2a
   :nd plural2a
   :ne plural2a
   :nl plural2a
   :nn plural2a
   :no plural2a
   :or plural2a
   :pcm plural2a
   :ps plural2a
   :pt-pt plural2a
   :rm plural2a
   :sc plural2a
   :sd plural2a
   :scn plural2a
   :sq plural2a
   :so plural2a
   :sv plural2a
   :sw plural2a
   :ta plural2a
   :te plural2a
   :tr plural2a
   :tk plural2a
   :ug plural2a
   :ur plural2a
   :uz plural2a
   :xh plural2a
   :zu plural2a
   :bho plural2b
   :ff plural2b
   :fr plural2b
   :hy plural2b
   :ln plural2b
   :nso plural2b
   :pa plural2b
   :pt plural2b
   :si plural2b
   :ceb plural2c
   :fil plural2c
   :tl plural2c
   :is plural2d
   :bs plural3a
   :hr plural3a
   :sr plural3a
   :cs plural3b
   :sk plural3b
   :lt plural3c
   :lv plural3d
   :ro plural3e
   :be plural3f
   :pl plural3g
   :ru plural3h
   :uk plural3h
   :gd plural4a
   :mt plural4b
   :sl plural4c
   :he plural4d
   :ga plural5
   :ar plural6})

(defn plural-fn
  "Returns a function that finds the plural translation index of language `lang` parameter."
  [lang]
  ;; don't break ngettext if plural for the current locale doesn't exist
  (get plurals lang plural1))
