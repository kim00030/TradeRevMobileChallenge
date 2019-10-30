# TradeRevMobileChallenge

# Development environment

* Android Studio version - 3.4

* Minimum SDK - 21, Target SDK - 28

* androidx

# Major dependencies being used

* ViewModel & LiveData API, MVVM Architecture

* Retrofit

* RXJava/Android

* Glide

* GSON converter for retrofit

* Toasty

* DataBind

* espresso

# Testing tool
* espresso framework
* Testing file for running in espresso: Located at src/androidTest/java/com.dan.traderevmobilechallenge/ExampleInstrumentedTest.java
* How to use:
1. For performance, if you test the app with an emulator turn off all drawing options in setting on your emulator.
 Go to setting-> Developer options-> find "Window animation scale", "Transition animation scale" and "Animator duration scale" then they should be turned **off** for running the app in espresso more smoothly. 

2. Open file "ExampleInstrumentedTest.java" and you can run it for testing the app. I created the testing methods in the class with the comments.
 
