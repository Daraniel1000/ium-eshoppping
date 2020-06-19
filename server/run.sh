#!/bin/bash

print_usage()
{
    echo
    echo "eShoppping Server"
    echo "Correct execution is : $0 model [port]"
    echo
    echo "model - model to choose (A or B)"
}

if [ "$#" -lt 1 ]
then
    echo "At least one argument is needed"
    print_usage
    exit 1
fi

if [ "$#" -gt 2 ]
then
    echo "At most two arguments are needed"
    print_usage
    exit 1
fi

model="models/$1.pkl"
shift

python3 -m server "$model" "$@"