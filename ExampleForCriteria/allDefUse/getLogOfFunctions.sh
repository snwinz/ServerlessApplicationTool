#!/bin/bash
serverless logs -f Caller_ADU --startTime 2h >> logs.txt
serverless logs -f Callee1_ADU --startTime 2h >> logs.txt
serverless logs -f Callee2_ADU --startTime 2h >> logs.txt
serverless logs -f Getter1_ADU --startTime 2h >> logs.txt
serverless logs -f Getter2_ADU --startTime 2h >> logs.txt
serverless logs -f Writer_ADU --startTime 2h >> logs.txt
