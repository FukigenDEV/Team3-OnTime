const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

//START OF EXAMPLE FUNCTIONS, COPIED FROM FIREBASE DOCUMENTATION

// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
exports.addMessage = functions.https.onRequest(async (req, res) => {
  // Grab the text parameter.
  const original = req.query.text;
  // Push the new message into the Realtime Database using the Firebase Admin SDK.
  const snapshot = await admin.database().ref('/messages').push({original: original});
  // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
  res.redirect(303, snapshot.ref.toString());
});

// Listens for new messages added to /messages/:pushId/original and creates an
// uppercase version of the message to /messages/:pushId/uppercase
exports.makeUppercase = functions.database.ref('/messages/{pushId}/original')
    .onCreate((snapshot, context) => {
      // Grab the current value of what was written to the Realtime Database.
      const original = snapshot.val();
      console.log('Uppercasing', context.params.pushId, original);
      const uppercase = original.toUpperCase();
      // You must return a Promise when performing asynchronous tasks inside a Functions such as
      // writing to the Firebase Realtime Database.
      // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
      return snapshot.ref.parent.child('uppercase').set(uppercase);
    });

//exports.scheduledFunction = functions.pubsub.schedule('every 5 minutes').onRun((context) => {
//  console.log('This will be run every 5 minutes!');
//  return null;
//});


//END OF EXAMPLE FUNCTIONS, COPIED FROM FIREBASE DOCUMENTATION


// SEND NOTIFICATION WHEN DEVICE TOKEN ADDED TO DB
// Listens for new tokens added to /Users/{AndroidID}/deviceToken and sends
// notification the device of the new token
exports.newUserNotification = functions.database.ref('/Users/{AndroidID}/deviceToken')
    .onWrite((change, context) => {
        const AndroidID = context.params.AndroidID;

        var ref = admin.database().ref(`/Users/${AndroidID}/deviceToken`);
        return ref.once("value", function(snapshot){
        const deviceToken = snapshot.val();
            const payload = {
                data: {
                    title: 'Hello from Firebase',
                    body: 'New user has been added to the database'
                },
                token: deviceToken
            };

            admin.messaging().send(payload)
            .then(function(response) {
                return console.log("Successfully sent message:", response);
                })
                .catch(function(error) {
                return console.log("Error sending message:", error);
                });

        }, function (errorObject){
            console.log("The read failed: " + errorObject.code);
        });
    });

// SEND NOTIFICATION WHEN USER ADDED TO
// Listens for new tokens added to /Groups/{GroupID}/Members/{AndroidID} and sends
// notification to the members of the groups telling them a new user has joined said group
exports.groupJoinedNotification = functions.database.ref('/Groups/{GroupID}/Members/{AndroidID}')
    .onWrite((change, context) => {
        const GroupID = context.params.GroupID;
        const AndroidID = context.params.AndroidID;

        var ref = admin.database().ref(`/Groups/${GroupID}/Members`);
        return ref.once("value", function(snapshot){
            snapshot.forEach(function(childSnapshot){
                const deviceToken = childSnapshot.child("deviceToken").val();
                const payload = {
                    data: {
                        title: 'On Time - New addition to the team!',
                        body: 'A new user has joined your group in the On Time app. Give them a warm welcome!'
                    },
                    token: deviceToken
                };

                admin.messaging().send(payload)
                .then(function(response) {
                    return console.log("Successfully sent message:", response);
                    })
                .catch(function(error) {
                    return console.log("Error sending message:", error);
                    });
            });
        }, function (errorObject){
            console.log("The read failed: " + errorObject.code);
        });
    });

// TEST FUNCTION that runs every five minutes
exports.scheduledFunction = functions.pubsub.schedule('every 5 minutes')
    .onRun((context) => {
        console.log('This will be run every 5 minutes!');
        return console.log(Date.now());
    });



