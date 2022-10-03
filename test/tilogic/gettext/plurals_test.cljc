(ns tilogic.gettext.plurals-test
  ;; #'p/plural1 etc. does not work in ClojureDart    
  {:clj-kondo/config {:linters {:private-call {:level :off}}}}
  (:require
   [tilogic.impl.plurals :as p]
   [clojure.test :refer [deftest is testing]]))

(deftest in-nat-subset?-test
  (testing "valid"
    (is (= true
           (p/in-nat-subset? 0 0 0)))
    (is (= true
           (p/in-nat-subset? 1 0 1)))
    (is (= true
           (p/in-nat-subset? 99 0 100)))
    (is (= false
           (p/in-nat-subset? 0 1 1)))
    (is (= false
           (p/in-nat-subset? 2 0 1)))))

(deftest plural1-test
  (testing "valid"
    (is (= 0
           (p/plural1 0)))
    (is (= 0
           (p/plural1 99)))))

(deftest plural2a-test
  (testing "valid"
    (is (= 0
           (p/plural2a 1)))
    (is (= 1
           (p/plural2a 0)))
    (is (= 1
           (p/plural2a 2)))
    (is (= 1
           (p/plural2a 100)))))

(deftest plural2b-test
  (testing "valid"
    (is (= 0
           (p/plural2b 0)))
    (is (= 0
           (p/plural2b 1)))
    (is (= 1
           (p/plural2b 2)))
    (is (= 1
           (p/plural2b 100)))))

(deftest plural2c-test
  (testing "valid"
    (is (= 0
           (p/plural2c 0)))
    (is (= 0
           (p/plural2c 3)))
    (is (= 0
           (p/plural2c 5)))
    (is (= 0
           (p/plural2c 7)))
    (is (= 0
           (p/plural2c 10)))
    (is (= 0
           (p/plural2c 15)))
    (is (= 0
           (p/plural2c 17)))
    (is (= 0
           (p/plural2c 18)))
    (is (= 0
           (p/plural2c 20)))
    (is (= 0
           (p/plural2c 21)))
    (is (= 0
           (p/plural2c 100)))
    (is (= 1
           (p/plural2c 4)))
    (is (= 1
           (p/plural2c 6)))
    (is (= 1
           (p/plural2c 9)))
    (is (= 1
           (p/plural2c 14)))
    (is (= 1
           (p/plural2c 16)))
    (is (= 1
           (p/plural2c 19)))
    (is (= 1
           (p/plural2c 24)))
    (is (= 1
           (p/plural2c 26)))
    (is (= 1
           (p/plural2c 104)))))

(deftest plural2d-test
  (testing "valid"
    (is (= 0
           (p/plural2d 1)))
    (is (= 0
           (p/plural2d 21)))
    (is (= 0
           (p/plural2d 31)))
    (is (= 0
           (p/plural2d 41)))
    (is (= 0
           (p/plural2d 51)))
    (is (= 0
           (p/plural2d 61)))
    (is (= 0
           (p/plural2d 71)))
    (is (= 0
           (p/plural2d 81)))
    (is (= 0
           (p/plural2d 101)))
    (is (= 1
           (p/plural2d 0)))
    (is (= 1
           (p/plural2d 2)))
    (is (= 1
           (p/plural2d 100)))))


(deftest plural3a-test
  (testing "valid"
    (is (= 0
           (p/plural3a 1)))
    (is (= 0
           (p/plural3a 21)))
    (is (= 0
           (p/plural3a 31)))
    (is (= 0
           (p/plural3a 41)))
    (is (= 0
           (p/plural3a 51)))
    (is (= 0
           (p/plural3a 61)))
    (is (= 0
           (p/plural3a 71)))
    (is (= 0
           (p/plural3a 81)))
    (is (= 0
           (p/plural3a 101)))
    (is (= 1
           (p/plural3a 2)))
    (is (= 1
           (p/plural3a 22)))
    (is (= 1
           (p/plural3a 32)))
    (is (= 1
           (p/plural3a 42)))
    (is (= 1
           (p/plural3a 52)))
    (is (= 1
           (p/plural3a 62)))
    (is (= 1
           (p/plural3a 102)))
    (is (= 2
           (p/plural3a 0)))
    (is (= 2
           (p/plural3a 5)))
    (is (= 2
           (p/plural3a 100)))))

(deftest plural3b-test
  (testing "valid"
    (is (= 0
           (p/plural3b 1)))
    (is (= 1
           (p/plural3b 2)))
    (is (= 2
           (p/plural3b 0)))
    (is (= 2
           (p/plural3b 5)))
    (is (= 2
           (p/plural3b 100)))))

