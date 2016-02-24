(ns pwdprotect.core
  (:gen-class)
  (:require [pwdprotect.crypto :as c]
            [clojure.string :as s]))

(defn encrypt-password-in-line
  "encrypts structure like (ENCRYPT-THIS \"some passw\") using given encryption-password.
  returns a new String with encrypted sensitive field."
  [^String line
   ^String encryption-password]
  (if-let [passw-vec (re-find #"\(\s*ENCRYPT-THIS\s+\"(.*)\"\s*\)" line)]
    (let [passw (nth passw-vec 1)
          encrypted-password (pr-str (c/encrypt-text passw encryption-password))]
      (s/replace-first line #"\(\s*ENCRYPT-THIS\s+\"(.*)\"\s*\)" encrypted-password))
    line))

(defn encrypt-passwords-in-file
  "Encrypts all passwords in text file marked as (ENCRYPT-THIS \"mystrongpassword\") using given password.
  Old file will be overwritten with secured passwords.
  returns nil."
  [^String file-name
   ^String passwd]
  (let [in-content (line-seq (clojure.java.io/reader file-name))
        out-content (for [s in-content]
                      (str (encrypt-password-in-line s passwd) \newline))]
    (spit file-name (s/trimr (apply str out-content)))))


(defn decrypt-password
  "Decrypts given encrypted text in base64 form using a String password as decryption key.
  Returns string of a plain text."
  [etext passw]
  (c/decrypt-text etext passw))
