const AWS = require('aws-sdk');
const lambda = new AWS.Lambda();

exports.handler = function(event, context, callback) {
  let params = {
    FunctionName: 'DBWriter_AR',
    InvocationType: 'Event',
    LogType: 'Tail',
    Payload: '{ "name" : "RandomName", "myID" : "1" } '
  };
  if (event.name != undefined && event.myID != undefined) {
    let payload = JSON.parse(params.Payload);
    payload.name = event.name;
    payload.myID = event.myID;
    params.Payload = JSON.stringify(payload);
  }
 lambda.invoke(params, function(err, data) {
    if (err) {
      callback(err);
    }
    else {
      callback(null, 'Successfully called');
    }
  });
};

