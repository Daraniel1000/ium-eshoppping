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

python3 -m server