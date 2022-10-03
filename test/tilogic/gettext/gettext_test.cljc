(ns  tilogic.gettext.gettext-test
  (:require
   [tilogic.gettext :as gt]
   [tilogic.impl.plurals :as p]
   [clojure.test :refer [deftest is testing]]))

(deftest add-locale-test
  (let [data (atom {:translations {:test {"1" "2"}}})]
    (gt/add-locale data :zz {"z" "z"})
    (is (= {:translations {:test {"1" "2"}
                           :zz {"z" "z"}}}
           (gt/add-locale data :zz {"z" "z"})))))

(deftest available-locales-test
  (let [data (atom {:translations {:test {"1" "2"}
                                   :zz {"z" "z"}}})]
    (is (= #{:test :zz}
           (set (gt/available-locales data))))))

(deftest get-locale-test
  (let [data (atom {:locale :test})]
    (is (= :test
           (gt/get-locale data)))))

(deftest language-test
  (let [data (atom {:language :test})]
    (is (= :test
           (gt/language data)))))

(deftest plural-fn-test
  (let [data (atom {:locale :fr})]
    (is (= (:fr p/plurals)
           (gt/plural-fn data)))))

(deftest remove-locale-test
  (let [data (atom {:translations {:test {"a" "b"}}})]
    (gt/remove-locale data :test)
    (is (= false
           (contains? (:translations @data) :test)))))

(deftest set-locale-test
  (let [data (atom {})]
    (gt/set-locale data :fr-ca)
    (is (= {:locale :fr-ca
            :language :fr}
           @data))))

(deftest supported-locale?-test
  (is (= true
         (gt/supported-locale? :zh-tw)))
  (is (= false
         (gt/supported-locale? :not-a-locale)))
  (is (= false
         (gt/supported-locale? nil))))

(deftest translations-test
  (let [data (atom {:translations {:hr {}}})]
    (is (= {:hr {}}
           (gt/translations data)))))

(deftest gettext-test
  (let [data (atom {:locale :hr
                    :translations
                    {:hr {"message" "translation"}
                     :fr {"dynamic message" "dynamic translation"}}})
        gettext (gt/gettext-fn data)]
    (testing "valid"
      (is (= "translation"
             (gettext "message")))
      (is (= "untranslated"
             (gettext "untranslated")))
      (is (= "dynamic translation"
             (gettext "dynamic message" :fr)))
      (is (= "dynamic untranslated"
             (gettext "dynamic untranslated" :fr)))
      (testing "invalid"
        (is (= nil
               (gettext nil nil)))))))

(deftest pgettext-test
  (let [data (atom {:locale :hr
                    :translations
                    {:hr {"context" {"message" "translation"}}
                     :fr {"dynamic context " {"dynamic message" "dynamic translation"}}}})
        pgettext (gt/pgettext-fn data)]
    (testing "valid"
      (is (= "translation"
             (pgettext "context" "message")))
      (is (= "untranslated"
             (pgettext "context" "untranslated")))
      (is (= "message"
             (pgettext "notacontext" "message")))
      (is (= "dynamic translation"
             (pgettext "dynamic context " "dynamic message" :fr)))
      (is (= "dynamic untranslated"
             (pgettext "dynamic context " "dynamic untranslated" :fr)))
      (is (= "dynamic message"
             (pgettext "dnotacontext" "dynamic message" :fr))))
    (testing "invalid"
      (is (= nil
             (pgettext "context" nil nil)))
      (is (= nil
             (pgettext "notacontext" nil nil)))
      (is (= nil
             (pgettext nil nil nil))))))

