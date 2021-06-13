@echo off

rem Removing compiled directory
echo Removing compiled directory
rmdir /s compiled\


rem Recreating compiled directory
echo Recreating compiled directory
mkdir compiled\
cd compiled\
mkdir classes\
mkdir Safekeeper\


rem Java compilation
echo Java compilation
cd ..\src\
javac -d ..\compiled\classes safekeeper\Entry.java
cd ..\compiled\


rem Creating jar
echo Creating jar
cd classes\
jar cfe Safekeeper.jar safekeeper.Entry safekeeper\
move Safekeeper.jar ..\Safekeeper\Safekeeper.jar
cd ..\


rem Copying resources
echo Copying resources
xcopy /E ..\src\resources\ Safekeeper\resources\


pause