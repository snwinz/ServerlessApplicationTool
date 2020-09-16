const AWS = require('aws-sdk');
const dynamodb = new AWS.DynamoDB({ apiVersion: '2012-08-10' });

exports.handler = (event, context, callback) => {
    var params = {
        Key: {
            "myID": {
                N: "1"
            }
        },
        TableName: "Table_ADU"
    };
    if (event.myID != undefined) {
        params.Key.myID.N = event.myID;
    }
    dynamodb.deleteItem(params, function(err, data) {
       console.log(data);
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
                body: 'Data deleted '
            };
            callback(null, response);
        }
    });
};
