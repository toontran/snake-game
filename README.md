# CSCI 205 Final Project
  **Welcome to this amazing snake game developed by the Team 3 Python!**
  
  **Team members:**
  
  Christopher Asbrock
  
  Tung Tran
  
  Lawrence Li
  
  Franco Perinotti
  
# Project Description

Everybody loves the good old classic game snake! 
In this project, we further enhance the game by adding a brand new online mode for 2 players to compete with each other! 
In addition to the 2 player multiplayer, we also have some other creativity with the snake game that will
enhance the gameplay compared to the original snake game. This includes some items that extended
from the original foods in the snake game. 

The game mechanics for snake game 2 will be the same from the original snake game. Here, Snake game 2 multiplayer will extend the basic, original mechanics and add interesting features and multiplayer mechanics as described above. 
The brand new multiplayer mechanics will allow players to have a thoughtful experience and have lots of fun.
The objective of this creative snake game is to use the Java programming concepts, including the multiplayer networking,
the Model, View, Controller design and Object-oriented principles. 

# Third Party libraries 

+ JavaFX-sdk-13
+ Junit-Jupiter:5.6.2

# Build and run instructions

The following instructions is tested successfully on Mac. 
The instruction will assume that you have this gitlab repository in your computer,
and you have this repository folder in your HOME location. 

Step 1 - Download Javafx-sdk-13 from the following download link:

Mac:
https://gluonhq.com/download/javafx-13-0-2-sdk-mac/

Linux:
https://gluonhq.com/download/javafx-13-0-2-sdk-linux/

Windows:
https://gluonhq.com/download/javafx-13-0-2-sdk-windows/


Step 2 - Once you downloaded it, unzip the file and store it somewhere in your computer.
Remember to store the file in a good location so you can know its directory path. 

Step 3 - Open terminal

Step 4 - Go to the repository folder by enter the following command:

**cd ~/csci205_final_project_sp2020** or **cd YOUR_DIRECTORY_HERE/csci205_final_project_sp2020** if you did not put
this repository in your HOME location.


Step 5 - Enter the following command



**java --module-path [INSERT YOUR JAVAFX LIBRARY DIRECTORY HERE] --add-modules=javafx.controls -jar ~/csci205_final_project_sp2020/dist/csci205_final_project_sp2020.jar**



The above command will assume that you have this repository folder in your HOME location. 

Also, your javafx library directory will be something like the following:



**your_directory_from_home/javafx-sdk-13.0.2/lib**



If you store the repository somewhere else, you need to replace the ~ in the command and enter your directory to this repository. 



Example: If you store it under /Users/USERNAME/Desktop:


/Users/USERNAME/Desktop/csci205_final_project_sp2020/dist/csci205_final_project_sp2020.jar


or for ~/Desktop as an example:


~/Desktop/csci205_final_project_sp2020/dist/csci205_final_project_sp2020.jar



 