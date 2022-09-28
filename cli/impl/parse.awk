#!/usr/bin/awk -f

# TODO could use a refactor...

# Builds JSON structure from a gettext '.po' file
# Format: {msgid: msgstr,
#          msgid: [msgstr0, msgstr1, msgstrN],
#          msgctxt: {msgid: msgstr,
#                    msgid: [msgstr0, msgstr1, msgstrN]}}

# RESOURCES:
# https://www.gnu.org/software/gawk/manual/html_node/String-Functions.html
# https://ss64.com/osx/awk.html
# https://www.math.utah.edu/docs/info/gawk_12.html#SEC117
# https://amp-blog.robertelder.org/intro-to-awk-command/
# https://www.gnu.org/software/gettext/manual/gettext.html#Plural-forms

# @note match: <"stuff \" \n stuff"> but not <"stuff \" \n stuff" something "more stuff">
# @see https://stackoverflow.com/a/65966080
# regex literal: "(\\.|[^"])*"
# regex string: \"(\\\\.|[^\"])*\"

function capture(text, rgx_str) {
 if (match(text, rgx_str)) {
     return substr(text, RSTART, RLENGTH)
  }
}

BEGIN {
  msgctxt_rgx = "[[:blank:]]*msgctxt[[:blank:]]+\"(\\\\.|[^\"])*\""
  msgid_rgx = "[[:blank:]]*msgid[[:blank:]]+\"(\\\\.|[^\"])*\""
  msgstr_rgx = "[[:blank:]]*msgstr[[:blank:]]+\"(\\\\.|[^\"])*\""
  msgid_pl_rgx = "[[:blank:]]*msgid_plural[[:blank:]]+\"(\\\\.|[^\"])*\""
  msgstr_n_rgx_pfx = "[[:blank:]]*msgstr\\["
  msgstr_n_rgx_sfx = "\\][[:blank:]]*\"(\\\\.|[^\"])*\""
  quoted_rgx = "\"(\\\\.|[^\"])*\""
  # msgstr_n_max = 6
  msgstr_n_max = 5
}
{
  # only process msgctxt / msgsid 'blocks'
  if ($0 ~ /^[[:blank:]]*msgctxt[[:blank:]]+/) {
    ctx_block = 1
  } else if  (!ctx_block && $0 ~ /^[[:blank:]]*msgid[[:blank:]]+/) {
    msg_block = 1
  } else if ($0 ~ /^[[:space:]]*$/) {
    ctx_block = 0
    msg_block = 0
    next
  }

  if (ctx_block || msg_block) {
    # normalize
    gsub(/^[[:cntrl:]*[:space:]]+|[[:cntrl:]*[:space:]]+$/,"")

    # compact blank strings (enables unwrapping of wrapped lines)
    gsub(/"[[:blank:]]+"/, "\"\"")

    if (ctx_block) {
      if ($0 ~ /^msgctxt[[:blank:]]+/) {
        # add blank line so we can split on context blocks
        message_blocks = message_blocks "\n\n" $0
      } else {
        message_blocks = message_blocks " " $0
      }
    } else if (msg_block) {
      if ($0 ~ /^msgid[[:blank:]]+/) {
        # add blank line so we can split on message blocks
        message_blocks = message_blocks "\n\n" $0
      } else {
        message_blocks = message_blocks " " $0
      }
    }
  }
}
END {
  split(message_blocks, blocks, "\n\n");

  for (blk in blocks) {
    msgctxt = ""
    msgid = ""
    msgstr = ""
    message = ""

    # skip header
    header = blocks[blk]
    if (header ~ /Project-Id-Version|Report-Msgid-Bugs-To|POT-Creation-Date|PO-Revision-Date|Last-Translator|Language-Team|Plural-Forms:[[:blank:]]+/) {
      continue
    }

    # remove line wrapping (gettext default)
    unwrapped = header
    gsub(/"[[:blank:]]+"/, "", unwrapped)

    ctx_line = capture(unwrapped, msgctxt_rgx)
    if (ctx_line) {
      msgctxt = capture(ctx_line, quoted_rgx)
    }

    msg_line = capture(unwrapped, msgid_rgx)
    msgid = capture(msg_line, quoted_rgx)
    msgstr_line = capture(unwrapped, msgstr_rgx)
    msgid_pl_line = capture(unwrapped, msgid_pl_rgx)

    if (msgstr_line) {
      msgstr = capture(msgstr_line, quoted_rgx)
      if (msgstr == "\"\"") {
        # skip untranslated singular messages
        continue
      }

      if (msgctxt) {
        if (contexts[msgctxt]) {
          contexts[msgctxt] = contexts[msgctxt] ","
        }

        contexts[msgctxt] = contexts[msgctxt] msgid ":"  msgstr
      } else {
        if (json_str) {
          json_str = json_str "," msgid ":" msgstr
        } else {
          json_str = msgid ":" msgstr
        }
      }
    } else if (msgid_pl_line) {
      for(i = 0; i < msgstr_n_max; i++) {
        msgstr_n_line = capture(unwrapped, msgstr_n_rgx_pfx i msgstr_n_rgx_sfx)
        if (msgstr_n_line) {
          msgstr_n = capture(msgstr_n_line, quoted_rgx)
          if (msgstr_n == "\"\"") {
            # @note cannot skip untranslated plural messages as it changes vector length
            msgstr_n = "null"
          }
          if (i == 0) {
            message = msgid ":[" msgstr_n
          } else {
            message = message "," msgstr_n
          }
        }
      }

      if (msgctxt && message) {
        if (contexts[msgctxt]) {
          contexts[msgctxt] = contexts[msgctxt] ","
        }

        contexts[msgctxt] = contexts[msgctxt] message "]"
      } else if (message) {
        if (json_str) {
          json_str = json_str "," message "]"
        } else {
          json_str = message "]"
        }
      }
    }
  } # end blocks loop

  for (c in contexts) {
    if (json_str) {
      json_str = json_str ","
    }

    json_str = json_str c ":{" contexts[c] "}"
  }

  print "{" json_str "}"
}
