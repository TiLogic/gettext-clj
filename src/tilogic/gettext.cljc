(ns tilogic.gettext
  (:require
   [tilogic.gettext.plurals :as p]
   [tilogic.gettext.impl :as i]))

(def supported-languages
  "`set` of all language `keyword`s supported by gettext-clj."
  (set (keys p/plurals)))

(defn add-locale
  "Add locale `kw` and translations map `m` to available locales. 
  Returns updated translations `map` on success or `nil` on error."
  [locale m]
  (when-let [lang (i/language locale)]
    (and (contains? supported-languages lang)
         (map? m))
    (swap! i/state assoc-in [:translations locale] m)))

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
  (p/plural-fn (:locale @i/state)))

(defn remove-locale
  "Remove locale `kw` from available locales."
  [locale]
  (swap! i/state update :translations dissoc locale))

(defn set-locale
  "Sets the language/locale used by `gettext` functions. `locale` should a `keyword` the form: `:ll` or `:ll-cc`, where `ll` is an ISO 639-1 2-letter language code and `cc` is an ISO 3166-1 country code. E.g. `:nl` `:fr-ca`, `:pt-br`, `:zh-tw`. Returns `nil` on error."
  [locale]
  (when-let [lang (i/language locale)]
    (when (contains? supported-languages lang)
      (swap! i/state assoc :language lang :locale locale))))

(defn supported-locale?
  "Determine if a locale `keyword` is supported by gettext-clj"
  [locale]
  (contains? supported-languages (i/language locale)))

(defn translations
  "Get the translations `map` for all available locales."
  []
  (:translations @i/state))

;; @performance
;; https://www.tiagoespinha.net/2016/12/clojure-beware-of-the-partial/
;; unroll get-in?  https://stackoverflow.com/a/36803237
(defn gettext
  [msgid & [locale]]
  ;; if no translation found return the original english string
  (get-in @i/state [:translations (or locale (:locale @i/state)) msgid] msgid))

(defn pgettext
  [msgctxt msgid & [locale]]
  (get-in @i/state [:translations (or locale (:locale @i/state)) msgctxt msgid] msgid))

(defn ngettext
  [msgid1 msgid2 n & [locale]]
  (if-not (number? n)
    msgid1
    (let [loc (or locale (:locale @i/state))
          plurals (get-in @i/state [:translations loc msgid1])]
      (or (get plurals ((p/plural-fn loc) n))
          ;; if no translation found return the appropriate english string
          (get [msgid1 msgid2] (p/en-plural n) msgid1)))))

(defn npgettext
  [msgctxt msgid1 msgid2 n & [locale]]
  (if-not (number? n)
    msgid1
    (let [loc (or locale (:locale @i/state))
          plurals (get-in @i/state [:translations loc msgctxt msgid1])]
      (or (get plurals ((p/plural-fn loc) n))
          (get [msgid1 msgid2] (p/en-plural n) msgid1)))))
