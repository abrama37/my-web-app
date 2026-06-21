
compilation: mvn clean compile

run server : mvn exec:java -Dexec.mainClass="com.myapp.App"
or : mvn clean exec:java -Dexec.mainClass="com.myapp.App"
