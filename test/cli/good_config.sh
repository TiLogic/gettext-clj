#!/usr/bin/env bash
# shellcheck disable=SC2034  # used in sourcing scripts

# one from each plural group
locales=("jp_JP" "en_CA" "fr_CA" "ro_RO" "lt_LT" "uk_UK" "sk_SK" "pl_PL" "sl_SL" "ar_EG")

echo "Test locales: ${locales[*]}"

app_dir=test/gettext/resources/cljd
generated_dir=test/gettext/output/generated
translated_dir=test/gettext/resources/good
assets_dir="${generated_dir:?}"
output_dir=test/gettext/output
app_name=unit_test
app_version=1.0
pot_contact_email=unit_test@example.com
genrated_file_name=unit_test
