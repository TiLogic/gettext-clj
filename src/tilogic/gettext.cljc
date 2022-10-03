(ns tilogic.gettext
  (:require
   [tilogic.impl.plurals :as p]
   [tilogic.impl.lang :as l]))

(def supported-languages
  "`set` of all language `keyword`s supported by gettext-clj."
  (set (keys p/plurals)))

(defn add-locale
  "Add locale `kw` and translations map `m` to available locales. 
  Returns updated translations `map` on success or `nil` on error."
  [data locale m]
  (when-let [lang (l/language locale)]
    (and (contains? supported-languages lang)
         (map? m))
    (swap! data assoc-in [:translations locale] m)))

(defn available-locales
  "Get all available locales as a `keyword` `list`."
  [data]
  (keys (:translations @data)))

(defn get-locale
  "Get the current locale `keyword`"
  [data]
  (:locale @data))

(defn language
  "Get the language `keyword` for the current locale."
  [data]
  (:language @data))

(defn plural-fn
  "Get the plural `function` for the current locale."
  [data]
  (p/plural-fn (:locale @data)))

(defn remove-locale
  "Remove locale `kw` from available locales."
  [data locale]
  (swap! data update :translations dissoc locale))

(defn set-locale
  "Sets the language/locale used by `gettext` functions. `locale` should a `keyword` the form: `:ll` or `:ll-cc`, where `ll` is an ISO 639-1 2-letter language code and `cc` is an ISO 3166-1 country code. E.g. `:nl` `:fr-ca`, `:pt-br`, `:zh-tw`. Returns `nil` on error."
  [data locale]
  (when-let [lang (l/language locale)]
    (when (contains? supported-languages lang)
      (swap! data assoc :language lang :locale locale))))

(defn supported-locale?
  "Determine if a locale `keyword` is supported by gettext-clj"
  [locale]
  (contains? supported-languages (l/language locale)))

(defn translations
  "Get the translations `map` for all available locales."
  [data]
  (:translations @data))

;; @performance
;; https://www.tiagoespinha.net/2016/12/clojure-beware-of-the-partial/
;; unroll get-in?  https://stackoverflow.com/a/36803237
(defn gettext-fn
  [data]
  (fn [msgid & [locale]]
    ;; if no translation found return the original english string
    (let [d @data]
      (get-in d [:translations (or locale (:locale d)) msgid] msgid))))

(defn pgettext-fn
  [data]
  (fn [msgctxt msgid & [locale]]
    (let [d @data]
      (get-in d [:translations (or locale (:locale d)) msgctxt msgid] msgid))))

(defn ngettext-fn
  [data]
  (fn [msgid1 msgid2 n & [locale]]
    ;; @see https://www.gnu.org/software/gettext/manual/gettext.html#Plural-forms
    (if (nat-int? n)
      (let [d @data
            loc (or locale (:locale d))
            plurals (get-in d [:translations loc msgid1])]
        (or (get plurals ((p/plural-fn loc) n))
            ;; if no translation found return the appropriate english string
            (get [msgid1 msgid2] (p/en-plural n) msgid1)))
      msgid1)))

(defn npgettext-fn
  [data]
  (fn [msgctxt msgid1 msgid2 n & [locale]]
    (if (nat-int? n)
      (let [d @data
            loc (or locale (:locale d))
            plurals (get-in d [:translations loc msgctxt msgid1])]
        (or (get plurals ((p/plural-fn loc) n))
            (get [msgid1 msgid2] (p/en-plural n) msgid1)))
      msgid1)))
