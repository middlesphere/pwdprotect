(ns pwdprotect.core-test
  (:require [clojure.test :refer :all]
            [pwdprotect.core :refer :all]
            [clojure.edn]))

(def etalon-str1
  ";This is test edn file
{ :param1 (ENCRYPT-THIS this is password1)
:param2 username
:param3 (ENCRYPT-THIS    \"this is password2 too\")
}")

(deftest pwd-encrypt-test
  (testing "Test for password encryption/decryption."
    (let [changed? (atom false)
          enc-s (pwdprotect.core/encrypt-password-in-line etalon-str1 "secret12" changed?)
          e (clojure.edn/read-string enc-s)
          _ (println "encrypted passwords:\npassw1" (e :param1) "\npassw2" (e :param3))
          passw1 (pwdprotect.core/decrypt-password (e :param1) "secret12")
          passw2 (pwdprotect.core/decrypt-password (e :param3) "secret12")
          _ (println "decrypted passwords:\npassw1" passw1 "\npassw2" passw2)]
      (is (= passw1 "this is password1"))
      (is (= passw2 "this is password2 too")))))
