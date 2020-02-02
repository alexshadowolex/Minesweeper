::Simple batch file to compile all Minesweeper-files and move the class-files to the bin folder
@echo off
javac src\*.java

IF NOT %ERRORLEVEL% == 0 goto fail

::delete all class-files in bin
del bin\*.class /q 
move src\*.class bin
echo Compiled Files
pause
exit

:fail
echo Error, check javac-output
pause
exit