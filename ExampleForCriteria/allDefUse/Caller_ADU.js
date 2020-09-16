var AWS = require('aws-sdk');
var lambda = new AWS.Lambda();

exports.handler = function(event, context, callback) {
  var params = {
    FunctionName: 'Callee1_ADU', 
    InvocationType: 'RequestResponse',
    LogType: 'None',
    Payload: '{ "myKey" : "myValue" }'
  };
  lambda.invoke(params, function(err, replyOfFirstFunction) {
        if (err) {
            console.log(err, err.stack);
            callback(null, {
                statusCode: '500',
                body: err
            });
        }
        else {
            console.log(replyOfFirstFunction);
        }
    });

    var params2 = {
    FunctionName: 'Callee2_ADU', 
    InvocationType: 'RequestResponse',
    LogType: 'None',
    Payload: '{ "myKey2" : "myValue2" }'
  };
  lambda.invoke(params2, function(err, replyOfSecondFunction) {
        if (err) {
            console.log(err, err.stack);
            callback(null, {
                statusCode: '500',
                body: err
            });
        }
        else {
            console.log(replyOfSecondFunction);
            const response = {
                statusCode: '200',
                body: 'Successfully called2',
                data: JSON.stringify(replyOfSecondFunction)            
            };
            callback(null, response);
        }
    });
};
