#!/bin/bash
sls invoke -f Invoker_ARS1 -d '{"name":"Stefan","myID":"11"}' 
sls invoke -f Invoker_ARS2 -d '{"name":"Emil","myID":"2"}' 
sls invoke -f Invoker_ARS1  
sls invoke -f DBGetter_ARS  -d '{"myID":"1"}'
sls invoke -f DBGetter_ARS  -d '{"myID":"2"}'
sls invoke -f DBGetter_ARS  -d '{"myID":"11"}'

