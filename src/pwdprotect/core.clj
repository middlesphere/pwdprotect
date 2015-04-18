(ns pwdprotect.core
  (:gen-class)
  (:require [pwdprotect.crypto :as c]
            [clojure.string :as s]))

(defn encrypt-password-in-line
  "Encrypts all structures like (ENCRYPT-THIS ...) in String using given password as encryption key.
  Returns a new String with encrypted sensitive fields."
  [^String line
   ^String password
   some-changed?]
  (if-let [pwd-items-seq (re-seq #"\(ENCRYPT-THIS\s+\"?[\w+\s*]+\"?\s*\)" line)]
    (let [next-pwd-item (first pwd-items-seq)
          password-body (s/trim (last (re-find #"(ENCRYPT-THIS\s+)(.+)" next-pwd-item)))
          password-body (s/trim (s/replace password-body #"\)$" ""))
          password-body (s/trim (.replaceAll password-body "^\"|\"$" ""))
          line-repl-pwd (s/replace-first line next-pwd-item (pr-str (c/encrypt-text password-body password)))
          _ (reset! some-changed? true)]
      (recur line-repl-pwd password some-changed?))
    line))

(defn encrypt-passwords-in-file
  "Encrypts all sensitive fields in text file marked as
  (ENCRYPT-THIS \"mystrongpassword\") or (ENCRYPT-THIS mystrongpassword) using given password as encryption key.
  New file will be created with secured sensitive fileds.
  Returns nil."
  [^String file-name
   ^String passwd]
  (let [in-content (slurp file-name)
        some-changed? (atom false)
        out-content (encrypt-password-in-line in-content passwd some-changed?)]
    (if @some-changed?
      (spit file-name out-content))))


(defn decrypt-password
  "Decrypts given encrypted text in base64 form using a String password as decryption key.
 Returns string of a plain text."
  [etext passw]
  (c/decrypt-text etext passw))
