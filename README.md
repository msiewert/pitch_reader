# pitch_reader

BIDS Trading Coding Exercise

A simple program that reads a file containing a stream of pitch data, and outputs the Top 10 symbols by executed volume.

Sample data file is located in the root directory and named `pitch_data`. Replace or update as required.

## Build

To build the program, execute the following command:

`./gradlew clean shadowJar`

## Run

To run the program, execute the following command:

`./read_pitch.sh`

The test dataset located (`pitch_data`) in the root directory is used by default. To use a different file update
the `read_pitch.sh`
script.

## Test

`./gradlew clean test`

## Tools

- IntelliJ IDEA 2023.1.2 (Ultimate Edition)
- GitHub Copilot Plugin 
