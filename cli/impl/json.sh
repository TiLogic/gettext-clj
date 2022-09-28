#!/usr/bin/env bash
# shellcheck disable=SC2154 # variables defined in sourcing file
set -euC -o pipefail
shopt -s inherit_errexit

exit_code=0

# RESOURCES:
# https://www.gnu.org/software/gawk/manual/html_node/String-Functions.html
# https://ss64.com/osx/awk.html
# https://www.math.utah.edu/docs/info/gawk_12.html#SEC117
# https://amp-blog.robertelder.org/intro-to-awk-command/
# https://www.gnu.org/software/gettext/manual/gettext.html#Plural-forms

echo "Converting gettext .po files to JSON"

readarray -t po_files < <(find "${source_dir}" -type f -name '*.po')

for file in "${po_files[@]}"; do
  # validate file
  encoding="$(file -b --mime-encoding "${file}")"

  if [[ "${encoding}" != "utf-8" ]]; then
    echo "ERROR ${file} - '${encoding}' file encoding is invalid (must be 'utf-8')"
    exit_code=125
    continue
  else
    # remove BOM @see https://stackoverflow.com/a/1068700
    file_contents="$(awk 'NR==1 {sub(/^\xef\xbb\xbf/,"")} 1' "${file}")"

    # echo "${validated_file}"
    # @see https://www.gnu.org/software/gettext/manual/html_node/msgfmt-Invocation.html
    if ! msgfmt -o /dev/null - <<<"${file_contents}"; then
      echo "ERROR gettext/json.sh - ${file} is invalid"
      exit_code=125
      continue
    fi
  fi

  po_json="$(awk -f cli/impl/parse.awk "${file}")"

  # only create file if json is not empty
  if [[ "${po_json}" != "{}" ]]; then
    locale_name="$(basename "${file}" ".po")"
    # @see https://stackoverflow.com/a/15988793
    lang="${locale_name%-*}"
    locale="${locale_name#*-}"
    # use base language as name when it's the same as locale (gettext uses: lang_LOCALE)
    if [[ "${lang}" == "${locale}" ]]; then
      file_name="${lang}"
    else
      file_name="${locale_name}"
    fi

    echo "${po_json}" >|"${output_dir}/${file_name}.json"
  fi
done

exit "${exit_code}"
