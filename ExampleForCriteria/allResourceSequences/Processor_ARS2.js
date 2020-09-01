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
            console.log(err, err.stack);
            const response = {
                statusCode: '500',
                body: err
            };
            callback(null, response);
        }
        else {
            const response = {
                statusCode: '200',
                body: 'Successfully called'
            };
            callback(null, response);
        }
    });
};
