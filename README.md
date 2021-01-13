# Match-IT i18n module

## How to run
To package and assemble the binary artifact run the following command
    
    $ mvn package appassembler:assemble

This will generate two binaries. One for Unix systems and a .bat file for Windows systems.

On Linux or MacOS use this:
    
    $ ./target/appassembler/bin/matchit-i18n

On Windows use this:

    $ ./target/appassembler/bin/matchit-i18n.bat

# User Stories
- "oude versie" is de versie van de fr/nl.json files die in de i18n module blijven staan nadat een dev ze converteert, doorgestuurd heeft en het antwoord van de helpdesk verwerkt heeft.

## er zijn aanpassingen of toevoegingen op dev gebeurd, doorsturen naar helpdesk
    - ~~dev kopieert nieuwe fr en nl naar i18n module~~
    - dev edit i18n files in matchit-react-frontend/src/config/i18n/locales
    - matchit-i18n-module zit in parent matchit project, leest automatisch aangepaste fr en nl json files uit en kan direct vergelijken met oude versie die in i18n module is gebleven.
    - ~~de oude versie moet in die module blijven staan, zodat de dev die nu i18n in orde moet brengen de vorige versie nog heeft en niet moet gaan zoeken in git~~ de oude versie blijft automatisch in i18n module staan.
    - verantwoordelijkheid van devs om 'oude versie' te committen
    - i18n checkt welke objecten zijn aangepast en toegevoegd
    - output deze objecten naar CSV met bijhorende aanduiding new/edited
    - conversie CSV > XLS/XLSX

## er zijn objecten die nog vertaald moeten worden in fr of nl versie
    - dev kopieert fr en nl naar i18n utility module
    - i18n checkt welke objecten beginnen met FR_ of NL_
    - output deze objecten naar CSV
    - conversie CSV > XLS/XLSX
    - CSV tussenstap nodig... ?
    
## mogelijkheid tot 'new & edited' + 'nog te vertalen' in één operatie of aparte operaties
    - output sowieso aparte files voor aanpassing/nieuw <> nog te vertalen objecten
    
## dev krijgt XLS/XLSX terug van helpdesk
    - 1 set met aangevulde vertalingen, 1 set met verificatie aanpassingen & nieuwe labels
    - i18n converteert excel terug naar json objecten
    - voegt ze samen in up to date fr en nl json files
    - schrijft nieuwe fr en nl json files weg zowel naar matchit-react-frontend/src/config/i18n/locales en naar matchit-i18n/archive
