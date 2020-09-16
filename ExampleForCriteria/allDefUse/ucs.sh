#!/bin/bash
sls invoke -f Writer_ADU -d '{"name":"Stefan","myID":"11"}' 
sls invoke -f Writer_ADU -d 
sls invoke -f Getter2_ADU  -d '{"myID":"1"}'
sls invoke -f Getter1_ADU  -d '{"myID":"11"}'
sls invoke -f Getter2_ADU  -d '{"myID":"11"}'
sls invoke -f Caller_ADU
sls invoke -f Deleter_ADU  -d '{"myID":"11"}'
sls invoke -f Getter2_ADU  -d '{"myID":"11"}'
