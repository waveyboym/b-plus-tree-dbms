# b-plus-tree-dbms
A dbms which manages students in a bplus tree data structure and has a gui to interact with the bplus tree

# preview images
home page
![home page](presentation-img/home.png "home page")
search page
![home page](presentation-img/search.png "search page")

# steps to run this application

1.  Download the project files.
2.  Download IntelliJ from here: https://www.jetbrains.com/idea/download/?source=google&medium=cpc&campaign=9736964638&term=intellij&gclid=Cj0KCQjw94WZBhDtARIsAKxWG-8VaCtrTKUAL_6Ja3_7vDQu2YRCuJF8UTm7Or6MWGZCLCfgPvZiF1QaAqL8EALw_wcB#section=windows
3.  Download JavaFx files from here: https://gluonhq.com/products/javafx/ (what I used: OS-Windows, Version-18, Architecture-x64, Type-SDK
4.  Save the files to a global path that you can easily access on your computer eg: 'C:\<your paths>\JavaFX'
5.  Open IntelliJ and click open and select the path where you saved the project files
6.  When the project has loaded, you may turn to your left where you'll see a section in the projects area labeled as 'External Libraries'. You should see 2 libraries there, one labelled as  <openjdk 18> and another as lib. 
7. If you look at the menu bar, you should see a button labeled as "file". Click that.
8. Then browse down and find a section named Project Structure and click and a new window should pop up.
9. Click on the libraries section.
10. There should be one library already there labelled as "json-simple-1.1.1". If there isn't one then you will have to download it and add it manually.
    Steps to add the library manually: 
      - If you don't have the json-simple library, download it from here: https://jar-download.com/artifacts/com.googlecode.json-simple/json-simple/1.1.1/source-code
      - Locate the path of the file and add it as a library by going back to intellij->file->project structure->libraries, then at the top you should see a plus button          which will allow you to add libraries. Click on it. 
      - Click Java from the options that are presented to you.
      - Locate the path of the library then when you find it click on it (should be labelled as "<library-name>.jar") then click ok then apply.
11. You may close the project structure window and your changes will be shown in the projects area labeled as 'External Libraries'.
12. To add JavaFx, steps similar to 10 will be taken again.
13. Click on file->project structure->libraries.
14. Then at the top you should see a plus button which will allow you to add libraries. Click on it. 
15. Locate the path of the library then when you find it click on it (path should be labelled as "C:<your paths>\javafx-sdk-<version>\lib") then click ok then apply.
16. That will add the library but won't reflect visible changes onto projects area labeled as 'External Libraries'.
17. Final step, Click on the Run dropdown menu at the top then click on configurations. A new window should open that will allow you to add a new configuration.
18. If there is no configuration named Main already available, create a new Application configuration by clicking on the plus sign at the top.
19. Click on the configuration
20. Under Build and run, make the entry point of that configuration "Main" if it is not already set. If all is well, the IDE should not be saying it cannot find Class Main
21. If there are VM options with the exact text to add in step 22, then you are done and you may close the window and click build application to run the app.
22. Under Build and run, click on Modify options and click on "Add VM options". Close the prompt. In the VM options box, add this text:
 
    --module-path
    ${PATH_TO_FX}
    --add-modules
    javafx.fxml,javafx.controls,javafx.graphics,javafx.web
 23. Click on apply changes and close the window and click build application to run the app.
