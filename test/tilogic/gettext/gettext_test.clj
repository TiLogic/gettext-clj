(ns  tilogic.gettext.gettext-test
  (:require
   [tilogic.gettext :as gt]
   [tilogic.gettext.impl :as i]
   [tilogic.gettext.plurals :as p]
   [clojure.test :refer [deftest is testing]]))

(deftest add-locale-test
  (testing "valid"
    (with-redefs [i/state (atom {:translations {:test {"1" "2"}}})]
      (gt/add-locale :zz {"z" "z"})
      (is (= {:translations {:test {"1" "2"}
                             :zz {"z" "z"}}}
             @i/state)))))

(deftest available-locales-test
  (testing "valid"
    (with-redefs [i/state (atom {:translations {:test {"1" "2"}
                                                :zz {"z" "z"}}})]
      (is (= '(:test :zz)
             (gt/available-locales))))))

(deftest get-locale-test
  (testing "valid"
    (with-redefs [i/state (atom {:locale :test})]
      (is (= :test
             (gt/get-locale))))))

(deftest language-test
  (testing "valid"
    (with-redefs [i/state (atom {:language :test})]
      (is (= :test
             (gt/language))))))

(deftest plural-fn-test
  (testing "valid"
    (with-redefs [i/state (atom {:plural-fn identity})]
      (is (= identity
             (gt/plural-fn))))))

(deftest supported-locales-test
  (testing "valid"
    (is (= #{:fr :ha :pt-pt :gu :wo :jv :az :ka :el :fo :hr :es :io :pt :fi :ii :ga :nd :ff :tl :is :be :ug :lij :kl :ln :ca :as :ro :sl :or :nn :th :jp :yue :xh :tr :ur :et :cs :hu :sg :mr :uz :ku :scn :ast :kk :ko :so :kn :te :af :pl :lo :ia :am :su :it :ml :bho :en :nl :zh :bn :gd :lb :pa :mn :sk :nso :an :de :id :ar :zh-hant :hy :uk :yo :ta :ru :gl :pcm :bs :sc :sd :km :ig :fy :he :fil :sv :da :zu :sw :ne :dz :gsw :zh-hans :bm :my :bg :lt :ms :chr :lv :tk :mt :kr :eu :sr :si :hi :ceb :vi :no :ps :sq :bo :fa :rm :ky}
           (set (gt/supported-locales))))))

(deftest translations-test
  (testing "valid"
    (with-redefs [i/state (atom {:translations {:test {}}})]
      (is (= {:test {}}
             (gt/translations))))))

(deftest set-locale-test
  (testing "valid"
    (with-redefs [i/state (atom nil)
                  i/language (fn [_] :lang)
                  p/plural-fn (fn [kw] kw)]
      (gt/set-locale :fr-ca)
      (is (= {:locale :fr-ca
              :language :lang
              :plural-fn :lang}
             @i/state))
      (gt/set-locale :pt-pt)
      (is (= {:locale :pt-pt
              :language :lang
              :plural-fn :pt-pt}
             @i/state)))))

(deftest gettext-test
  (testing "valid"
    (with-redefs [i/state (atom {:locale :test
                                 :translations
                                 {:test {"message" "translation"}
                                  :dynamic {"dmessage" "dtranslation"}}
                                 :plural-fn identity})]
      (is (= "translation"
             (gt/gettext "message")))
      (is (= "untranslated"
             (gt/gettext "untranslated")))
      (is (= "dtranslation"
             (gt/gettext "dmessage" :dynamic)))
      (is (= "duntranslated"
             (gt/gettext "duntranslated" :dynamic)))
      (is (= nil
             (gt/gettext nil nil))))))

(deftest pgettext-test
  (testing "valid"
    (with-redefs [i/state (atom {:locale :test
                                 :translations
                                 {:test {"context" {"message" "translation"}}
                                  :dynamic {"dcontext" {"dmessage" "dtranslation"}}}
                                 :plural-fn identity})]
      (is (= "translation"
             (gt/pgettext "context" "message")))
      (is (= "untranslated"
             (gt/pgettext "context" "untranslated")))
      (is (= "message"
             (gt/pgettext "doesnotexist" "message")))
      (is (= "dtranslation"
             (gt/pgettext "dcontext" "dmessage" :dynamic)))
      (is (= "duntranslated"
             (gt/pgettext "dcontext" "duntranslated" :dynamic)))
      (is (= "dmessage"
             (gt/pgettext "doesnotexist" "dmessage" :dynamic)))
      (is (= nil
             (gt/pgettext "context" nil nil)))
      (is (= nil
             (gt/pgettext nil nil nil))))))

