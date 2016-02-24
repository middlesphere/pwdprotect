(defproject com.middlesphere/pwdprotect "1.0.1"
            :description "Password protection library. Hide sensitive information in your configs."
            :url "https://github.com/middlesphere/pwdprotect.git"
            :vendor "Middlesphere"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.8.0"]
                           [org.jasypt/jasypt "1.9.2"]
                           ]
            :target-path "target/%s/"
            :omit-source false
            :global-vars {*warn-on-reflection* false}
            :profiles {
                       :dev      {:dependencies [[org.bouncycastle/bcprov-jdk15on "1.54"]]}
                       :provided {:dependencies [[org.bouncycastle/bcprov-jdk15on "1.54"]]}}
            :scm {:name "git"
                  :url "https://github.com/middlesphere/pwdprotect.git"})
