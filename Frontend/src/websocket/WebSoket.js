// import React, { useEffect } from 'react';
// import { w3cwebsocket as W3CWebSocket } from 'websocket';

// const client = new W3CWebSocket('ws://localhost:8080/ws');

// function WebSocket(props) {
//     useEffect(() => {
//         client.onopen = () => {
//             console.log('WebSocket Client Connected');
//         };

//         client.onmessage = (msg) => {
//             const messageData = msg.data;
//             // if (typeof messageData === 'string') {
//             //     setMessages((prevMessages) => [...prevMessages, messageData]);
//             // } else if (messageData instanceof Blob) {
//             //     const reader = new FileReader();
//             //     reader.onload = function () {
//             //         const text = reader.result;
//             //         setMessages((prevMessages) => [...prevMessages, text]);
//             //     };
//             //     reader.readAsText(messageData);
//             // }
//             const dataSensor = JSON.parse(messageData);
//             props.save(dataSensor);
//         };
//     }, [props]);

//     return (
//         <div className="App">
//             {/* <h1>WebSocket Client</h1> */}
//             {/* <div>
//                 {renderMessage}
//             </div> */}
//       </div>
//     );
// }

// export default WebSocket;

