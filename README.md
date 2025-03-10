# Multiplayer Quiz Game 🎮

A network-based interactive quiz application featuring real-time multiplayer functionality with distinct roles for administrators and examinees. 

## Features 🌟

- **Multi-Client Architecture**: Supports simultaneous connections from multiple users 
- **Role-Based Access** 👥:  
  - Admin: Creates and manages quiz questions 
  - Examinees: Participate in the quiz and receive real-time feedback 
- **Real-Time Scoring** ⚡: Instant feedback and score tracking 
- **Interactive Question Management** 📝: Admins can add questions during runtime 
- **Concurrent User Support** 👥: Handles multiple examinees simultaneously 

## Screenshots 📸

### Admin Dashboard
![Admin Dashboard](screenshots/admin-dashboard.png)

### Examiner Dashboard
![Examiner Dashboard](screenshots/examiner-dashboard.png)

### Admin and Examiners in Action
![Admin and Examiners](screenshots/ALL.png)  
*This screenshot shows the Admin dashboard alongside two Examiner dashboards running simultaneously.*


## Technical Stack 🛠️

- Language: Java ☕
- Networking: Socket Programming 🔌
- Architecture: Multi-threaded Server 🔄

## Project Structure 📁

``` 
├── src/
│   ├── QuizServer.java      # Main server implementation
│   ├── AdminClient.java     # Admin client interface
│   ├── ExaminerClient.java  # Examinee client interface
├── screenshots/
│   ├── admin-dashboard.png  # Admin dashboard screenshot
│   ├── examiner-dashboard.png  # Examiner dashboard screenshot
│   └── All.png              # Combined view screenshot
``` 

## Setup and Installation 💻

### Prerequisites 📋

- Java Development Kit (JDK) 8 or higher 
- Java Runtime Environment (JRE) 
- Command-line interface or IDE 

### Running the Application ▶️

1. **Compile the Source Files** 

```bash 
javac src/QuizServer.java src/AdminClient.java src/ExaminerClient.java 
``` 

2. **Start the Server** 

```bash 
java src.QuizServer
``` 

3. **Launch Admin Client** 

```bash 
java src.AdminClient 
``` 

4. **Launch Examinee Client(s)** 

```bash 
java src.ExaminerClient
``` 

## Usage Guide 📚

### For Administrators 👨‍💼

1. Launch the AdminClient 
2. Input questions in the format: `question;optionA;optionB;optionC;optionD;correctOption` 
3. Type 'done' when finished adding questions 
4. Type 'exit' to close the admin session 

Example question format: 
``` 
What is the capital of Germany?;Berlin;Paris;London;Madrid;A 
``` 

### For Examinees 👨‍🎓

1. Launch the ExaminerClient 
2. Enter your name when prompted 
3. Answer questions as they appear 
4. Receive immediate feedback on answers 
5. View final score upon completion 

## Technical Details ⚙️

### Network Configuration 🌐

- Default Port: 12345 
- Server Address: localhost (127.0.0.1) 

### Protocol 📡

- Simple text-based protocol 
- Question format: `question;optionA;optionB;optionC;optionD;correctAnswer` 
- Answers accepted: A, B, C, or D 

## Error Handling ⚠️

- Connection loss detection 
- Invalid input validation 
- Graceful client disconnection 
- Server-side exception management 

## Contributing 🤝

1. Fork the repository 
2. Create a feature branch 
3. Commit your changes 
4. Push to the branch 
5. Create a Pull Request 

## Future Enhancements 🚀

- GUI implementation 
- Database integration for question storage 
- Timer-based questions 
- Multiple quiz categories 
- User authentication 
- Score persistence 
- Leaderboard functionality 

## Repository 📦

The project is hosted on GitHub at: 
[https://github.com/Praveen-Hapuarachchi/Quiz-Master-Server.git](https://github.com/Praveen-Hapuarachchi/Quiz-Master-Server.git) 

## License ⚖️

This project is open source and available under the MIT License. 

## Contact 📫

- Email: hapup14@gmail.com 📧
- LinkedIn: [Praveen Hapuarachchi](https://www.linkedin.com/in/praveen-hapuarachchi) 💼
- GitHub Issues: For technical questions and support, please open an issue in the project repository 🔧