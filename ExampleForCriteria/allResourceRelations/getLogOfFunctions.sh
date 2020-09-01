#!/bin/bash
serverless logs -f DBGetter_ARR --startTime 2h >> logs.txt
serverless logs -f DBWriter_ARR --startTime 2h >> logs.txt 
serverless logs -f Invoker_ARR1 --startTime 2h >> logs.txt
serverless logs -f Invoker_ARR2 --startTime 2h >> logs.txt 
serverless logs -f Processor_ARR1 --startTime 2h >> logs.txt 
serverless logs -f Processor_ARR2 --startTime 2h >> logs.txt 