(deftest plural3c-test
  (testing "valid"
    (is (= 0
           (p/plural3c 1)))
    (is (= 0
           (p/plural3c 21)))
    (is (= 0
           (p/plural3c 31)))
    (is (= 0
           (p/plural3c 41)))
    (is (= 0
           (p/plural3c 51)))
    (is (= 0
           (p/plural3c 61)))
    (is (= 0
           (p/plural3c 71)))
    (is (= 0
           (p/plural3c 81)))
    (is (= 0
           (p/plural3c 101)))
    (is (= 1
           (p/plural3c 2)))
    (is (= 1
           (p/plural3c 22)))
    (is (= 1
           (p/plural3c 102)))
    (is (= 2
           (p/plural3c 0)))
    (is (= 2
           (p/plural3c 10)))
    (is (= 2
           (p/plural3c 20)))
    (is (= 2
           (p/plural3c 30)))
    (is (= 2
           (p/plural3c 40)))
    (is (= 2
           (p/plural3c 50)))
    (is (= 2
           (p/plural3c 60)))
    (is (= 2
           (p/plural3c 100)))))

(deftest plural3d-test
  (testing "valid"
    (is (= 0
           (p/plural3d 0)))
    (is (= 0
           (p/plural3d 10)))
    (is (= 0
           (p/plural3d 30)))
    (is (= 0
           (p/plural3d 40)))
    (is (= 0
           (p/plural3d 50)))
    (is (= 0
           (p/plural3d 60)))
    (is (= 0
           (p/plural3d 100)))
    (is (= 1
           (p/plural3d 1)))
    (is (= 1
           (p/plural3d 21)))
    (is (= 1
           (p/plural3d 31)))
    (is (= 1
           (p/plural3d 41)))
    (is (= 1
           (p/plural3d 51)))
    (is (= 1
           (p/plural3d 61)))
    (is (= 1
           (p/plural3d 71)))
    (is (= 1
           (p/plural3d 81)))
    (is (= 1
           (p/plural3d 101)))
    (is (= 2
           (p/plural3d 2)))
    (is (= 2
           (p/plural3d 22)))
    (is (= 2
           (p/plural3d 102)))))

(deftest plural3e-test
  (testing "valid"
    (is (= 0
           (p/plural3e 1)))
    (is (= 1
           (p/plural3e 0)))
    (is (= 1
           (p/plural3e 2)))
    (is (= 1
           (p/plural3e 102)))
    (is (= 2
           (p/plural3e 20)))
    (is (= 2
           (p/plural3e 100)))))


(deftest plural3f-test
  (testing "valid"
    (is (= 0
           (p/plural3f 1)))
    (is (= 0
           (p/plural3f 21)))
    (is (= 0
           (p/plural3f 31)))
    (is (= 0
           (p/plural3f 41)))
    (is (= 0
           (p/plural3f 51)))
    (is (= 0
           (p/plural3f 61)))
    (is (= 0
           (p/plural3f 71)))
    (is (= 0
           (p/plural3f 81)))
    (is (= 0
           (p/plural3f 101)))
    (is (= 1
           (p/plural3f 2)))
    (is (= 1
           (p/plural3f 22)))
    (is (= 1
           (p/plural3f 32)))
    (is (= 1
           (p/plural3f 42)))
    (is (= 1
           (p/plural3f 52)))
    (is (= 1
           (p/plural3f 62)))
    (is (= 1
           (p/plural3f 102)))
    (is (= 2
           (p/plural3f 0)))
    (is (= 2
           (p/plural3f 5)))
    (is (= 2
           (p/plural3f 100)))))

(deftest plural3g-test
  (testing "valid"
    (is (= 0
           (p/plural3g 1)))
    (is (= 1
           (p/plural3g 2)))
    (is (= 1
           (p/plural3g 22)))
    (is (= 1
           (p/plural3g 32)))
    (is (= 1
           (p/plural3g 42)))
    (is (= 1
           (p/plural3g 52)))
    (is (= 1
           (p/plural3g 62)))
    (is (= 1
           (p/plural3g 102)))
    (is (= 2
           (p/plural3g 0)))
    (is (= 2
           (p/plural3g 5)))
    (is (= 2
           (p/plural3g 100)))))

(deftest plural3h-test
  (testing "valid"
    (is (= 0
           (p/plural3h 1)))
    (is (= 0
           (p/plural3h 21)))
    (is (= 0
           (p/plural3h 31)))
    (is (= 0
           (p/plural3h 41)))
    (is (= 0
           (p/plural3h 51)))
    (is (= 0
           (p/plural3h 61)))
    (is (= 0
           (p/plural3h 71)))
    (is (= 0
           (p/plural3h 81)))
    (is (= 0
           (p/plural3h 101)))
    (is (= 1
           (p/plural3h 2)))
    (is (= 1
           (p/plural3h 22)))
    (is (= 1
           (p/plural3h 32)))
    (is (= 1
           (p/plural3h 42)))
    (is (= 1
           (p/plural3h 52)))
    (is (= 1
           (p/plural3h 62)))
    (is (= 1
           (p/plural3h 102)))
    (is (= 2
           (p/plural3h 0)))
    (is (= 2
           (p/plural3h 5)))
    (is (= 2
           (p/plural3h 100)))))

(deftest plural4a-test
  (testing "valid"
    (is (= 0
           (p/plural4a 1)))
    (is (= 0
           (p/plural4a 11)))
    (is (= 1
           (p/plural4a 2)))
    (is (= 1
           (p/plural4a 12)))
    (is (= 2
           (p/plural4a 3)))
    (is (= 2
           (p/plural4a 13)))
    (is (= 3
           (p/plural4a 0)))
    (is (= 3
           (p/plural4a 20)))
    (is (= 3
           (p/plural4a 100)))))

