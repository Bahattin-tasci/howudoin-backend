This project was developed by two people as part of the Mobile Application Development course. I worked on the backend side, while my project partner worked on the frontend. 
The system will allow users to register and log in using an email and password. The login process will be secured with JWT-based authentication. 
Users will be able to send friend requests to each other, and once a request is accepted, they will be able to send messages to their friends. 
Users who are mutual friends can send and receive messages from each other.
MongoDB will be used as the database on the backend, and user, group, and message data will be stored there. 
The collections will be organized as "Users," "Groups," and "Messages." Users can create groups and send messages to all members of the group.
Each group will have a unique group ID, and messages sent to the group will be stored in MongoDB associated with this ID. Users will be able to view the group members and the message history of the group.
