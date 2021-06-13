@echo off

rem Removing compiled directory
echo Removing compiled directory
rmdir /s compiled\


rem Recreating compiled directory
echo Recreating compiled directory
mkdir compiled\
cd compiled\
mkdir classes\
mkdir Safekeeper_Java\
mkdir Safekeeper_Windows\


rem Java compilation
echo Java compilation
cd ..\src\
"C:\Program Files\Java\jdk-11.0.11\bin\javac.exe" -d ..\compiled\classes safekeeper\Entry.java
cd ..\compiled\


rem Creating jar
echo Creating jar
cd classes\
"C:\Program Files\Java\jdk-11.0.11\bin\jar.exe" cfe Safekeeper.jar safekeeper.Entry safekeeper\
move Safekeeper.jar ..\Safekeeper_Java\Safekeeper.jar
cd ..\


rem Copying resources
echo Copying resources
xcopy /E ..\src\resources\ Safekeeper_Java\resources\
xcopy /E ..\src\resources\ Safekeeper_Windows\resources\


rem Creating windows executable
echo Creating windows executable
cd ..\
launch4jc launch4j_config.xml
cd compiled\


pause