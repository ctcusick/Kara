# Kara

Kara is a Discord-to-Telnet bridge bot implemented using the [Apache Commons Net](https://commons.apache.org/proper/commons-net/) library and [Discord4J](https://github.com/Discord4J/Discord4J).

Users may find it to be a work-in-progress. They would be correct.

## Discontinued

Please do not look at or use this. It is broken.

## Usage

Place an XML file (described below) named kara_config.xml with your Discord bot API token in the directory the bot is running from.

### kara_config.xml
kara_config.xml should have one element named authKey enclosing your API token.

```xml
<?xml version="1.1"?>
<authKey>apitokenhere</authKey>
```

### Commands

**/connect**  
Connect to the default Telnet server

**/send [text]**  
Send the specified text to the server

**/sendCtrl [caret notation character]**  
Send the specified ctrl character to the server

**/sendDel**  
Send the delete character to the server

**/disconnect**  
Disconnect from the server

**/help**  
Display this message.

## To Do

* Remove spaghetti
* Figure out how to manage object lifecycles
* Implement some semblance of architecture
