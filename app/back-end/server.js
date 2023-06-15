const express = require('express'); //requires express module
const socket = require('socket.io'); //requires socket.io module
const fs = require('fs');
const app = express();
var PORT = process.env.PORT || 3000;
const server = app.listen(PORT); //hosts server on localhost:3000



app.use(express.static('public'));
console.log('Server is running');
const io = socket(server);

var count = 0;

var note = "";

//Socket.io Connection------------------
io.on('connection', (socket) => {

    console.log("New socket connection: " + socket.id)

    socket.on('counter', () => {
        count++;
        console.log(count)
        io.emit('counter', count);
    })

    socket.on('detection-reject', () => {
        //[ THIS DETECTION OBJECT/IMAGE --> DISCARD]
        //MAYBE PASS A DETECTION OR IMAGE-ID (assuming each detection image is stored as an object). STACK TYPE OR IDK
        socket.emit("confirmation-response", 'Detection Rejected by Pengawas.')
        console.log("Rejected detection")
    })

    socket.on('detection-confirm', (note) => {
        //[SAVE IMAGE OR WITH STRING (OF INFORMATION)?]
        console.log("Image saved, note: " + note)
        socket.emit("confirmation-response", `Detection confirmed successfully. \nNote: ${note} `)
        try {
            // ... something something something
            socket.emit("confirmation-response", `Detection confirmed successfully. \nNote: ${note} `)
        } catch (Exception) {
            socket.emit("confirmation-response", 'Detection confirmation failed.')
        }
    })

})