(ns tilogic.gettext.lang-test
  (:require
   [tilogic.impl.lang :as l]
   [clojure.test :refer [deftest is testing]]))

(deftest language-test
  (testing "valid"
    (is (= :en
           (l/language :en)))
    (is (= :fr
           (l/language :fr-ca)))
    (is (= :pt
           (l/language :pt-br)))
    (is (= :pt-pt
           (l/language :pt-pt)))
    (is (= :zh-hant
           (l/language :zh)))
    (is (= :zh-hant
           (l/language :zh-hant)))
    (is (= :zh-hant
           (l/language :zh-tw)))
    (is (= :zh-hant
           (l/language :zh-hk)))
    (is (= :zh-hant
           (l/language :zh-mo)))
    (is (= :zh-hant
           (l/language :zh-hant-cn)))
    (is (= :zh-hant
           (l/language :zh-hant-sg)))
    (is (= :zh-hant
           (l/language :zh-hant-es)))
    (is (= :zh-hans
           (l/language :zh-hans)))
    (is (= :zh-hans
           (l/language :zh-cn)))
    (is (= :zh-hans
           (l/language :zh-sg)))
    (is (= :zh-hans
           (l/language :zh-hans-tw)))
    (is (= :zh-hans
           (l/language :zh-hans-hk)))
    (is (= :zh-hans
           (l/language :zh-hans-mo)))
    (is (= :zh-hans
           (l/language :zh-hans-fi))))
  (testing "invalid"
    (is (= nil
           (l/language nil)))
    (is (= nil
           (l/language "not-a-keyword")))
    (is (= nil
           (l/language :-)))
    (is (= nil
           (l/language :-bad-locale)))))