(deftest ngettext-test
  (testing "valid"
    (with-redefs [i/state (atom {:locale :test
                                 :translations
                                 {:test {"message with plurals" ["index 0 (singular)" "index 1 (plural)" "index 2 (plural)"]}
                                  :dynamic {"dmessage with plurals" ["dindex 0 (singular)" "dindex 1 (plural)" "dindex 2 (plural)"]}}
                                 :plural-fn identity})]
      (is (= "index 0 (singular)"
             (gt/ngettext "message with plurals" "plural message" 0)))
      (is (= "index 1 (plural)"
             (gt/ngettext "message with plurals" "plural message" 1)))
      (is (= "index 2 (plural)"
             (gt/ngettext "message with plurals" "plural message" 2)))
      (is (= "plural message"
             (gt/ngettext "message with plurals" "plural message" 99)))
      (is (= "untranslated"
             (gt/ngettext "untranslated" "untranslated plural" 0)))
      (is (= "untranslated plural"
             (gt/ngettext "untranslated" "untranslated plural" 1)))
      (is (= "dindex 0 (singular)"
             (gt/ngettext "dmessage with plurals" "dplural message" 0 :dynamic)))
      (is (= "dindex 1 (plural)"
             (gt/ngettext "dmessage with plurals" "dplural message" 1 :dynamic)))
      (is (= "dindex 2 (plural)"
             (gt/ngettext "dmessage with plurals" "dplural message" 2 :dynamic)))
      (is (= "dplural message"
             (gt/ngettext "dmessage with plurals" "dplural message" 99 :dynamic)))
      (is (= "duntranslated"
             (gt/ngettext "duntranslated" "duntranslated plural" 0 :dynamic)))
      (is (= "duntranslated plural"
             (gt/ngettext "duntranslated" "duntranslated plural" 1 :dynamic)))
      (is (= nil
             (gt/ngettext nil nil 1)))
      (is (thrown? ClassCastException
                   (gt/ngettext "throws if n" "is not a number" :one))))))

(deftest npgettext-test
  (testing "valid"
    (with-redefs [i/state (atom {:locale :test
                                 :translations
                                 {:test {"context" {"message with plurals" ["index 0 (singular)" "index 1 (plural)" "index 2 (plural)"]}}
                                  :dynamic {"dcontext" {"dmessage with plurals" ["dindex 0 (singular)" "dindex 1 (plural)" "dindex 2 (plural)"]}}}
                                 :plural-fn identity})]
      (is (= "index 0 (singular)"
             (gt/npgettext "context" "message with plurals" "plural message" 0)))
      (is (= "index 1 (plural)"
             (gt/npgettext "context" "message with plurals" "plural message" 1)))
      (is (= "index 2 (plural)"
             (gt/npgettext "context" "message with plurals" "plural message" 2)))
      (is (= "plural message"
             (gt/npgettext "context" "message with plurals" "plural message" 99)))
      (is (= "untranslated"
             (gt/npgettext "context" "untranslated" "untranslated plural" 0)))
      (is (= "untranslated plural"
             (gt/npgettext "context" "untranslated" "untranslated plural" 1)))
      (is (= "dindex 0 (singular)"
             (gt/npgettext "dcontext" "dmessage with plurals" "dplural message" 0 :dynamic)))
      (is (= "dindex 1 (plural)"
             (gt/npgettext "dcontext" "dmessage with plurals" "dplural message" 1 :dynamic)))
      (is (= "dindex 2 (plural)"
             (gt/npgettext "dcontext" "dmessage with plurals" "dplural message" 2 :dynamic)))
      (is (= "dplural message"
             (gt/npgettext "dcontext" "dmessage with plurals" "dplural message" 99 :dynamic)))
      (is (= "duntranslated"
             (gt/npgettext "dcontext" "duntranslated" "duntranslated plural" 0 :dynamic)))
      (is (= "duntranslated plural"
             (gt/npgettext "dcontext" "duntranslated" "duntranslated plural" 1 :dynamic)))
      (is (= nil
             (gt/npgettext "context" nil nil 1 nil)))
      (is (= nil
             (gt/npgettext "doesnotexist" "message with plurals"  nil 1 nil)))
      (is (= nil
             (gt/npgettext nil nil nil 1 nil)))
      (is (thrown? ClassCastException
                   (gt/npgettext "context" "throws if n" "is not a number" :one nil))))))
