#!/bin/bash

print_usage()
{
    echo
    echo "eShoppping Server"
    echo "Correct execution is : $0"
    echo
}

if ! [ "$#" -eq 0 ]
then
    echo "Exactly zero arguments are needed"
    print_usage
    exit 1
fi

if command -v 'gradle'
then
    echo "Using local Gradle installation."
    gradle run
    exit "$?"
fi

echo "No Gradle installation detected."

if [ ! -f 'gradlew' ]
then 
    echo "No gradlew file. Can't use wrapper."
    print_usage
    exit 2
fi

if [ ! -x 'gradlew' ]
then 
    echo "You don't have permission to use gradlew. Can't use wrapper."
    print_usage
    exit 3
fi

if [ ! -f 'gradle/wrapper/gradle-wrapper.jar' ]
then 
    echo "No wrapper detected."
    print_usage
    exit 4
fi

echo "Using Gradle wrapper."
./gradlew run
exit "$?"
