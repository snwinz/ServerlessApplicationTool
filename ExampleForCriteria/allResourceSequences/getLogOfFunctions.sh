#!/bin/bash
serverless logs -f DBGetter_ARS --startTime 2h >> logs.txt
serverless logs -f DBWriter_ARS --startTime 2h >> logs.txt 
serverless logs -f Invoker_ARS1 --startTime 2h >> logs.txt 
serverless logs -f Invoker_ARS2 --startTime 2h >> logs.txt 
serverless logs -f Processor_ARS1 --startTime 2h >> logs.txt 
serverless logs -f Processor_ARS2 --startTime 2h >> logs.txt 
