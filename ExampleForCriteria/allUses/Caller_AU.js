var AWS = require('aws-sdk');
var lambda = new AWS.Lambda();

exports.handler = function(event, context, callback) {
  var params = {
    FunctionName: 'Callee1_AU', 
    InvocationType: 'RequestResponse',
    LogType: 'Tail',
    Payload: '{ "myKey" : "my" }'
  };
  lambda.invoke(params, function(err, data) {
    if (err) {
      callback(err);
    } else {
      callback(null, 'Successfully called');
    }
  });
    var params2 = {
    FunctionName: 'Callee2_AU', 
    InvocationType: 'RequestResponse',
    LogType: 'Tail',
    Payload: '{ "myKey2" : "my" }'
  };
  lambda.invoke(params2, function(err, data) {
    if (err) {
      callback(err);
    } else {
      callback(null, 'Successfully called');
    }
  });
};
