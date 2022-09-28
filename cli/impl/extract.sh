#!/usr/bin/env bash
# shellcheck disable=SC2154 # variables defined in sourcing file
set -euC -o pipefail
shopt -s inherit_errexit

for ((i = "${first_locale}"; i < "${#all_args[@]}"; i++)); do
  locales+=("${all_args[${i}]}")
done

echo "Extracting gettext translations..."

# @see https://unix.stackexchange.com/a/15337
readarray -t cljd_files < <(find "${source_dir}" -type f -name '*.clj' -o -name '*.cljc' -o -name '*.cljs' -o -name '*.cljx' -o -name '*.cljd')

# create .pot file
# @note duplicates are combined in .pot file (but comments are not)
# e.g.  src/app/presentation/layout/shared.cljd:25
#       src/app/presentation/search.cljd:35
#       msgid "Search"
#       msgstr ""
# @note Clojure is not available in gettext, but setting langauge as Lisp ensures translatable strings are extracted correctly
# e.g.  `(gettext "whatever")`
#       `(ngettext "One day." "Two days" 2))`
#       `(ngettext "It will take %s day." "It will take %s days." 26))`
#       `(format (ngettext "It will take %s day." "It will take %s days." 26)) 26)`
# @see https://www.labri.fr/perso/fleury/posts/programming/a-quick-gettext-tutorial.html, https://www.gnu.org/software/gettext/manual/gettext.html#Common-Lisp and https://www.gnu.org/software/gettext/manual/gettext.html#lisp_002dformat
xgettext --package-name="${app_name}" --package-version="${app_version}" --msgid-bugs-address="${pot_email}" --keyword="pgettext:1c,2" --keyword="npgettext:1c,2,3" --add-comments="${comment_keyword}" -L Lisp -o "${pot_file}" "${cljd_files[@]}"

if [[ "${#locales[@]}" -eq 0 ]]; then
  echo "WARN: No locale argument(s), only '${pot_name}.pot' will be created."
else
  for locale in "${locales[@]}"; do
    lowercase="${locale,,}"
    locale_name="${lowercase//_/-}"

    if [[ -f "${pot_file}" ]]; then
      msginit --no-translator --input="${pot_file}" --locale="${locale}" --output="${output_dir}/${locale_name}.po"
    fi
  done

fi

# TODO Possibly add support for dgettext, dngettext, dpgettext, dnpgettext by extracting into separate files...
# xgettext --package-name="${app_name}" --package-version="${app_version}" --msgid-bugs-address="${pot_email}" -k --keyword="dgettext:2" --keyword="dngettext:2,3" --keyword="dpgettext:2c,3" --keyword="dnpgettext:2c,3,4" --add-comments="${comment_keyword}" -L Lisp -o "${pot_file}" "${cljd_files[@]}"
