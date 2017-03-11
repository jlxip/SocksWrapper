# SocksWrapper
_A socks5 proxy connector wrapper for java._


## HOW TO
### Simple connection
The library is made to be as easy and simple as possible.<br>
For making a simple connection ( CLIENT -> PROXY -> SERVER ), you can use the method `newConnection`, located in the class `SocksNewConnection`.<br>
The method returns a socket, which you shall use in order to send a receive data. The arguments are `{String PROXY, int PROXY_PORT, String SERVER, int SERVER_PORT}`.<br><br>

Example 1: connecting to a server through TOR.
```java
Socket s = SocksWrapper.newConnection("127.0.0.1", 9150, "myexternalip.com", 80);
```
<br><br>

### Chained connection
You can also make a chained connection ( CLIENT -> PROXY -> PROXY -> ... -> SERVER ), for this purpose, you shall use the method `newRecursiveConnection`, located in the class `SocksRecursiveConnection`.<br>
This method also returns a socket. The arguments are `{String[] PROXIES, int[] PROXIES_PORTS, String SERVER, int SERVER_PORT}`.<br><br>

Example 2: connecting to a server through a chain of proxies (TOR + an exit proxy).
```java
String[] IPs = new String[]{"localhost", "00.000.00.000"};	// Change the second one.
int[] PORTs = {9150, 63303};	// Change the second one.
Socket s = SocksRecursiveConnection.newRecursiveConnection(IPs, PORTs, "myexternalip.com", 80);
```
<br><br>

In both cases, if an error has ocurred, it will be printed out with `System.err.println` to the console. Also, the return value of the method will be null, this way you can end the program in case there was a failure, this way:
```java
if(s == null) return;
```
Slow proxy? No problem! You can change the entry proxy connection timeout like this:
```java
SocksWrapper.timeout = 10000; // 10000 milliseconds (10 seconds) instead of the default 5000 milliseconds (5 seconds).
```
