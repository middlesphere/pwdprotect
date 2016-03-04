(ns pwdprotect.core-test
  (:require [clojure.test :refer :all]
            [pwdprotect.core :refer :all]
            [clojure.edn]))

(def etalon-str1
  ";This is test edn file
{ :param1 (ENCRYPT-THIS \"this is password1\")
:param2 username
:param3 (     ENCRYPT-THIS    \"this is password2 too\"          )
}")

(deftest pwd-encrypt-test
  (testing "Test for password encryption/decryption."
    (let [test-file-name "test$$$.txt"
          _ (spit  test-file-name etalon-str1)
          enc-s (pwdprotect.core/encrypt-passwords-in-file test-file-name "secret12")
          e (clojure.edn/read-string (slurp test-file-name))
          _ (println "encrypted passwords:\npassw1" (:param1 e) "\npassw2" (:param3 e))

          plain-content (pwdprotect.core/decrypt-passwords-in-file test-file-name "secret12")
          _ (println "file content:\n" plain-content )

          edn-content (clojure.edn/read-string plain-content)
          passw1 (edn-content :param1)
          passw2 (edn-content :param3)
          _ (println "decrypted passwords:\npassw1" passw1 "\npassw2" passw2)]
      (clojure.java.io/delete-file test-file-name)
      (is (= passw1 "this is password1"))
      (is (= passw2 "this is password2 too")))))
