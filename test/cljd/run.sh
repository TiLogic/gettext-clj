#!/bin/sh

echo "Running gettext-clj ClojureDart tests..."

if ! [ -f bin/gettext.dart ]; then
  clj -M:test-cljd init
  rm test/gettext_test.dart
fi

# convert file path to ns
# shellcheck disable=SC2046 #re-splitting required
if ! clj -M:test-cljd compile $(
  find test/tilogic -type f -name '*_test.cljc' | sed -e 's/^test.//g; s/_/-/g; s/\//./g; s/.cljc//g'
); then
  # FIXME `compile` returns 0, even when compilation fails
  echo "ClojureDart test compilation failed"
  exit 125
fi

dart test
