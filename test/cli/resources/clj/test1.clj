(ns cli.resources.clj.test1)

(declare gettext)
(declare ngettext)

;; TRANSLATORS will not be detected (must go on line aboge gettext call)
(def a
  ;; TRANSLATORS this is a message for you
  (gettext "gettext a"))

(defn b
  []
  (gettext "gettext b"))

(def c
  (ngettext
   "ngettext %s c"
   "ngettext %s c's" 1))


(defn d
  []
  (ngettext "ngettext %s d"
            "ngettext %s d's"
            2))

(defn e
  []
  (ngettext "ngettext %s e" "ngettext %s e's" 11))
