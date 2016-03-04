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
      (s/replace line #"\(\s*ENCRYPT-THIS\s+\"(.*)\"\s*\)" (str "(PROTECTED-DATA " encrypted-password ")")))
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

(defn decrypt-password-in-line
  "decrypts structure like (PROTECTED-DATA \"Ua5T4UPIntu9GFvquVduDe4VV/Yg18dOG1n7sp5U5kw=\") using given decryption-password.
  returns a String with plain text."
  [^String line
   ^String decryption-password]
  (if-let [passw-vec (re-find #"\(\s*PROTECTED-DATA\s+\"(.*)\"\s*\)" line)]
    (let [passw (nth passw-vec 1)
          decrypted-password (pr-str (decrypt-password passw decryption-password))]
      (s/replace line #"\(\s*PROTECTED-DATA\s+\"(.*)\"\s*\)" decrypted-password))
    line))

(defn decrypt-passwords-in-file
  "Decrypt all passwords in text file marked as (PROTECTED-DATA \"Ua5T4UPIntu9GFvquVduDe4VV/Yg18dOG1n7sp5U5kw=\")
  using given password.
  Return body of file as string with plain passwords."
  [^String file-name
   ^String passwd]
  (let [in-content (line-seq (clojure.java.io/reader file-name))
        out-content (for [s in-content]
                      (str (decrypt-password-in-line s passwd) \newline))]
    (s/trimr (apply str out-content))))