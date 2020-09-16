exports.handler = (event, context, callback) => {
    console.log(event);
    const response = {
                statusCode: '200',
                body: 'Function was called '
            };
    callback(null, response);
};
