#!/bin/bash
#Simple shell script to compile all Minesweeper-files and move the class-files to the bin folder
javac src/*.java

if [ $? -eq 0 ]
then
    #delete all class-files in bin
    rm bin/*.class
    mv src/*.class bin
    echo "Compiled Files"
else
    echo "Error, check javac-output"
fi

read -p "Press enter to continue"