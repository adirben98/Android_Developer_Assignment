User List Application
Description
This is a simple Android application that displays a list of users fetched from an API and stores them locally using Room.

Features
Fetch and display a list of users from a remote API.
Local data storage using Room for offline access.
Image upload feature using Firebase Storage.
Swipe-to-refresh functionality.
Error handling and user feedback with Toast messages.

Prerequisites
Before you can build and run the project, ensure you have the following installed:

Android Studio: Version 4.1 or higher
Java Development Kit (JDK): Version 8 or higher
Gradle: Latest version (usually comes with Android Studio)

Installation and Setup

1.Clone the repository:

Copy code
git clone https://github.com/yourusername/your-repo-name.git

2.Open the project in Android Studio:

Launch Android Studio.
Click on "Open an existing project."
Navigate to the directory where you cloned the project and select it.

3.ensure you have set up Firebase for the project.
Place your google-services.json file in the app directory.

4.Run the project:

Connect your Android device or start an emulator.
Click on the "Run" button (green triangle) in Android Studio.
Select your device or emulator to run the app.



Assumptions

Pagination Handling:

Implementing pagination with LiveData was hard for me since i haven't been learned in college. I'v tried but it was a bit challenging.

Error Handling and User Feedback:

Implementing a user-friendly way of handling errors, especially with network requests, was a key focus. Ensuring that the user received immediate feedback through Toast messages required careful management of the UI thread.
Firebase Integration:



