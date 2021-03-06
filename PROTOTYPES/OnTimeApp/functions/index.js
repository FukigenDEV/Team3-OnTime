const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

//--------------------------------------------------------------------------------------------//
// DEPRECATED AS IT IS UNNECCESARY IN THE APP AND WAS ONLY USED FOR TESTING

// SEND NOTIFICATION WHEN DEVICE TOKEN ADDED TO DB
// Listens for new tokens added to /Users/{AndroidID}/deviceToken and sends
// notification the device of the new token
// exports.newUserNotification = functions.database.ref('/Users/{AndroidID}/deviceToken')
//     .onWrite((change, context) => {
//         const AndroidID = context.params.AndroidID;
//
//         var ref = admin.database().ref(`/Users/${AndroidID}/deviceToken`);
//         ref.once("value", function(snapshot){
//         const deviceToken = snapshot.val();
//             const payload = {
//                 data: {
//                     title: 'Hello from Firebase',
//                     body: 'New user has been added to the database'
//                 },
//                 token: deviceToken
//             };
//
//             admin.messaging().send(payload)
//             .then(function(response) {
//                 return console.log("Successfully sent message:", response);
//                 })
//                 .catch(function(error) {
//                 return console.log("Error sending message:", error);
//                 });
//
//         }, function (errorObject){
//             console.log("The read failed: " + errorObject.code);
//         });
//     });
//--------------------------------------------------------------------------------------------//



//--------------------------------------------------------------------------------------------//
// REMOVED TO NOT CONFLICT WITH THE NEW ALARM FUNCTIONS IN THE APP
// MIGHT BE REWORKED IN THE FUTURE

// DELETE PASSED ALARMS
// This function automatically runs every 5 minutes and checks the database (Groups node)
// Every alarm in the database gets comparet to the current time
// If the alarm time is behind the current time, the alarm gets deleted
// exports.SFDeleteAlarms = functions.pubsub.schedule('every 10 minutes')
//   .onRun((context) => {
//     const currentTime = Date.now();
//
//     var ref = admin.database().ref(`/Groups`);
//     return ref.once("value", function(snapshot){
//       snapshot.forEach(function(childSnapshot){
//         if(childSnapshot.hasChild("Alarms")){
//           const groupID = childSnapshot.child("groupId").val();
//           childSnapshot.forEach(function(childSnapshot2){
//             if(childSnapshot2.key === "Alarms"){
//               childSnapshot2.forEach(function(alarmsSnapshot){
//                 const alarmName = alarmsSnapshot.child("name").val();
//                 const alarmMillis = alarmsSnapshot.child("milis").val();
//                 if (alarmMillis < currentTime){
//                   console.log("ALARM", alarmName, "DELETED FROM GROUP", groupID);
//                   var deletedalarm = admin.database().ref(`/Groups/${groupID}/Alarms/${alarmName}`);
//                   deletedalarm.remove();
//                 }
//               });
//             }
//           });
//         }
//       });
//     }, function (errorObject){
//         return console.log("The read failed: " + errorObject.code);
//     });
//   });
//--------------------------------------------------------------------------------------------//


// SEND NOTIFICATION WHEN USER ADDED TO
// Listens for new tokens added to /Groups/{GroupID}/Members/{AndroidID} and sends
// notification to the members of the groups telling them a new user has joined said group
exports.groupJoinedNotification = functions.database.ref('/Groups/{GroupID}/Members/{AndroidID}')
    .onWrite((change, context) => {
      if(change.before.exists()){
        return null;
      }
      if(!change.after.exists()){
        return null;
      }
      const groupId = context.params.GroupID;
      const androidId = context.params.AndroidID;
      var ref = admin.database().ref(`/Groups/${groupId}/Members`);
      return ref.once("value", function(snapshot){
        snapshot.forEach(function(childSnapshot){
          const deviceToken = childSnapshot.child("deviceToken").val();
          const deviceId = childSnapshot.key;
          if(androidId !== deviceId){
            const payload = {
              data: {
                title:  'On Time - New addition to the team!',
                body:   'A new user has joined your group in the On Time app. Give them a warm welcome!'
              },
              token: deviceToken
            };
            admin.messaging().send(payload)
            .then(function(response) {
              return console.log("Successfully sent message:", response, deviceToken);
              })
              .catch(function(error) {
              return console.log("Error sending message:", error, deviceToken);
              });
          }
          if(androidId === deviceId){
            console.log("Message not sent to new user: ", deviceToken);
          }
        });
      }, function (errorObject){
          console.log("The read failed: " + errorObject.code);
      });
    });

