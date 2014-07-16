ServerSocketListener
====================

Android Implementation for Socket Listener Server/Client mode

This project, build with Android Studio 0.8, implements 2 classes:

- **ServerSocketActivity.java**
- **ClientSocketActivity.java**

this classes extends android.app.Activity class and try to open connections in order to create a TCP Server or a TCP Client.

###ServerSocketActivity

This class use a java.net.ServerSocket and a java.net.Socket in order to bind a port and start listening on it.

This class is an abstract class that define an abstract method:
```
protected abstract void onDataReceive(String datas);
```
This method must be implemented in the subclasses and it is called every time the ServerSocket received a String, that is passed as argument.

To enable the ServerSocket, the Activity must put an Extra in the Intent that starts the Activity. This Extra must be an Integer and represent the Port on which the ServerSocket listens.

The KEY for the Extra is: **"me.corti.serversocketlistener.SERVER_PORT"**
*You can find this key in the static attribute SERVER_PORT of the Activity.*

#####An Example on implementation


```
class MyServerSocketActivity extends ServerSocketActivity
{

  protected void onDataReceive(String datas){
    TextView tv = (TextView) this.findViewById(R.id.mytextview);
    tv.append(datas+"\n");
  }

}

```

You can start this Activity in this way:

```
Intent newIntent = new Intent(this, MyServerSocketActivity.class);
newIntent.putExtras(MyServerActivity.SERVER_PORT, 5555);
startActivity(newIntent);
```
