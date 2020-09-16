aws s3 ls | cut -d ' ' -f3 | while read x; do echo "deleting s3 bucket $x"; aws s3 rb s3://$x --force; done
