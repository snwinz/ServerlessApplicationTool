#!/bin/bash
sls invoke -f Writer_AD -d '{"name":"Stefan","myID":"11"}' 
sls invoke -f Writer_AD -d 
sls invoke -f Getter2_AD  -d '{"myID":"1"}'
sls invoke -f Getter1_AD  -d '{"myID":"11"}'
sls invoke -f Getter2_AD  -d '{"myID":"11"}'
sls invoke -f Caller_AD
sls invoke -f Deleter_AD  -d '{"myID":"11"}'
sls invoke -f Getter2_AD  -d '{"myID":"11"}'
