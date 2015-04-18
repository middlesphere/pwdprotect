(ns pwdprotect.crypto
  (:gen-class)
  (:import [org.jasypt.encryption.pbe StandardPBEStringEncryptor]
           [org.bouncycastle.jce.provider BouncyCastleProvider]))

(defn break-jce-policy-limit
  "Break JCE crypto limit. Should be run once, primarily at the begining of the program
  to avoid JCE policy limit if JDK/JRE runtime has no installed files for break crypto limit. Returns nil."
  []
  (let [field (-> (Class/forName "javax.crypto.JceSecurity")
                  (.getDeclaredField "isRestricted"))]
    (.setAccessible field true)
    (.set field nil Boolean/FALSE)))


(defn- init-engine
  "Init password encryption/decryption engine.
  Encryption algorithm is PBEWITHSHA256AND128BITAES-CBC-BC.
  Returns initilized org.jasypt.encryption.pbe.StandardPBEStringEncryptor object."
  []
  (break-jce-policy-limit)
  (let [e (StandardPBEStringEncryptor.)
        algo "PBEWITHSHA256AND128BITAES-CBC-BC"]
    (.setProvider e (BouncyCastleProvider.))
    (.setAlgorithm e algo)
    e))

(defn encrypt-text
  "Encrypts given text using a String password.
  Returns base64 string of encrypted text."
  [^String text
   ^String password]
  (let [^StandardPBEStringEncryptor e (init-engine)]
    (.setPassword e password)
    (.encrypt e text)))


(defn decrypt-text
  "Decrypts given encrypted text in base64 form using a String password.
  Returns string of a plain text."
  [^String encrypted-text
   ^String password]
  (let [^StandardPBEStringEncryptor e (init-engine)]
    (.setPassword e password)
    (.decrypt e encrypted-text)))