const AWS = require('aws-sdk');
const dynamodb = new AWS.DynamoDB({ apiVersion: '2012-08-10' });

exports.handler = (event, context, callback) => {
    var params = {
        Key: {
            "myID": {
                N: "1"
            }
        },
        TableName: "Table_AD"
    };
    if (event.myID != undefined) {
        params.Key.myID.N = event.myID;
    }
    dynamodb.getItem(params, function(err, data) {
        if (err) {
            console.log(err, err.stack);
            callback(null, {
                statusCode: '500',
                body: err
            });
        }
        else {
            console.log(data.Item);
            const response = {
                statusCode: '200',
                body: 'Data read: ',
                data: JSON.stringify(data.Item)            
            };
            callback(null, response);
        }
    });
};
