A simple application to show the evolution of employment per council based on data from the Spanish Social Security.

This is the Java parser for the published files. The Javascript client is available at https://github.com/gorbeia/EnpleguDatuakJS.

It reads data from the files and parses it to output data in JSON format. It can optionally read them from files on the filesystem or directly from the Social Security page. It can also output it in JSON format or send it directly to a MongoDB database.
When reading the data from the page it saves a file with the read links and on next executions it does not attempt to parse already parsed files.

## Usage
Build it:
``` shell
mvn assembly:assembly
```
Load data from the Social Security page and output to the console:
``` shell
java -jar target/EnpleguDatuakJava-1.0-SNAPSHOT-jar-with-dependencies.jar -t ss -c myconfiguration.properties
```

Demo: http://gorbeia.github.io/EnpleguDatuakJS/