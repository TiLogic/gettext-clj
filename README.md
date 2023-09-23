# gettext-clj

gettext-clj ( `gtclj` ) is a library that allows developers quickly and easily internationalize their [Clojure](https://github.com/clojure/clojure), [ClojureScript](https://github.com/clojure/clojurescript) and **[ClojureDart](https://github.com/Tensegritics/ClojureDart)** projects.

Using standard GNU [gettext](https://www.gnu.org/software/gettext/manual/gettext.html) utilities `gtclj` extracts translatable strings from `.clj` , `.cljc` , `.cljx` , `.cljs` and `.cljd` files into [Portable Object](https://www.gnu.org/software/gettext/manual/gettext.html#PO-Files) ( `po` , `pot` ) files. The `po` file format is widely supported by translation software, and many programming languages. Translated `po` files are parsed by `gtclj` into compact, Clojure friendly JSON.

## Features

* pure Clojure (no external dependencies)
* simple `map` data format
* plural support for 121 languages (derrived from Unicode CLDR [data](https://unicode-org.github.io/cldr-staging/charts/41/supplemental/language_plural_rules.html))
* context support to allow for multiple translations of the same message (ui elements, homonyms, gender etc.)
  * e.g.`{{"Queue" "File d'attente"} "tab" {"Queue" "File"}}`
* fallback to a language other than english with a simple `merge`
  * e.g.`(merge (json/read-str (slurp "be-nl.json")) (json/read-str (slurp "be-fr.json")))`

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
bash cli/gtclj -p -s path/to/clj/project/dir -o path/to/output/dir fr_CA fr_FR de_DE
```

### parse

```bash
# Parse to JSON
bash cli/gtclj -j -s path/to/po/files -o path/to/output/dir
```

### clojure

```clojure
;; deps.edn
{:paths ["src"]
 :deps {,,,
        tilogic/gettext-clj
        {:git/url "https://github.com/tensegritics/ClojureDart.git"
         :sha "fcf8f3b8b2937e8a93576041ef98b623bf3de96d"}}}
```

```clojure
(ns my.namespace
  (:require
   [clojure.data.json :as json]
   [tilogic.gettext-clj :as gt]))

(def data (atom nil))
(def fr-ca {"Hello, world!" "Salut, monde!"
            "What a beautiful cat!" ["Quel beau chat!"  "Quels beaux chats!"]
            "Duck!" "Baisser la tête!"
            "She found a bat in her basement." ["Elle a trouvé un bâton de baseball dans son sous-sol." "Elle a trouvé des bâtons de baseball dans son sous-sol."]
            "bird" {"Duck!" "Canard!"}
            "mammal" {"She found a bat in her basement." ["Elle a trouvé une chauve-souris dans son sous-sol." "Elle a trouvé %s chauves-souris dans son sous-sol."]}})

(gt/add-locale data :jp (json/read-str (slurp "assets/jp.json")))
(gt/add-locale data :fr-ca fr-ca)
(gt/set-locale data :fr-ca)

(def gettext (gt/gettext-fn data))
(def ngettext (gt/gettext-fn data))
(def pgettext (gt/gettext-fn data))
(def npgettext (gt/gettext-fn data))

(gettext "Hello, world!") ;; => "Salut, monde!"

(ngettext "What a beautiful cat!" "What beautiful cats!" 17) ;; => "Quels beaux chats!"

(pgettext "bird" "Duck!") ;; => "Canard!"

(format (npgettext "mammal"
                    "She found a bat in her basement."
                    "She found %s bats in her basement."
                    2) 2) ;; => "Elle a trouvé 2 chauves-souris dans son sous-sol."

(pgettext "bird" "Duck!" :jp) ;; => "鴨!"
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

### Does Clojure really need another translation library?

No (and yes!). There are many fine internationalization libraries[^1], but (currently) none of them work with [ClojureDart](https://github.com/Tensegritics/ClojureDart) — hence, the existence of `gettext-clj`.

### Why `gettext`?

`gettext`'s `.po` file format is widely used by [translation software](https://www.google.com/search?q=po+file+editor), and can be easily converted to other formats if required.

### Do I have to use `po` and `JSON` files with `gtclj`?

No. The `gtclj` library uses a Clojure `map` as as its data format. String extraction and `po` file parsing are optional.

### Why `JSON` instead of `edn`?

ClojureDart does not currently support `edn`.

[^1]: [Tempura](https://github.com/ptaoussanis/tempura), [Tongue](https://github.com/tonsky/tongue) and [Pottery](https://github.com/brightin/pottery) to name a few.

## Development

### Running Tests

#### Clojure

 `clj -X:test`

#### ClojureScript

 `clj -M:test-cljs`

#### ClojureDart

 `sh test/cljd/run.sh`

#### cli

 `bash test/cli/run.sh`
