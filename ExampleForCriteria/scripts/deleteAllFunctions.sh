aws lambda list-functions | grep FunctionName | cut -d '"' -f4 | while read x; do echo "deleting $x" ; aws lambda delete-function --function-name $x; done
