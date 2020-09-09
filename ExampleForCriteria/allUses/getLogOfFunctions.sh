#!/bin/bash
serverless logs -f Caller_AU --startTime 2h >> logs.txt
serverless logs -f Callee1_AU --startTime 2h >> logs.txt
serverless logs -f Callee2_AU --startTime 2h >> logs.txt
serverless logs -f Getter1_AU --startTime 2h >> logs.txt
serverless logs -f Getter2_AU --startTime 2h >> logs.txt
serverless logs -f Writer_AU --startTime 2h >> logs.txt
