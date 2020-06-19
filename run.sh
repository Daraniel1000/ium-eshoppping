#!/bin/bash

possible_targets='client server'

print_usage()
{
    echo
    echo "eShoppping"
    echo "Correct execution is : $0 {`echo "$possible_targets" | sed 's/ /, /'`} ..."
    echo
}

if [ "$#" -lt 1 ]
then
    echo "At least one argument is needed"
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

echo "Changing directory to $target"
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

shift
echo "Running $target/run.sh from $target"
./run.sh "$@"

exitval="$?"

if ! [ "$exitval" -eq 0 ]
then
    echo "Running $target/run.sh from $target failed"
    print_usage
fi
exit "$exitval"