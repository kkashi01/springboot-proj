******** What:
Spring boot project to use MVC for file upload / download.

******** NOTE: I can convert this to RESTful service to remove the view technology (thymeleaf), but just wanted to have a starting point and upload proj into git hub.

******** Description
The file can be selected from local storage and will then be stored into persistence store; file system.
The uploaded file will be stored into /tmp/uploadFolder.

Once uploaded, it will also save some file meta data information. For this, it will create a matching filename, but will use naming convention. The meta data file name will end with _META.txt

******** Running:
mvn spring-boot:run

http://localhost:8080/upload
   using chooser to select a file from local device

http://localhost:8080/download
   If text-entry field is blank, it will list the meta-data files
   If non-existent file is entered, produces error message
   If valid meta data file is requested, it will pull the information and display result




