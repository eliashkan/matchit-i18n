# Match-IT i18n module

## How to run
    $ mvn package appassembler:assemble
    ...
    $ ./target/appassembler/bin/matchit-i18n
    or
    $ ./target/appassembler/bin/matchit-i18n.bat

# User Stories

## er zijn aanpassingen of toevoegingen op dev gebeurd, doorsturen naar helpdesk
    - dev kopieert nieuwe fr en nl naar i18n module
    - OF module zit in matchit project, leest automatisch huidige fr en nl uit en kan direct vergelijken met vorige versie die in i18n module is gebleven.
    - de vorige versie moet in die module blijven staan, zodat de dev die nu i18n in orde moet brengen de vorige versie nog heeft en niet moet gaan zoeken in git
    - verantwoordelijkheid van devs om 'vorige versie' te committen op VCS
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
