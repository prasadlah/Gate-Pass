'use-strict'

const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.firestore.document("Register/{register_id}/Notification/{notification_id}").onWrite(event=> {
    const register_id = event.params.register_id;
    const notification_id = event.params.notification_id;

    console.log("User Id : " + register_id + " | Notification Id : " + notification_id);
});