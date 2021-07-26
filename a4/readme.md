      Maitry Mistry
      20778013 mm3mistr
      openjdk version "15.0.2" 2021-01-19
      Windows 10 (YOGA 920-13IKB) 
      Android API 29 SDK
      Pixel C tablet AVD with API 29


# Features 
- To **Zoom**, Press Ctrl and Click on the screen. This will open the pinch gesture which can be used to zoom in/out on screen
    - If any other icons like draw, erase, or highlight are selected (if their respective icons are filled in black instead of having a black outline only), you won't be able to zoom. Make sure to unselect those other options to perform Zoom.
- **Undo**
    - If undo action is possible on the screen/page the Undo icon will be filled in black (instead of just having the black outline)
    - Press Undo button on action bar, to undo the action
    - You can undo any number of past actions on that page
- **Redo**
    - If redo action is possible on the screen/page the Redo icon will be filled in black (instead of just having the black outline)
    - Press Redo button on action bar, to redo the action
    - You can undo any number of past actions on that page

- **Highlight**
    - To highlight, press the Highlight icon button (once you press it, the icon will be filled in black meaning highlight function has been selected)
    - Perform highlight (drag mouse around the PDF page)
    - Click the Highlight icon AGAIN when you are done highlighting and want to perform something else (like draw or erase or zoom)-- you will see the highlight icon turn from being filled in black to just having the black outline which means you have successfully unselected the highlight option

- **Draw**
    - To draw, press the Draw icon button (once you press it, the icon will be filled in black meaning draw function has been selected)
    - Perform Draw (drag mouse around the PDF page)
    - Click the Draw icon AGAIN when you are done drawing and want to perform something else (like highlight or erase or zoom)-- you will see the draw icon turn from being filled in black to just having the black outline which means you have successfully unselected the draw option

- **Erase**
   - To erase, first make sure that all draw or highlight icons are not selected. Then click Erase button/icon. You will see the icon turn from the black outline to being filled in black which means you have successfully selected erase
   - To erase a line, just click on the line and it will disappear
   - Click the Erase icon again when you are done erasing and want to perfom another action-- you will see the erase icon turn from being filled in black to just have the black outline which means you have successfully un selected the erase option

- **Change Pages**
   - The sample PDF will have 3 pages.
   - To go to the next page, click the `+` button on the screen
   - To go to the previous page, click the `-` button on the screen


# Build Instrctions
  - The `app-debug.APK` file is placed in the `\app` folder. It can be used to launch the project 
  - You can also import the project into intellij, Click `Android` structure [instead of the default Project structure], and choose the Virtual device and click the `Play` button.
  - It may take some time/attempts for the app to launch. If you don't have enough memory on the Virtual device you may need to wipe the data and re launch the app

  - I have noticed that if the following output occurs in the `Run` console, then the app has launched successfully
    ```
    $ adb shell am start -n "net.codebot.pdfviewer/net.codebot.pdfviewer.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
    Connected to process 4529 on device 'Pixel_C_API_29 [emulator-5554]'.
    Capturing and displaying logcat messages from application. This behavior can be disabled in the "Logcat output" section of the "Debugger" settings page.
    I/debot.pdfviewe: Not late-enabling -Xcheck:jni (already on)
    E/debot.pdfviewe: Unknown bits set in runtime_flags: 0x8000
    W/debot.pdfviewe: Unexpected CPU variant for X86 using defaults: x86
    D/libEGL: Emulator has host GPU support, qemu.gles is set to 1.
    ```
  - If you get something like ` Launching 'app' on No Devices.`, then usually the VD ran out of memory and you need to wipe the data, or just try launching it again.


# Credits
The icons used for undo, redo, highlight, draw and erase buttons are free icons from https://www.flaticon.com/.

The starter code and sample PDF provided for the assignment was used for this project.


