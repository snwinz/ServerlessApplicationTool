var AWS = require('aws-sdk');
var lambda = new AWS.Lambda();
exports.handler = function(event, context, callback) {
    var params = {
        FunctionName: 'DBWriter_ARS',
        InvocationType: 'RequestResponse',
        LogType: 'Tail',
        Payload: '{ "action" : "nothing" }'
    };
    lambda.invoke(params, function(err, data) {
        if (err) {
            callback(err);
        }
        else {
            callback(null, 'Successfully called');
        }
    });
};
