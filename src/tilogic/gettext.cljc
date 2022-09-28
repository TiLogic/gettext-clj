(ns tilogic.gettext
  (:require
   [tilogic.gettext.plurals :as p]
   [tilogic.gettext.impl :as i]))

(defn add-locale
  "Add locale `kw` and translations map `m` to available locales. 
  Returns updated translations `map` on success or `nil` on error."
  [kw m]
  (when (and (keyword? kw)
             (map? m))
    (swap! i/state assoc-in [:translations kw] m)))

(defn available-locales
  "Get all available locales as a `keyword` `list`."
  []
  (keys (:translations @i/state)))

(defn get-locale
  "Get the current locale `keyword`"
  []
  (:locale @i/state))

(defn language
  "Get the language `keyword` for the current locale."
  []
  (:language @i/state))

(defn plural-fn
  "Get the plural `function` for the current locale."
  []
  (:plural-fn @i/state))

(defn supported-locales
  "Get `list` of all language `keyword`s supported by gettext-clj."
  []
  (keys p/plurals))

(defn translations
  "Get the translations `map` for all available locales."
  []
  (:translations @i/state))

(defn set-locale
  "Sets the language/locale used by `gettext` functions. `locale` should a `keyword` the form: `:ll-cc`, where `ll` is an ISO 639-1 2-letter language code and `cc` is an ISO 3166-1 country code. E.g. `:fr-ca`, `:pt-br`, `:zh-tw`. Returns `nil` on error."
  [locale]
  (when (keyword? locale)
    (when-let [lang (i/language locale)]
      (swap! i/state
             assoc
             :language lang
             :locale locale
             :plural-fn
             ;; Portuguese is the only language with a locale-specific plural function
             (p/plural-fn (if (= locale :pt-pt)
                            :pt-pt
                            lang))))))

;; @performance
;; https://www.tiagoespinha.net/2016/12/clojure-beware-of-the-partial/
;; unroll get-in?  https://stackoverflow.com/a/36803237
(defn gettext
  [msgid & [locale]]
  (get-in @i/state [:translations (or locale (:locale @i/state)) msgid] msgid))

(defn pgettext
  [msgctxt msgid & [locale]]
  ;; if no translation found return the original (english) string
  (get-in @i/state [:translations (or locale (:locale @i/state)) msgctxt msgid] msgid))

(defn ngettext
  [msgid1 msgid2 n & [locale]]
  (let [index ((:plural-fn @i/state) n)
        plurals (get-in @i/state [:translations (or locale (:locale @i/state)) msgid1])]
    (or (get plurals index)
        ;; if no translation found return the original pluralized (english) string
        (if (> index 0)
          msgid2
          msgid1))))

(defn npgettext
  [msgctxt msgid1 msgid2 n & [locale]]
  (let [index ((:plural-fn @i/state) n)
        plurals (get-in @i/state [:translations (or locale (:locale @i/state)) msgctxt msgid1])]
    (or (get plurals index)
        ;; if no translation found return the original pluralized (english) string
        (if (> index 0)
          msgid2
          msgid1))))
