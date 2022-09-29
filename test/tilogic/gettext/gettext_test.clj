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
    (with-redefs [i/state (atom {:locale :test})
                  p/plural-fn (fn [_] identity)]
      (is (= identity
             (gt/plural-fn))))))

(deftest remove-locale-test
  (testing "valid"
    (with-redefs [i/state (atom {:translations {:test {"a" "b"}}})]
      (gt/remove-locale :test)
      (is (= false
             (contains? (:translations @i/state) :test))))))

(deftest set-locale-test
  (testing "valid"
    (with-redefs [i/state (atom nil)
                  i/language (fn [_] :lang)
                  gt/supported-languages #{:lang}]
      (gt/set-locale :fr-ca)
      (is (= {:locale :fr-ca
              :language :lang}
             @i/state)))))

(deftest supported-locale?-test
  (testing "valid"
    (is (= true
           (gt/supported-locale? :zh-tw)))
    (is (= false
           (gt/supported-locale? :not-a-locale)))
    (is (= false
           (gt/supported-locale? nil)))))

(deftest translations-test
  (testing "valid"
    (with-redefs [i/state (atom {:translations {:test {}}})]
      (is (= {:test {}}
             (gt/translations))))))

(deftest gettext-test
  (testing "valid"
    (with-redefs [i/state (atom {:locale :test
                                 :translations
                                 {:test {"message" "translation"}
                                  :dynamic {"dmessage" "dtranslation"}}})]
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
                                  :dynamic {"dcontext" {"dmessage" "dtranslation"}}}})]
      (is (= "translation"
             (gt/pgettext "context" "message")))
      (is (= "untranslated"
             (gt/pgettext "context" "untranslated")))
      (is (= "message"
             (gt/pgettext "notacontext" "message")))
      (is (= "dtranslation"
             (gt/pgettext "dcontext" "dmessage" :dynamic)))
      (is (= "duntranslated"
             (gt/pgettext "dcontext" "duntranslated" :dynamic)))
      (is (= "dmessage"
             (gt/pgettext "dnotacontext" "dmessage" :dynamic)))
      (is (= nil
             (gt/pgettext "context" nil nil)))
      (is (= nil
             (gt/pgettext "notacontext" nil nil)))
      (is (= nil
             (gt/pgettext nil nil nil))))))

(deftest ngettext-test
  (testing "valid"
    (with-redefs [i/state (atom {:locale :test
                                 :translations
                                 {:test {"message with plurals" ["index 0" "index 1" "index 2"]}
                                  :dynamic {"dmessage with plurals" ["dindex 0" "dindex 1" "dindex 2"]}}})
                  p/plural-fn (fn [loc]
                                (if (= loc :test)
                                  identity
                                  (fn [n] (if (< n 2) 0 n))))]
      (is (= "index 0"
             (gt/ngettext "message with plurals" "plural message" 0)))
      (is (= "index 1"
             (gt/ngettext "message with plurals" "plural message" 1)))
      (is (= "index 2"
             (gt/ngettext "message with plurals" "plural message" 2)))
      (is (= "plural message"
             (gt/ngettext "message with plurals" "plural message" 99)))
      (is (= "untranslated plural"
             (gt/ngettext "untranslated" "untranslated plural" 0)))
      (is (= "untranslated"
             (gt/ngettext "untranslated" "untranslated plural" 1)))
      (is (= "untranslated plural"
             (gt/ngettext "untranslated" "untranslated plural" 2)))
      (is (= "dindex 0"
             (gt/ngettext "dmessage with plurals" "dplural message" 0 :dynamic)))
      (is (= "dindex 0"
             (gt/ngettext "dmessage with plurals" "dplural message" 1 :dynamic)))
      (is (= "dindex 2"
             (gt/ngettext "dmessage with plurals" "dplural message" 2 :dynamic)))
      (is (= "dplural message"
             (gt/ngettext "dmessage with plurals" "dplural message" 99 :dynamic)))
      (is (= "duntranslated plural"
             (gt/ngettext "duntranslated" "duntranslated plural" 0 :dynamic)))
      (is (= "duntranslated"
             (gt/ngettext "duntranslated" "duntranslated plural" 1 :dynamic)))
      (is (= "duntranslated plural"
             (gt/ngettext "duntranslated" "duntranslated plural" 2 :dynamic)))
      (is (= nil
             (gt/ngettext nil nil 1)))
      (is (= "message with plurals"
             (gt/ngettext "message with plurals" "plural message" :not-number))))))

(deftest npgettext-test
  (testing "valid"
    (with-redefs [i/state (atom {:locale :test
                                 :translations
                                 {:test {"context" {"message with plurals" ["index 0" "index 1" "index 2"]}}
                                  :dynamic {"dcontext" {"dmessage with plurals" ["dindex 0" "dindex 1" "dindex 2"]}}}})
                  p/plural-fn (fn [loc]
                                (if (= loc :test)
                                  identity
                                  (fn [n] (if (< n 2) 0 n))))]
      (is (= "index 0"
             (gt/npgettext "context" "message with plurals" "plural message" 0)))
      (is (= "index 1"
             (gt/npgettext "context" "message with plurals" "plural message" 1)))
      (is (= "index 2"
             (gt/npgettext "context" "message with plurals" "plural message" 2)))
      (is (= "plural message"
             (gt/npgettext "context" "message with plurals" "plural message" 99)))
      (is (= "untranslated plural"
             (gt/npgettext "context" "untranslated" "untranslated plural" 0)))
      (is (= "untranslated"
             (gt/npgettext "context" "untranslated" "untranslated plural" 1)))
      (is (= "untranslated plural"
             (gt/npgettext "context" "untranslated" "untranslated plural" 2)))
      (is (= "dindex 0"
             (gt/npgettext "dcontext" "dmessage with plurals" "dplural message" 0 :dynamic)))
      (is (= "dindex 0"
             (gt/npgettext "dcontext" "dmessage with plurals" "dplural message" 1 :dynamic)))
      (is (= "dindex 2"
             (gt/npgettext "dcontext" "dmessage with plurals" "dplural message" 2 :dynamic)))
      (is (= "dplural message"
             (gt/npgettext "dcontext" "dmessage with plurals" "dplural message" 99 :dynamic)))
      (is (= "duntranslated plural"
             (gt/npgettext "dcontext" "duntranslated" "duntranslated plural" 0 :dynamic)))
      (is (= "duntranslated"
             (gt/npgettext "dcontext" "duntranslated" "duntranslated plural" 1 :dynamic)))
      (is (= "duntranslated plural"
             (gt/npgettext "dcontext" "duntranslated" "duntranslated plural" 2 :dynamic)))
      (is (= nil
             (gt/npgettext "context" nil nil 1 nil)))
      (is (= "message with plurals"
             (gt/npgettext "notacontext" "message" "message with plurals" 0 nil)))
      (is (= "message"
             (gt/npgettext "notacontext" "message" "message with plurals" 1 nil)))
      (is (= "message with plurals"
             (gt/npgettext "notacontext" "message" "message with plurals" 2 nil)))
      (is (= nil
             (gt/npgettext nil nil nil 1 nil)))
      (is (= "message with plurals"
             (gt/npgettext "context" "message with plurals" "plural message" :not-number))))))
