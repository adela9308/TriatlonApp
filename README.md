# _Problema Triatlon_

Aplicație simplă, însă care înglobează multe aspecte teoretice (șablonul observer, Spring Remote cu RMI, ORM-Hibernate, servicii REST, etc.). 
Ca și structură, aceasta este organizată în mai multe proiecte Java, toate fiind încapsulate tot într-un proiect Java principal.

Aplicația a fost dezvoltată pentru a asista organizatorii unui concurs de triatlon în gestiunea rezultatelor 
obținute de participanți, fiecare persoana responsabilă de o probă (arbitrul) are un cont de utilizator în cadrul 
aceste aplicații. 


## Tehnologii

- Limbaj de programare: Java 
- GUI: JavaFX
- Bază de date: SQLite

## Funcționalități

- _Login:_ După autentificarea cu succes, o nouă fereastră se deschide în care sunt afișate numele arbitrului,
lista participanților și numărul total de puncte obținut până acum de fiecare participant. Participanții sunt
afișați în ordine alfabetică după nume.
- _Adăugare rezultat:_ La finalul unei probe, arbitrul responsabil de proba respectivă introduce pentru fiecare
participant numărul de puncte obținut (dacă este cazul). După adăugarea unui nou rezultat, lista
participanților și numărul total de puncte obținute se actualizează automat pentru toți arbitrii.
- _Raport:_ Un arbitru poate vizualiza în altă listă/alt tabel toți participanții care au obținut puncte la proba
respectivă în ordine descrescătoare după numărul de puncte obținut. 
- _Logout_

## Rulare
- Se rulează serverul din subproiectul TriatlonServer, fișierul StartRPCServer.java (TriatlonServer\src\main\java\StartRPCServer.java)
- Se rulează clientul/ clienții din subproiectul TriatlonClient, fișierul MainFX.java (TriatlonClient\src\main\java\MainFX.java)

## DEMO al aplicației

La deschiderea aplicației se deschide ecranul de Login/Register, unde utilizatorul se poate autentifica.
<p align="center">
     <img src = "imagini_readme\1.PNG" height="300" width="200" style="float:left">
</p>

Odată logat, se va deschide ecranul principal al aplicației. Se vor afișa 3 liste: cea din stânga reprezentând 
toți participanții din concurs, cea din mijloc conținând participanții la care arbitrul logat poate să le ofere 
punctaje, iar cea din urmă, lista din dreapta ecranului, surprinzând concurenții care au participat la proba asignată 
arbitrului logat (le-au fost acordate punctaje de către acest arbitru).
<p align="center">
     <img src = "imagini_readme\2.PNG" height="300" width="500" style="float:left">
</p>

Pentru a puncta un participant este nevoie ca acesta să fie selectat din lista din mijloc, punctul sa fie trecut 
în casuța de input, după care să se acționeze butonul _Add points_.
<p align="center">
     <img src = "imagini_readme\3_1.PNG" height="300" width="200" style="float:left">
</p>

În urma punctării unui concurent, acesta dispare din lista din mijloc și punctajul acestuia se modifică atat în 
lista cu toți participanții, dar și în cea care îi conține doar pe cei punctați de arbitrul logat.
<p align="center">
     <img src = "imagini_readme\3_2.PNG" height="300" width="500" style="float:left">
</p>

- LogOut - Prin acționarea butonului din colțul din dreapta sus, utilizatorul va fi automat delogat.
