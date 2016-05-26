# Tickit-connectors

Connectors for several EMRs via TickiT Notifications API.

## Installation

1. Make sure Java SDK is installed on the target machine.

```
java -version
```

2. Clone or download/unzip this repository.
3. Create a config.properties file, or use the provided sample:

```
cp config.properties.sample config.properties
```

4. Edit the configuration file. Most of its settings (AWS keys, staging API) are provided by Shift Health.

5. Execute the sanity test:

```
gradlew run
```
6. Send the results back to Shift Health.



