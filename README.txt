*******
******* Spring boot RESTful service
*******
Display list of files to download (search):
	curl.exe "http://localhost:8080/rest/download

Displaying content of a file: (Run above to see list of files. If no files exist, then upload):
    curl.exe "http://localhost:8080/rest/download?fileName=pom.xml_META.txt

Downloading file:
	wget.exe -O foo.txt "http://localhost:8080/rest/download?fileName=pom.xml_META.txt"


*******
******** Spring boot MVC services for file upload / download
*******
The file can be selected from local storage and will then be stored into persistence store; file system.
The uploaded file will be stored into /tmp/uploadFolder.

Once uploaded, it will also save some file meta data information. For this, it will create a matching filename, but will use naming convention. The meta data file name will end with _META.txt

http://localhost:8080/upload
   using chooser to select a file from local device

http://localhost:8080/download
   If text-entry field is blank, it will list the meta-data files
   If non-existent file is entered, produces error message
   If valid meta data file is requested, it will pull the information and display result


