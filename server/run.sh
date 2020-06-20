#!/bin/bash

print_usage()
{
    echo
    echo "eShoppping Server"
    echo "Correct execution is : $0 [port]"
    echo
    echo "port - port at which to run the server"
}

if [ "$#" -gt 1 ]
then
    echo "At most one argument is needed"
    print_usage
    exit 1
fi

python3 -m server "$@"