# android-remote-communication

This is a simple example of communication between applications, and thus processes, in Android. This requires a few components that aren't required for communication between components inside the same process: 

### [Message](https://developer.android.com/reference/android/os/Message)
Class that can hold data to pass between threads or processes. Set `what` to a constant that describes the type of action you want to take. Set `arg1` or `arg2` to integer data you want to pass, or set `obj` to a [`Parcelable`](https://developer.android.com/reference/android/os/Parcelable) object to send that.

### [Messenger](https://developer.android.com/reference/android/os/Messenger)
Acts as a bridge for sending or receiving [`Messages`](https://developer.android.com/reference/android/os/Message) across processes. When receiving, set a `Handler`(https://developer.android.com/reference/android/os/Handler) on the Messenger to determine what happens when the message is received.

### [Handler](https://developer.android.com/reference/android/os/Handler)
Object that can accept a received message and act on it.

As it stands, you can see the described communication by running the two applications on a device, entering a number in the text field of one, and seeing the result propagate to a text field in the other. The repository contains two applications:

### 1. Walkie

This app holds a service running in its own remote process. It can accept incoming messages, which run through its handler to post data or add/remove a client to post the data to. For instance, the main fragment in this app adds a listener for posted data that in turn will update its TextView.

### 2. Talkie

This app creates an intent to start and bind to the remote service in Walkie. It can then post data to Walkie using an Android Messenger class.

In order to see the apps in action, do the following:

1. Build and run Walkie.
2. Leaving Walkie in the foreground, build and run Talkie over the top.
3. In Talkie, enter a number in the EditText field and press Send.
4. Exit Talkie, enter Walkie. You should now see the number you entered in Talkie.

Note: you must run Talkie without exiting Walkie to the home screen or to another app first. WalkieService is a background service; it has no notification or related UI to keep it in the foreground when you exit Walkie. That means that Android is likely to stop the service if you exit Walkie.
