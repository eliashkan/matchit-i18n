# Match-IT i18n module

## How to run
To package and assemble the binary artifact run the following command
    
    $ mvn clean package

This will generate two binaries. One for Unix systems and a .bat file for Windows systems.

## To generate outgoing csv's (after packaging)

On Linux or MacOS use this:
    
    $ ./target/appassembler/bin/matchit-i18n-generate-outgoing

On Windows use this:

    $ ./target/appassembler/bin/matchit-i18n-generate-outgoing.bat

## To parse incoming csv's (this part not yet written)

On Linux or MacOS use this:
    
    $ ./target/appassembler/bin/matchit-i18n-parse-incoming

On Windows use this:

    $ ./target/appassembler/bin/matchit-i18n-parse-incoming.bat

Or you can just run the main method in your IDE.
