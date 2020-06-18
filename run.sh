#!/bin/bash

possible_targets='client server'

print_usage()
{
    echo
    echo "eShoppping"
    echo "Correct execution is : $0 {`echo "$possible_targets" | sed 's/ /, /'`}"
    echo
}

if ! [ "$#" -eq 1 ]
then
    echo "Exactly one argument in needed"
    print_usage
    exit 1
fi

target="$1"

if ! echo "$possible_targets" | grep -wq "$target"
then
    echo "Invalid target: $target"
    print_usage
    exit 2
fi

if [ ! -d "$target" ]
then 
    echo "Directory $target does not exist"
    print_usage
    exit 3
fi

if [ ! -r "$target" ]
then 
    echo "You don't have rights to read $target"
    print_usage
    exit 4
fi

cd "$target"


if [ ! -f 'run.sh' ]
then 
    echo "There is no $target/run.sh file"
    print_usage
    exit 5
fi

if [ ! -x 'run.sh' ]
then 
    echo "You don't have rights to execute $target/run.sh"
    print_usage
    exit 4
fi

./run.sh