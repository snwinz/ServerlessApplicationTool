#!/bin/bash
serverless logs -f Caller_AD --startTime 2h >> logs.txt
serverless logs -f Callee1_AD --startTime 2h >> logs.txt
serverless logs -f Callee2_AD --startTime 2h >> logs.txt
serverless logs -f Getter1_AD --startTime 2h >> logs.txt
serverless logs -f Getter2_AD --startTime 2h >> logs.txt
serverless logs -f Writer_AD --startTime 2h >> logs.txt
serverless logs -f Deleter_AD --startTime 2h >> logs.txt
