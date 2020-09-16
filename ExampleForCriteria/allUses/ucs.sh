#!/bin/bash
sls invoke -f Writer_AU -d '{"name":"Stefan","myID":"11"}' 
sls invoke -f Writer_AU -d 
sls invoke -f Getter2_AU  -d '{"myID":"1"}'
sls invoke -f Getter1_AU  -d '{"myID":"11"}'
sls invoke -f Getter2_AU  -d '{"myID":"11"}'
sls invoke -f Caller_AU
sls invoke -f Deleter_AU  -d '{"myID":"11"}'
sls invoke -f Getter2_AU  -d '{"myID":"11"}'
