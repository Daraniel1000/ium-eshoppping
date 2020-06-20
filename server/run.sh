#!/bin/bash

print_usage()
{
    echo
    echo "eShoppping Server"
    echo "Correct execution is : $0 [port] [-mode {basic, AB}]"
    echo
    echo "port - port at which to run the server"
    echo "mode - to use basic model or performing AB test"
}

if [ "$#" -gt 3 ]
then
    echo "At most three arguments are needed"
    print_usage
    exit 1
fi

python3 -m server "$@"