#!/bin/bash
serverless logs -f DBGetter_AR --startTime 2h >> logs.txt
serverless logs -f DBWriter_AR --startTime 2h >> logs.txt 
serverless logs -f Invoker_AR --startTime 2h >> logs.txt 
serverless logs -f Processor_AR --startTime 2h >> logs.txt 
