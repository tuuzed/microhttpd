@echo off
set path=%path%;..\lib;
java -jar ..\lib\microhttpd.jar %1 %2 %3 %4 %5 %6 %7 %8 %9
@pause