const AWS = require('aws-sdk');
const dynamodb = new AWS.DynamoDB({ apiVersion: '2012-08-10' });

exports.handler = (event, context, callback) => {
    console.log(event);
    var params = {
        Item: {
            "myID": {
                N: "1"
            },
            "name": {
                S: "JustAName"
            }
        },
        TableName: "Table_AR"
    };
    if (event.name != undefined && event.myID != undefined) {
        params.Item.name.S = event.name;
        params.Item.myID.N = event.myID;
    }
    dynamodb.putItem(params, function(err, data) {
        if (err) {
            console.log(err, err.stack);
            callback(null, {
                statusCode: '500',
                body: err
            });
        }
        else {
            console.log("Data written\n");
            callback(null, {
                statusCode: '200',
                body: 'Data written '
            });
        }
    });
};

