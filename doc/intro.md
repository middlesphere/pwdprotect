# pwdprotect

A Clojure library designed to protect passwords in config files.

## Usage

Add dependency to your project.clj file.

```
[com.middlesphere/pwdprotect "0.1"]
```

Also, BouncySastle crypto library should be in classpath.
You can add it as development dependency in your project.clj:
```
            :profiles {
                       :dev      {:dependencies [[org.bouncycastle/bcprov-jdk15on "1.52"]]}
                       :provided {:dependencies [[org.bouncycastle/bcprov-jdk15on "1.52"]]}}
```

First, edit your config file and mark any sensitive fields or passwords to be encrypted like (ENCRYPT-THIS ...)
e.g. (ENCRYPT-THIS password1) or (ENCRYPT-THIS "password 2"). Quotes around this is unnecessary.

Second, call encrypt-passwords-in-file function every time you start program before you read config file.
Example:
```
(pwdprotect.core/encrypt-passwords-in-file "myconfig.txt" "test")
```
This function will encrypt all passwords or any other sensitive fields in config file marked as (ENCRYPT-THIS ...).
e.g. "Ua5T4UPIntu9GFvquVduDe4VV/Yg18dOG1n7sp5U5kw="

Third, when you parse a config file just decrypt passwords:
```
(pwdprotect.core/decrypt-password "Ua5T4UPIntu9GFvquVduDe4VV/Yg18dOG1n7sp5U5kw="  "test")
```

## License

Copyright © 2015 by Middlepshere.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
