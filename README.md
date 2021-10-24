# ![](./src/main/resources/icon/favicon.ico) Assignment: RocketML Threat Detection Project

This project loads csv file, analyzes it and creates a threat report.

## How to Start Project
```bash
install maven from <link> : https://maven.apache.org/install.html
open threatdetection.zip file
cd threatdetection
mvn spring-boot:run

open the link below in Chrome or Microsoft Edge
<link> : http://localhost:8081

OR

open threatdetection.zip file
in Eclipse IDE, use menu item "File->Open Projects from File System", and open project
wait Maven dependencies being downloaded 
run StartWebApplication.java

open the link below in Chrome or Microsoft Edge
<link> : http://localhost:8081

```


`<link>` : <http://localhost:8081>

## Problem Description

Security Threat System detect threats on various nodes and log data to the csv file named
"threat_generator.csv". An admin wants to prepare a report from the generated file - "threat_generator.csv"
where they can see the number of threats per IP address, last creation time, the status of the IP 
address, and the time difference between first event and last event of the IP.

## Solution Description

Project is developed in Java Spring Boot, and for front-end used Thymeleaf.

After uploading the csv file to the /uploads folder, **createAnalyzeReport** method is called that is implemented in ThreatService class.
This method returns a list of **AnalyzeReport** object that holds all the data created after all process. This list is used for reporting.

In this method, **AnalyzeReport** method is called for the CSV file. This method takes the file as a parameter, 
reads it line by line and forms a list of **FileLineInfo** that holds the fields of the line, 
for all the **FileLineInfo** object in the list creates a **Threat** object by calling the **createThreat** method.
This method calls **validateFields** method to validate the fields of the line. If the data is valid,
stores the **Threat** object in a HashMap that the key is the **Ip Address** field
and the value is the **Threat** Object. If the line has a field that is invalid, logs the line.

After forming the HashMap for the CSV file, by using the list analysis report  is form as another list. 
In the process, the Ip Address is checked in the map. If it stored before, the latest data is kept and the fields 
of the object is updated so that the last creation time, status and the difference between first and the last data 
for the IP Address are kept.

An this list used for the reporting.



## Usage

1- Click on the **Browse** button and Select the csv file from the file system.

2- Click on the **Analyze** button

3- The system will upload the file, read the contents, do the required validations, creates the data structure for analyze report.

4- Click on the **Detail Report** button

5- The analyze report will be listed. 