(deftest ngettext-test
  (let [data (atom {:locale :hr
                    :translations
                    {:hr {"message with plurals" ["index 0" "index 1" "index 2"]}
                     :fr {"dynamic message with plurals" ["dynamic index 0" "dynamic index 1" "dynamic index 2"]}}})
        ngettext (gt/ngettext-fn data)]
    (testing "valid"
      (is (= "index 2"
             (ngettext "message with plurals" "plural message" 0)))
      (is (= "index 0"
             (ngettext "message with plurals" "plural message" 1)))
      (is (= "index 1"
             (ngettext "message with plurals" "plural message" 2)))
      (is (= "index 2"
             (ngettext "message with plurals" "plural message" 99)))
      (is (= "message with plurals"
             (ngettext "message with plurals" "plural message" -1)))
      (is (= "untranslated plural"
             (ngettext "untranslated" "untranslated plural" 0)))
      (is (= "untranslated"
             (ngettext "untranslated" "untranslated plural" 1)))
      (is (= "untranslated plural"
             (ngettext "untranslated" "untranslated plural" 2)))
      (is (= "untranslated"
             (ngettext "untranslated" "untranslated plural" -1)))
      (is (= "dynamic index 0"
             (ngettext "dynamic message with plurals" "dynamic plural message" 0 :fr)))
      (is (= "dynamic index 0"
             (ngettext "dynamic message with plurals" "dynamic plural message" 1 :fr)))
      (is (= "dynamic index 1"
             (ngettext "dynamic message with plurals" "dynamic plural message" 2 :fr)))
      (is (= "dynamic index 1"
             (ngettext "dynamic message with plurals" "dynamic plural message" 99 :fr)))
      (is (= "dynamic message with plurals"
             (ngettext "dynamic message with plurals" "dynamic plural message" -1 :fr)))
      (is (= "dynamic untranslated plural"
             (ngettext "dynamic untranslated" "dynamic untranslated plural" 0 :fr)))
      (is (= "dynamic untranslated"
             (ngettext "dynamic untranslated" "dynamic untranslated plural" 1 :fr)))
      (is (= "dynamic untranslated plural"
             (ngettext "dynamic untranslated" "dynamic untranslated plural" 2 :fr)))
      (is (= "dynamic untranslated"
             (ngettext "dynamic untranslated" "dynamic untranslated plural" -1 :fr)))
      (is (= "message with plurals"
             (ngettext "message with plurals" "plural message" :not-number)))
      (testing "invalid"
        (is (= nil
               (ngettext nil nil 1)))))))

(deftest npgettext-test
  (let [data (atom {:locale :hr
                    :translations
                    {:hr {"context" {"message with plurals" ["index 0" "index 1" "index 2"]}}
                     :fr {"dynamic context " {"dynamic message with plurals" ["dynamic index 0" "dynamic index 1" "dynamic index 2"]}}}})
        npgettext (gt/npgettext-fn data)]
    (testing "valid"
      (is (= "index 2"
             (npgettext "context" "message with plurals" "plural message" 0)))
      (is (= "index 0"
             (npgettext "context" "message with plurals" "plural message" 1)))
      (is (= "index 1"
             (npgettext "context" "message with plurals" "plural message" 2)))
      (is (= "index 2"
             (npgettext "context" "message with plurals" "plural message" 99)))
      (is (= "message with plurals"
             (npgettext "context" "message with plurals" "plural message" -1)))
      (is (= "untranslated plural"
             (npgettext "context" "untranslated" "untranslated plural" 0)))
      (is (= "untranslated"
             (npgettext "context" "untranslated" "untranslated plural" 1)))
      (is (= "untranslated plural"
             (npgettext "context" "untranslated" "untranslated plural" 2)))
      (is (= "untranslated plural"
             (npgettext "context" "untranslated" "untranslated plural" 99)))
      (is (= "dynamic index 0"
             (npgettext "dynamic context " "dynamic message with plurals" "dynamic plural message" 0 :fr)))
      (is (= "dynamic index 0"
             (npgettext "dynamic context " "dynamic message with plurals" "dynamic plural message" 1 :fr)))
      (is (= "dynamic index 1"
             (npgettext "dynamic context " "dynamic message with plurals" "dynamic plural message" 2 :fr)))
      (is (= "dynamic message with plurals"
             (npgettext "dynamic context " "dynamic message with plurals" "dynamic plural message" -1 :fr)))
      (is (= "dynamic index 1"
             (npgettext "dynamic context " "dynamic message with plurals" "dynamic plural message" 99 :fr)))
      (is (= "dynamic untranslated plural"
             (npgettext "dynamic context " "dynamic untranslated" "dynamic untranslated plural" 0 :fr)))
      (is (= "dynamic untranslated"
             (npgettext "dynamic context " "dynamic untranslated" "dynamic untranslated plural" 1 :fr)))
      (is (= "dynamic untranslated plural"
             (npgettext "dynamic context " "dynamic untranslated" "dynamic untranslated plural" 2 :fr)))
      (is (= "dynamic untranslated plural"
             (npgettext "dynamic context " "dynamic untranslated" "dynamic untranslated plural" 99 :fr)))
      (is (= "message with plurals"
             (npgettext "notacontext" "message" "message with plurals" 0 nil)))
      (is (= "message"
             (npgettext "notacontext" "message" "message with plurals" 1 nil)))
      (is (= "message with plurals"
             (npgettext "notacontext" "message" "message with plurals" 2 nil)))
      (is (= "message with plurals"
             (npgettext "notacontext" "message" "message with plurals" 99 nil)))
      (is (= "message"
             (npgettext "notacontext" "message" "message with plurals" 01 nil)))
      (is (= "message with plurals"
             (npgettext "context" "message with plurals" "plural message" :not-number)))
      (testing "invalid"
        (is (= nil
               (npgettext "context" nil nil 1 nil)))
        (is (= nil
               (npgettext nil nil nil 1 nil)))))))