// MONITOR NEW ALARMS
// This function listens for new tokens added to the Alarms child of the Groups node
// When a new alarm is added, all of the members of the group get assigned to the alarm
// The state of the members is then changed to 'Not awake'.
exports.SetAlarmUserState = functions.database.ref('/Groups/{GroupID}/Alarms/{AlarmName}')
  .onWrite((change, context) => {
    if(change.before.exists()){
      return null;
    }
    if(!change.after.exists()){
      return null;
    }
    const groupID = context.params.GroupID;
    const alarmName = context.params.AlarmName;

    var ref = admin.database().ref(`/Groups/${groupID}/Members`);
    return ref.once("value", function(snapshot){
      snapshot.forEach(function(childSnapshot){
        const deviceToken = childSnapshot.key;
        admin.database().ref(`/Groups/${groupID}/Alarms/${alarmName}/MemberState`).child(deviceToken).set("NOT AWAKE!");
      });
    }, function (errorObject){
        console.log("The read failed: " + errorObject.code);
    });
  });

// NOTIFY OF ALARM
// This function runs every 5 minutes and checks whether an alarm has gone of in the last five minutes.
// If so, the members of the group get notified about the alarm in case their actual alarm has not gone off.
exports.NotifyOfAlarm = functions.pubsub.schedule('every 5 minutes')
  .onRun((context) => {
    var currentTime = Date.now();

    var ref = admin.database().ref(`/Groups`);
    return ref.once("value", function(snapshot){
      snapshot.forEach(function(childSnapshot){
        if(childSnapshot.hasChild("Alarms")){
          const groupID = childSnapshot.child("groupId").val();
          const groupName = childSnapshot.child("groupName").val();
          childSnapshot.forEach(function(childSnapshot2){
            if(childSnapshot2.key === "Alarms"){
              childSnapshot2.forEach(function(alarmsSnapshot){
                const alarmName = alarmsSnapshot.child("name").val();
                const alarmMillis = alarmsSnapshot.child("milis").val();
                if (alarmMillis < currentTime && alarmMillis > (currentTime - 300000)){
                  console.log(alarmName + " of " + groupID + " passes the check, notifications distributed");
                  childSnapshot2.child(alarmName).child("MemberState").forEach(function(memberStateSnapshot){
                    const memberID = memberStateSnapshot.key;
                    const memberState = memberStateSnapshot.val();
                    if (memberState === "NOT AWAKE!"){
                      admin.database().ref(`/Users/${memberID}`).once('value', function(userSnapshot){
                        const deviceToken = userSnapshot.child('deviceToken').val();
                        const memberName = userSnapshot.child('name').val();
                        const payload = {
                          data: {
                            title:  'Wake up ' + memberName + "!",
                            body:   'An alarm in your squad ' + groupName +  ' is about to go off any minute! Make sure you do not miss it!'
                          },
                          token: deviceToken
                        };
                        admin.messaging().send(payload)
                        .then(function(response) {
                          return console.log("Successfully sent message:", response, deviceToken);
                          })
                          .catch(function(error) {
                          return console.log("Error sending message:", error, deviceToken);
                          });
                      }, function (errorObject){
                          return console.log("The read failed: " + errorObject.code);
                      });
                    }
                  });
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

// NOTIFICATION OF A COMPLETED TASK
// This function notifies the members of a squad when one of the members completes one of their tasks

exports.taskCompletedNotification = functions.database.ref('/Groups/{GroupID}/Members/{MemberID}/Tasks/{task}')
  .onWrite((change, context) => {
    if(!change.before.exists()){
      return null;
    }
    if(!change.after.exists()){
      return null;
    }
    const groupID = context.params.GroupID;
    const memberID = context.params.MemberID;
    const taskName = context.params.task;
    const oldState = change.before.val();
    const newState = change.after.val();
    var memberName;
    console.log("old: " + oldState);
    console.log("new: " + newState);
    if(newState === "Finished"){
      admin.database().ref(`/Users/${memberID}`).once('value', function(userSnapshot){
        memberName = userSnapshot.child('name').val();
      }, function (errorObject){
          return console.log("The read failed: " + errorObject.code);
      });
      var ref = admin.database().ref(`/Groups/${groupID}/Members`);
      return ref.once("value", function(snapshot){
        snapshot.forEach(function(childSnapshot){
          const userAndroidId = childSnapshot.key;
          const deviceToken = childSnapshot.child("deviceToken").val();
          if (userAndroidId === memberID) {
            console.log("Message not sent to task owner: ", deviceToken);
          } else {
            const payload = {
              data: {
                title:  'Your squad has progressed!',
                body:   memberName + ' has just finished a part of their mission! Good job!'
              },
              token: deviceToken
            };
            admin.messaging().send(payload)
            .then(function(response) {
              return console.log("Successfully sent message:", response, deviceToken);
              })
            .catch(function(error) {
            return console.log("Error sending message:", error, deviceToken);
            });
          }
        });
      }, function (errorObject){
          return console.log("The read failed: " + errorObject.code);
      });
    } else {
      return console.log("No action required")
    }
  });

//  NOTIFICATIONS ABOUT THE ALARM STATUS
//  This function sends notifications based on what the alarm status is of members of a said alarm
//  It is scheduled every 5 minutes, and fully triggers between 10 and 15 minutes after an alarm was supposed to go off
//  The state and combination of states is checked, and notifications are dispatched correctly
//  There are 4 possible notifications a user could get from this fuction:
//    -Everyone is awake;
//    -Everyone is asleep;
//    -User is awake, squad members are asleep;
//    -User is asleep, squad members are awake;

exports.AlamrStatusNotifications = functions.pubsub.schedule('every 5 minutes')
  .onRun((context) => {
    var currentTime = Date.now();
    var membersNotAwake = [];
    var membersAwake = [];
    var ref = admin.database().ref(`/Groups`);
    return ref.once("value", function(snapshot){
      snapshot.forEach(function(childSnapshot){
        if(childSnapshot.hasChild("Alarms")){
          const groupID = childSnapshot.child("groupId").val();
          const groupName = childSnapshot.child("groupName").val();
          childSnapshot.forEach(function(childSnapshot2){
            if(childSnapshot2.key === "Alarms"){
              childSnapshot2.forEach(function(alarmsSnapshot){
                const alarmName = alarmsSnapshot.child("name").val();
                const alarmMillis = alarmsSnapshot.child("milis").val();
                if (alarmMillis < (currentTime - 600000) && alarmMillis > (currentTime - 910000)){
                  console.log(alarmName + " of " + groupID + " is now late, looking through members");
                  childSnapshot2.child(alarmName).child("MemberState").forEach(function(memberStateSnapshot){
                    const memberID = memberStateSnapshot.key;
                    const memberState = memberStateSnapshot.val();
                    if (memberState === "NOT AWAKE!"){
                      membersNotAwake.push(memberID);
                    } else if (memberState === "AWAKE") {
                      membersAwake.push(memberID);
                    }
                  });
                  if(!Array.isArray(membersAwake) && !membersAwake.length && !Array.isArray(membersNotAwake) && !membersNotAwake.length){
                    return console.log("No members in alarm");
                  } else if (!membersAwake.length && membersNotAwake.length) {
                    for (var i = 0; i < membersNotAwake.length; i++){
                      const memberIdNA = membersNotAwake[i];
                      admin.database().ref(`/Users/${memberIdNA}`).once('value', function(userSnapshot){
                        const deviceTokenNA = userSnapshot.child('deviceToken').val();
                        const memberNameNA = userSnapshot.child('name').val();
                        const payloadNA = {
                          data: {
                            title:  'Wake up ' + memberNameNA + "!",
                            body:   'Nobody from your squad ' + groupName +  ' has woken up yet! Wake them up or you might fail todays mission!'
                          },
                          token: deviceTokenNA
                        };
                        admin.messaging().send(payloadNA)
                        .then(function(response) {
                          return console.log("Successfully sent message:", response, deviceTokenNA);
                          })
                        .catch(function(error) {
                        return console.log("Error sending message:", error, deviceTokenNA);
                        });
                      }, function (errorObject){
                          return console.log("The read failed: " + errorObject.code);
                      });
                    }
                  } else if (membersAwake.length && !membersNotAwake.length) {
                    for (var i2 = 0; i2 < membersAwake.length; i2++){
                      const memberIdA = membersAwake[i2];
                      admin.database().ref(`/Users/${memberIdA}`).once('value', function(userSnapshot){
                        const deviceTokenA = userSnapshot.child('deviceToken').val();
                        const memberNameA = userSnapshot.child('name').val();
                        const payloadA = {
                          data: {
                            title:  'Good Job ' + memberNameA + "!",
                            body:   'Everybody from your squad ' + groupName +  ' has woken up on time! Thats the spirit! Carry on like this, and you are going to become the best squad in the whole battlefield!'
                          },
                          token: deviceTokenA
                        };
                        admin.messaging().send(payloadA)
                        .then(function(response) {
                          return console.log("Successfully sent message:", response, deviceTokenA);
                          })
                        .catch(function(error) {
                        return console.log("Error sending message:", error, deviceTokenA);
                        });
                      }, function (errorObject){
                          return console.log("The read failed: " + errorObject.code);
                      });
                    }
                  } else if (membersAwake.length && membersNotAwake.length) {
                    var memberNamesNA = [];
                    for (i3 = 0; i3 < membersNotAwake.length; i3++){
                      const memberIdNA2 = membersNotAwake[i3];
                      admin.database().ref(`/Users/${memberIdNA2}`).once('value', function(userSnapshot){
                        const deviceTokenNA2 = userSnapshot.child('deviceToken').val();
                        const memberNameNA2 = userSnapshot.child('name').val();
                        memberNamesNA.push(memberNameNA2);
                        const payloadNA2 = {
                          data: {
                            title:  'Wake up ' + memberNameNA2 + "!",
                            body:   'Your comrades from your squad ' + groupName +  ' are waiting on you! Wake up and do not dissapoint your squad captain again!'
                          },
                          token: deviceTokenNA2
                        };
                        admin.messaging().send(payloadNA2)
                        .then(function(response) {
                          return console.log("Successfully sent message:", response, deviceTokenNA2);
                          })
                        .catch(function(error) {
                        return console.log("Error sending message:", error, deviceTokenNA2);
                        });
                      }, function (errorObject){
                          return console.log("The read failed: " + errorObject.code);
                      });
                    }
                    for (i4 = 0; i4 < membersAwake.length; i4++){
                      const memberIdA2 = membersAwake[i4];
                      admin.database().ref(`/Users/${memberIdA2}`).once('value', function(userSnapshot){
                        const deviceTokenA2 = userSnapshot.child('deviceToken').val();
                        const memberNameA2 = userSnapshot.child('name').val();
                        const payloadA2 = {
                          data: {
                            title:  'Attention ' + memberNameA2 + "!",
                            body:   'Some of your comrades (' + memberNamesNA + ') from your squad ' + groupName +  ' have not woken up yet! Make sure the rascals get up on time and complete your mission, or else...'
                          },
                          token: deviceTokenA2
                        };
                        admin.messaging().send(payloadA2)
                        .then(function(response) {
                          return console.log("Successfully sent message:", response, deviceTokenA2);
                          })
                        .catch(function(error) {
                        return console.log("Error sending message:", error, deviceTokenA2);
                        });
                      }, function (errorObject){
                          return console.log("The read failed: " + errorObject.code);
                      });
                    }
                  }
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
