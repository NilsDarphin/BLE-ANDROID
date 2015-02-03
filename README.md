# LEB-ANDROID

## The LEB Project

LEB is a college project which purpose is to allow you to control remotely the I/Os of a microcontroller.
The project consists of a Bluetooth low energy shield, its firmware, and an Android application.

### Exemples of use

[Control the IOs from your Android](http://www.youtube.com/watch?v=B23XFl-bA60)

[Control a Car/Robot from your Android](http://www.youtube.com/watch?v=EagJ7pZ6EUM)


**LEB works with the following projects :**

Repository  | Description
------------- | -------------
NilsDarphin/LEBoard_hardware      |Schematics and design of the board (Eagle files)
NilsDarphin/LEBoard_firmware      |Firmware of the board
NilsDarphin/LEB_Android           |Example of Android application

### What is it used for ?

The LEB Project allows you to develop easily Bluetooth low energy peripherals : 
Plug a LEB shield on your circuit and develop your own IOS or Android application.
You don't need to program the shield, everything is managed by the client (Smartphone, computer or smart home box...), so you have little electronic to do and no C/C++.

## The Android application
This android app gives you a basic example of use of a LEB shield. This app actually contains 3 activities :

### Proximity sensor
This activity displays the rssi of the remote shield in a progress bas. It can be used to estimate the distance between the client and the shield.

It doesn't use any I/O.

### IOs control
This activity display all the available IOs on the shield and give you direct control on them.

It uses all the I/Os.

### Landraider
This is a more advanced application. It allows you to control a bi-motor robot (1 motor to control the right wheels, 1 motor to control the left wheels)

It uses AO0 and DO0 for the left motor and AO1 and DO1 for the right wheels/
