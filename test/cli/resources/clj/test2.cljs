(ns cli.resources.clj.test2)

(declare gettext)
(declare ngettext)

(def f
  (gettext "gettext f"))

(defn g
  []
  (gettext "gettext g"))

(def h
  (ngettext "ngettext %s h" "ngettext %s h's" 1))


(defn i
  []
  (ngettext "ngettext %s i" "ngettext %s i's" 2))

(defn j
  []
  (ngettext "ngettext %s j" "ngettext %s j's" 216))