(deftest plural4b-test
  (testing "valid"
    (is (= 0
           (p/plural4b 1)))
    (is (= 1
           (p/plural4b 0)))
    (is (= 1
           (p/plural4b 2)))
    (is (= 1
           (p/plural4b 102)))
    (is (= 1
           (p/plural4b 1002)))
    (is (= 2
           (p/plural4b 11)))
    (is (= 2
           (p/plural4b 111)))
    (is (= 2
           (p/plural4b 1011)))
    (is (= 3
           (p/plural4b 20)))
    (is (= 3
           (p/plural4b 100)))))

(deftest plural4c-test
  (testing "valid"
    (is (= 0
           (p/plural4c 1)))
    (is (= 0
           (p/plural4c 101)))
    (is (= 0
           (p/plural4c 201)))
    (is (= 0
           (p/plural4c 301)))
    (is (= 0
           (p/plural4c 401)))
    (is (= 0
           (p/plural4c 501)))
    (is (= 0
           (p/plural4c 601)))
    (is (= 0
           (p/plural4c 701)))
    (is (= 0
           (p/plural4c 1001)))
    (is (= 1
           (p/plural4c 2)))
    (is (= 1
           (p/plural4c 102)))
    (is (= 1
           (p/plural4c 202)))
    (is (= 1
           (p/plural4c 302)))
    (is (= 1
           (p/plural4c 402)))
    (is (= 1
           (p/plural4c 502)))
    (is (= 1
           (p/plural4c 602)))
    (is (= 1
           (p/plural4c 702)))
    (is (= 1
           (p/plural4c 1002)))
    (is (= 2
           (p/plural4c 3)))
    (is (= 2
           (p/plural4c 4)))
    (is (= 2
           (p/plural4c 103)))
    (is (= 2
           (p/plural4c 104)))
    (is (= 2
           (p/plural4c 203)))
    (is (= 2
           (p/plural4c 204)))
    (is (= 2
           (p/plural4c 303)))
    (is (= 2
           (p/plural4c 304)))
    (is (= 2
           (p/plural4c 403)))
    (is (= 2
           (p/plural4c 404)))
    (is (= 2
           (p/plural4c 503)))
    (is (= 2
           (p/plural4c 504)))
    (is (= 2
           (p/plural4c 603)))
    (is (= 2
           (p/plural4c 604)))
    (is (= 2
           (p/plural4c 703)))
    (is (= 2
           (p/plural4c 704)))
    (is (= 2
           (p/plural4c 1003)))
    (is (= 3
           (p/plural4c 0)))
    (is (= 3
           (p/plural4c 5)))
    (is (= 3
           (p/plural4c 100)))))

(deftest plural4d-test
  (testing "valid"
    (is (= 0
           (p/plural4d 1)))
    (is (= 1
           (p/plural4d 2)))
    (is (= 2
           (p/plural4d 20)))
    (is (= 2
           (p/plural4d 30)))
    (is (= 2
           (p/plural4d 40)))
    (is (= 2
           (p/plural4d 50)))
    (is (= 2
           (p/plural4d 60)))
    (is (= 2
           (p/plural4d 70)))
    (is (= 2
           (p/plural4d 80)))
    (is (= 2
           (p/plural4d 90)))
    (is (= 2
           (p/plural4d 100)))
    (is (= 3
           (p/plural4d 0)))
    (is (= 3
           (p/plural4d 3)))
    (is (= 3
           (p/plural4d 101)))))

(deftest plural5-test
  (testing "valid"
    (is (= 0
           (p/plural5 1)))
    (is (= 1
           (p/plural5 2)))
    (is (= 2
           (p/plural5 3)))
    (is (= 3
           (p/plural5 7)))
    (is (= 4
           (p/plural5 0)))
    (is (= 4
           (p/plural5 11)))
    (is (= 4
           (p/plural5 100)))))

(deftest plural6-test
  (testing "valid"
    (is (= 0
           (p/plural6 0)))
    (is (= 1
           (p/plural6 1)))
    (is (= 2
           (p/plural6 2)))
    (is (= 3
           (p/plural6 3)))
    (is (= 3
           (p/plural6 103)))
    (is (= 3
           (p/plural6 1003)))
    (is (= 4
           (p/plural6 11)))
    (is (= 4
           (p/plural6 111)))
    (is (= 4
           (p/plural6 1011)))
    (is (= 5
           (p/plural6 100)))
    (is (= 5
           (p/plural6 200)))
    (is (= 5
           (p/plural6 300)))
    (is (= 5
           (p/plural6 400)))
    (is (= 5
           (p/plural6 500)))
    (is (= 5
           (p/plural6 600)))
    (is (= 5
           (p/plural6 1000)))))

(deftest plural-fn-test
  (testing "valid"
    (is (fn? (p/plural-fn :uk)))
    (is (fn? (p/plural-fn :not-a-locale)))))
