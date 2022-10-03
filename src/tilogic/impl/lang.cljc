(ns tilogic.impl.lang
  (:require
   [clojure.string :as str]))

;; @note assume hans for CN, SG, hant for TW, HK, MO unless specified
;; simplified chinese
(def ^:private zh-hans
  #{"zh-cn"
    "zh-sg"
    "zh-hans"
    "zh-hans-cn"
    "zh-hans-sg"
    "zh-hans-tw"
    "zh-hans-hk"
    "zh-hans-mo"})

;; traditional chinese
(def ^:private zh-hant
  #{"zh" ; @default if no locale set
    "zh-tw"
    "zh-hk"
    "zh-mo"
    "zh-hant"
    "zh-hant-tw"
    "zh-hant-hk"
    "zh-hant-mo"
    "zh-hant-cn"
    "zh-hant-sg"})

;; Determine the base language from a given locale `kw`. Returns `keyword` or `nil` if not a valid locale. Keyword should be in the form: `:ll-cc`, where `ll` is an ISO 639-1 2-letter language code and `cc` is an ISO 3166-1 country code. E.g. `:fr-ca`, `:pt-br`, `:zh-tw`
(defn ^:internal language
  [kw]
  (when (keyword? kw)
    ;; Portuguese has a locale-specific plurals, thus is considered a 'base language'
    (if (= kw :pt-pt)
      :pt-pt
      (let [locale (name kw)]
        (if (or (contains? zh-hans locale)
                ;; zh-hans-us etc. are possible
                (str/starts-with? locale "zh-hans"))
          :zh-hans
          (if (or (contains? zh-hant locale)
                  (str/starts-with? locale "zh-hant"))
            :zh-hant
            (if (str/includes? locale "-")
              (-> locale
                  (str/split #"-")
                  first
                  ((fn [loc]
                     (when (and (string? loc)
                                (not (str/blank? loc)))
                       (keyword loc)))))
              kw)))))))
