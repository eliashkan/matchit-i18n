# Match-IT i18n module

## How to assemble and run

We :heart: shell scripts! To package and assemble the binary artifact run the following command:
    
    $ ./mvnw clean package
    
This will execute the `assemble` goal from the appassembler plugin, as well as generate the required .class files.
    
Two binaries will be generated. One for Unix systems and a .bat file for Windows systems.

### To generate outgoing excel files

On Linux or MacOS use this:
    
    $ ./target/appassembler/bin/i18n out

On Windows use this:

    $ .\target\appassembler\bin\i18n.bat out

### To parse incoming excel files

On Linux or MacOS use this:
    
    $ ./target/appassembler/bin/i18n in

On Windows use this:

    $ .\target\appassembler\bin\i18n.bat in

Or you can just run the main method in your IDE.

** Be sure to run from the root directory with `./target/appassembler/bin/i18n` and not navigating into the folder and running with ./i18n, it will get confused about filepaths!**

---

# FAQ

## I'm getting a `Missing required subcommands` error when running the i18n binary. What do?

You need to specify a subcommand. 

| Commands          | Effect 
| ----------------- | -----------------
| ./i18n **in**     | Parse incoming i18n files reviewed by helpdesk
| ./i18n **out**    | Generate excel sheets for i18n purposes to share with helpdesk

## Where do I put the old and new json files?

New json files are put in `src/main/resources/new`. The old ones with which the program will compare the updated ones should go in the `src/main/resources/old` folder. 

**There should be a consensus on how and where these shall be preserved...** Maybe after processing the incoming the 'new' ones can be put in the old folder and the old ones thrown out manually by the dev. The date metadata can come in handy here.

## Where are the generated labels that I can send to helpdesk?

For now they are placed in `target/generated-i18n-files/`. There you will find `labelsForReview.xlsx` & `labelsForReview.xls` which are suitable to send out to helpdesk. They are identical in content.

There's a directory called `target/generated-i18n-files/csv/` where you will find the intermediate csv's. You can use these to check which labels were changed, which were not yet translated and which were newly added. It's these sets that are combined in `combinedLabelsForReviewNL/FR.csv`.
 
There you will also find `labelsNotSelectedForReviewFR/NL.csv` which should be kept. These will be used for merging the reviewed labels that come back from helpdesk. The same dev will have to do the outgoing operation and process the incoming in order to have these complementary labels, or at least share them with the dev that will do the incoming operation.

## Where do I put the excel file that helpdesk sent back?

I've pointed the parsing programming to `src/main/resources/labelsForReview.xls` or `.xlsx`, either one is fine.

Put the **reviewed excel file AND both `labelsNotSelectedForReviewFR/NL.csv` files** in `src/main/resources/` and run the parser to generate incremented json for each language.
 
## Where do I find the final incremented json after parsing?

They will be exported to `.json` files in `target/generated-i18n-files/incremented-json/`.
