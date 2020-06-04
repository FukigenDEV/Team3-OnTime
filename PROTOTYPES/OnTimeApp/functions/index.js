const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// TEST FUNCTION that runs every five minutes
// exports.scheduledFunction = functions.pubsub.schedule('every 5 minutes')
//     .onRun((context) => {
//         console.log('This will be run every 5 minutes!');
//         return console.log(Date.now());
//     });
// END OF TEST FUNCTION

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

exports.SFDeleteAlarms = functions.pubsub.schedule('every 5 minutes')
  .onRun((context) => {
    const currentTime = Date.now();

    var ref = admin.database().ref(`/Groups`);
    ref.once("value", function(snapshot){
      snapshot.forEach(function(childSnapshot){
        if(childSnapshot.hasChild("Alarms")){
          const groupID = childSnapshot.child("groupId").val();
          childSnapshot.forEach(function(childSnapshot2){
            if(childSnapshot2.key === "Alarms"){
              childSnapshot2.forEach(function(alarmsSnapshot){
                const alarmName = alarmsSnapshot.child("name").val();
                const alarmMillis = alarmsSnapshot.child("milis").val();
                if (alarmMillis < currentTime){
                  console.log("ALARM", alarmName, "DELETED FROM GROUP", groupID);
                  var deletedalarm = admin.database().ref(`/Groups/${groupID}/Alarms/${alarmName}`);
                  deletedalarm.remove();
                }
              });
            }
          });
        }
      });
    }, function (errorObject){
        return console.log("The read failed: " + errorObject.code);
    });
  });
