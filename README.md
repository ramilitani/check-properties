# check-properties

Utility tool to verify properties from a template file missing in other properties file.

This is useful when you need to add or update properties and checking whether the key value from template exist in other propertie files making sure in all files the keys are the same. 

PS: checking existing keys of several extensive language properties files.

**When you run this utility, it create a log file witch shows properties contained in the template file that are not in other properties file**

### Prerequisites
```
Java 8
Maven
```
### Running the application
 - Go to the resources folder **src/main/resources** 
   
   - include the contents of your template properties file in the application's **template.properties** file.
   
   - include the new properties file content witch you would like to add/update in the apllication's **tobechecked.properties** file.
 
 - Go to the root path of the application and type the below maven command to build the project
    ```
    mvn clean install
    ```
 
 - Go to the target folder and type the following command to run the application.
    ```
    java -jar checkProperties.jar
    ```
   
### Application execution warnings

  After you ran the application go to the target folder **check-properties/target** to verify the **app.log** file that show you the missing properties.
