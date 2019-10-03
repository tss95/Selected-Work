# Selected-Work

inf101.v19.sem2:

I sem2 folderet finner du min implementasjon på battlehip spillet. Gitt et basic
grensesnitt måtte vi lage regler for spillet og en trivielt lett AI å spille mot 
(skyter tilfeldig på kartet). Som en ekstra oppgave lagde jeg en litt mer avansert
AI som brukte informasjonen fra sine tideligere skudd til å ta litt mer optimale valg.
Som du kan lese i README i prosjekt folderet forklarer jeg hva som var bra og hva som
var dårlig med koden. Kort oppsummert så skrev jeg en spaghetti kode for AIen. Hvis jeg skulle
gjort det på nytt ville jeg delt koden opp i mangen forksjellige kortere funksjoner for å
gjøre den lettere å teste og mer leselig. Jeg ville også latt være å blande sammen UI
og game klassene. 
For å kjøre koden trenger dere JavaFX JDK. Kjør 'Main' klassen for å prøve spillet.

Project1

project.py er mitt forsøk på å skrive en tree diagram (ID3) algoritme fra bunnen av.
Her implementerer jeg algoritmen med både Gini og Entropy som urenhetsmål. 
Jeg kan fint gjøre algoritmen bedre ved å endre funksjonen "getPotentialSplits" til å
finne den beste splitten for kolonne, men for å holde test hastigheten lav valgte jeg
å bruke gjennomsnittet av hver kolonne som potensielt split. På grunn av dette valget ble
nøyaktigheten min 2% lavere enn sklearn sin version. 

Hvorfor disse kodene?
Jeg valgte battleship spillet ettersom det viser hvordan jeg klarer å
bryte ned ett komplekst problem ned i mindre deler og gjennom dette finne en
løsning. Det var en oppgave som viste meg viktigheten av å holde meg unna
spaghetti programering og hvor mye lettere oppgaven ville vært dersom jeg
brukte bedre teknikker. ID3 algoritmen viser at jeg lærte av min erfaring i battleship
og her brøt jeg ned hvert element av algoritmen ned til sin egen funksjon.
Uten en slik metode kan større projekter raskt bli umulig å lese, teste og dermed
bugfikse. 


