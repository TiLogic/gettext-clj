# gettext-clj

gettext-clj ( `gtclj` ) is a library that allows developers quickly and easily internationalize their [Clojure](https://github.com/clojure/clojure), [ClojureScript](https://github.com/clojure/clojurescript) and **[ClojureDart](https://github.com/Tensegritics/ClojureDart)** projects.

Using standard GNU [gettext](https://www.gnu.org/software/gettext/manual/gettext.html) utilities `gtclj` extracts translatable strings from `.clj` , `.cljc` , `.cljx` , `.cljs` and `.cljd` files into [Portable Object](https://www.gnu.org/software/gettext/manual/gettext.html#PO-Files) ( `po` , `pot` ) files. The `po` file format is widely supported by translation software, and many programming languages. Translated `po` files are parsed by `gtclj` into compact JSON (Clojure `map` ) files.

## Features

* pure Clojure (no external dependencies)
* simple Clojure `map` data format
* plural support for 121 languages (derrived from Unicode CLDR [data](https://unicode-org.github.io/cldr-staging/charts/41/supplemental/language_plural_rules.html))
* context support to allow for multiple translations of the same message (ui elements, homonyms, gender etc.)
    ```clojure
          {{"Queue" "File d'attente"}
            "tabs" {"Queue" "File"}}`
    ```

## Requirements

### `po` file creation

[gettext](https://www.gnu.org/software/gettext/)

[bash](https://www.gnu.org/software/bash/)

### `JSON` file creation

[bash](https://www.gnu.org/software/bash/)

### `gettext-clj` library

None. `gtclj` is a pure Clojure project, with no external dependencies.

## Quick Start

```bash
git clone https://github.com/TiLogic/gettext-clj
cd gettext-clj
```

### extract

```bash
# Extract po files (locales are optional)
bash cli/gtclj -e -s path/to/clj/project/dir -o path/to/output/dir fr_CA fr_FR de_DE
```

### parse

```bash
# Parse to JSON
bash cli/gtclj -e -s path/to/po/files -o path/to/output/dir
```

### `deps.edn`

```clojure
{:paths ["src"]
 :deps { ; ...
        tilogic/gettext-clj
        {:git/url "https://github.com/tensegritics/ClojureDart.git"
         :sha "fcf8f3b8b2937e8a93576041ef98b623bf3de96d"}}}
```

### clojure

```clojure
(ns my.namespace
  (:require
   [clojure.data.json :as json]
   [tilogic.gettext-clj :as gt]))

(def data (atom nil))
;; (def fr-ca {"Hello, world!" "Salut, monde!"
;;             "What a beautiful cat!" ["Quel beau chat!"  "Quels beaux chats!"]})

(gt/add-locale data :fr-ca (json/read-str (slurp "assets/fr-ca.json")))
(gt/set-locale data :fr-ca)

(def gettext (gt/gettext-fn data))
(def ngettext (gt/gettext-fn data))

(defn something
  []
  (gettext "Hello, world!")) ;; => "Salut, monde!"

(defn what-have-you
  []
  (ngettext "What a beautiful cat!" "What beautiful cats!" 17)) ;; => "Quels beaux chats!"
```

### `JSON` file / Clojure `map` format

```clojure
{"msgid" "msgstr"
 "a message" "a translated message"
 "msgid (plural)" ["msgstr[0]" "msgstr[1]" "msgstr[n]"]
 "a plural message" ["plural translation 0" "plural translation 1" "plural translation n"]
 "context" {"message" "translated message"}}
```

## FAQ

1. Do I have to use `po` and `JSON` files with `gtclj`?
  No. The `gtclj` library uses a Clojure `map` as as its data format. String extraction and `po` file parsing are optional.

2. Why not use `edn`?
`gtclj` was originally written for use with ClojureDart, which does not currently support `edn` .

## Development

### Running Tests

#### Clojure

 `clj -X:test`

#### ClojureScript

 `clj -M:test-cljs`

#### ClojureDart

 `sh test/cljd/run.sh`
