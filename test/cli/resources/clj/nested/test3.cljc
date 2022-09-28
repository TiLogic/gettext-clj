(ns cli.resources.clj.nested.test3)

(declare gettext)
(declare ngettext)
(declare pgettext)
(declare npgettext)

(def k
  (gettext "gettext k          "))

(defn l
  []
  (gettext
   "gettext l"))

(def m
  (ngettext "ngettext %s m  (plural)   " "ngettext %s m's  (plural)   " 1))


(defn n
  []
  (ngettext "ngettext %s n" "ngettext %s n's (plural)" 2))

(defn o
  []
  (ngettext "ngettext %s o" "ngettext %s o's (plural)" 99))

(def p
  (gettext "gettext p - A string that is longer than 80 characters so that gettext will wrap the line."))

(def q
  (gettext "gettext q - A string that is extra long so that gettext will wrap the line multiple times. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."))

(def r
  (ngettext "ngettext r - A string that is longer than 80 characters so that ngettext will wrap the line."
            "ngettext r - A string that is longer than 80 characters so that ngettext will wrap the line. (plural)"
            123456))

(def s
  ;; TRANSLATORS this is a message for you
  (ngettext "ngettext r - A string that is extra long so that ngettext will wrap the line multiple times. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
            "ngettext r - A string that is extra long so that ngettext will wrap the line multiple times. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
            1))

(def t
  (pgettext "pgettext t context" "pgettext t"))

(def u
  (npgettext "npgettext n context"
             "npgettext u message"
             "npgettext u plural" 44))
