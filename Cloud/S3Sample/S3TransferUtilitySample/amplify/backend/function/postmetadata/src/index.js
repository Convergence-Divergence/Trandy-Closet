const AWS = require('aws-sdk');
var mysql = require('mysql');

const connection = mysql.createConnection({
            host        : 'database-closet-instance-1.ckoes5kxoo1c.us-east-1.rds.amazonaws.com',
            user        : 'admin',
            password    : 'campus07@multi',
            database    : 'Trandy-Closet',
            port        : 3306
            })

//exports.handler = async (event) => {
//    try {
//     const data = await new Promise((resolve, reject) => {
//       connection.connect(function (err) {
//         if (err) {
//           reject(err);
//         }
//         connection.query('insert into Users (name, sex) VALUES ('me', 'M');', function (err, result) {
//           if (err) {
//             console.log("Error->" + err);
//             reject(err);
//           }
//           resolve(result);
//         });
//       })
//     });
//
//    return {
//      statusCode: 200,
//      body: JSON.stringify(data)
//    }


//  } catch (err) {
//    return {
//      statusCode: 400,
//      body: err.message
//    }
//  }
//};

exports.handler = (event, context, callback) => {
  // allows for using callbacks as finish/error-handlers
  context.callbackWaitsForEmptyEventLoop = false;
  const sql = "insert into Trandy-Closet.Users values('babo','F');";
  con.query(sql, (err, res) => {
    if (err) {
      throw err
    }
    callback(null, '1 records inserted.');
  })
};
