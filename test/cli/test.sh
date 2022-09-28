#!/usr/bin/env bash
# macOS/Homebrew @see https://unix.stackexchange.com/a/566019

extract=0
json=0
hlp=0
clj_dir=test/cli/resources/clj
good_po_dir=test/cli/resources/good
bad_po_dir=test/cli/resources/bad
good_output_dir=test/cli/output/good
generated_dir=test/cli/output/generated
mkdir -p "${generated_dir:?}"
app_name=unit_test
app_version=1.0
pot_contact_email=unit_test@example.com
pot_file_name=unit_test
pot_file="${generated_dir:?}/${pot_file_name:?}.pot"
help_file="${generated_dir:?}/help-test.txt"
# one from each plural group
locales=("vi_VI" "en_CA" "fr_CA" "ro_RO" "lt_LT" "uk_UK" "sk_SK" "pl_PL" "sl_SL" "ar_EG")

echo "Test locales: ${locales[*]}"
echo "Running -h (help) tests..."

bash cli/gtclj -h >"${help_file:?}"

if ! diff -q cli/impl/help.txt "${help_file:?}"; then
  ((hlp = hlp + 1))
fi

echo "Running gtclj -p (extract) tests..."

if ! bash cli/gtclj -p -s "${clj_dir:?}" -o "${generated_dir:?}" -a "${app_name:?}" -e "${pot_contact_email:?}" -n "${pot_file_name:?}" -v "${app_version:?}" "${locales[@]}" >/dev/null 2>&1; then
  ((extract = extract + 1))
fi

for locale in "${locales[@]}"; do
  lower="${locale,,}"
  loc_name="${lower/_/-}"
  file="${generated_dir:?}/${loc_name:?}.po"

  if [[ ! -s "${file:?}" ]]; then
    ((extract = extract + 1))
    echo "Missing ${file:?}"
  fi

done

if [[ ! -s "${pot_file:?}" ]]; then
  ((extract = extract + 1))
  echo "Missing ${pot_file:?}"
fi

if ! diff -I '^"POT-Creation-Date:' -q "${good_output_dir:?}/pot.result" "${pot_file:?}"; then
  ((extract = extract + 1))
fi

echo "Running gtclj -j (json) tests..."

if ! bash cli/gtclj -j -s "${good_po_dir:?}" -o "${generated_dir:?}" >/dev/null 2>&1; then
  ((json = json + 1))
fi

if ! diff -q "${good_output_dir:?}/simple.result" "${generated_dir:?}/simple.json"; then
  ((json = json + 1))
fi

if ! diff -q "${good_output_dir:?}/complex.result" "${generated_dir:?}/complex.json"; then
  ((json = json + 1))
fi

if ! bash cli/gtclj -j -s "${good_po_dir:?}" -o "${generated_dir:?}" >/dev/null 2>&1; then
  ((json = json + 1))
fi

# Should fail
# missing -p or -j
if bash cli/gtclj -s "${clj_dir:?}" -o "${generated_dir:?}" >/dev/null 2>&1; then
  ((json = json + 1))
fi

# have both -p and -j
if bash cli/gtclj -p -j -s "${clj_dir:?}" -o "${generated_dir:?}" >/dev/null 2>&1; then
  ((json = json + 1))
fi

# missing -s
if bash cli/gtclj -j -o "${generated_dir:?}" >/dev/null 2>&1; then
  ((json = json + 1))
fi

# bad .po files
if bash cli/gtclj -j -s "${bad_po_dir:?}/acii.po" -o "${generated_dir:?}" >/dev/null 2>&1; then
  ((json = json + 1))
fi

if bash cli/gtclj -j -s "${bad_po_dir:?}/bom.po" -o "${generated_dir:?}" >/dev/null 2>&1; then
  ((json = json + 1))
fi

if bash cli/gtclj -j -s "${bad_po_dir:?}/duplicates.po" -o "${generated_dir:?}" >/dev/null 2>&1; then
  ((json = json + 1))
fi

if bash cli/gtclj -j -s "${bad_po_dir:?}/missing-msgid.po" -o "${generated_dir:?}" >/dev/null 2>&1; then
  ((json = json + 1))
fi

if bash cli/gtclj -j -s "${bad_po_dir:?}/missing-msgstr_n.po" -o "${generated_dir:?}" >/dev/null 2>&1; then
  ((json = json + 1))
fi

if bash cli/gtclj -j -s "${bad_po_dir:?}/missing-msgstr.po" -o "${generated_dir:?}" >/dev/null 2>&1; then
  ((json = json + 1))
fi

# cleanup
# rm -r "${generated_dir:?}"

if [[ $((hlp + json + extract)) -eq 0 ]]; then
  echo "✅ All tests succeeded"
else
  echo "❌ ${hlp:?} gtclj -h (help), ${extract:?} -p (extract) and ${json:?} -j (json) test(s) failed"
  exit 125
fi
