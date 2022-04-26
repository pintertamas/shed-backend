#Haladási napló

##1. hét

Mivel a konzin felmerült hogy websocketet vagy mqtt protokollt használjunk a kommunikációra, ezért erről beszéltünk és végül az mqtt mellett döntöttünk.
Azért emellett döntöttünk, mivel azt olvastuk róla, hogy 
- Hatékonyan osztja szét az információt a kliensek között, ami azért jó, mert nem a felhasználóknak kell szűrniük az információt
- Minimális sávszélességet igényel, ez pedig esetünkben elég fontos tényező.
- Lightweight és gyorsan összerakható, ezzel sok fejelsztési időt lehet spórolni
- Megbízható, skálázható és bármilyen adatot lehet rajta közvetíteni

Elkészítettük a projekt [specifikációját](https://github.com/pintertamas/shed-backend/blob/master/docs/specifikacio.pdf) és megterveztük az adatmodellt, ami itt látható:

![adatmodell](https://github.com/pintertamas/shed-backend/blob/master/docs/data_model_1.png?raw=true)

Ezek után megbeszéltük, hogy hogyan fogjuk szétosztani a munkát az első pár héten.
A terv az, hogy amíg nincsen kész az alap kommunikációt biztosító backend, addig közösen dolgozunk rajta, majd ha elkészültünk vele és képesek leszünk bármiféle információt átadni rajta, elkezdjük a mobil kliens fejlesztését is.
Előre láthatólag Tomi fog többet foglalkozni a backenddel, én pedig a Flutteres résszel, viszont mindketten fogunk mindkét oldallal foglalkozni, a feladatokat pedig taskboard használatával fogjuk elosztani.


##2. hét
A levelezős konzultáció után megpróbáltunk egy rendes [ER diagramot](https://github.com/pintertamas/shed-backend/blob/master/docs/data_model_2.png?raw=true) készíteni és frissítettük a [specifikációt](https://github.com/pintertamas/shed-backend/blob/master/docs/specifikacio2.pdf), így már látható a játék részletes szabálya és el is képzelhető hogy a felhasználók mit fognak látni mikor játszanak a játékkal.

Kicsit jobban utánanéztünk hogy hogyan tudnánk a gyakorlatban használni a RabbitMQ-t és arra a döntésre jutottunk hogy a websocketes megközelítés valószínűleg találóbb lesz a kétirányú kommunikáció miatt.
Megcsináltunk egy Spring alapú websocketes kommunikációt lehetővé tevő alkalmazást és Flutterben is csináltunk hozzá egy demo alkalmazást amivel websocketen lehet küldeni és fogadni üzeneteket.
Csináltunk egy SSL Certificatet az alábbi [tutorial](https://www.thomasvitale.com/https-spring-boot-ssl-certificate/) segítségével, így már HTTPS-t használ az alkalmazásunk.

## 3. & 4. hét
A 3. hét elején újra terveztük az adatmodellünket, viszont a két hét alatt nagyon sokat változott.
Először erre változtattuk:

![](https://github.com/pintertamas/shed-backend/blob/master/docs/data_model_3.png?raw=true)

Ezzel meg is voltunk elégedve, de miután nekiálltunk megvalósítani, azt beszéltük meg hogy frissítjük, úgy, hogy az adatbázis táblák így nézzenek ki:

![](https://github.com/pintertamas/shed-backend/blob/master/docs/db_plan.png?raw=true)

A tervünk az, hogy a felső 3 táblát Redis-t használja cache memóriában tároljuk mivel ezeket folyamatosan olvasni és írni kell,
az alsó 3-at pedig PostgreSQL adatbázisban (mivel ezeket az adatokat ritkán változtatjuk, de hosszú távon kell tárolni), amit azóta már setupoltunk is.

####RedisDB:
- A CardConfig tábla azt fogja tárolni, hogy adott kártya szám - szín kombinációhoz melyik szabályt rendeltük adott játékban.
- A PlayerCards arra lesz jó, hogy a játékosok kezeiben lévő kártyákat számon tudjuk tartani olyan módon, hogy a játékos id-ja alapján lekérdezhetjük a kártyái beállításait (CardConfig) és a kártya állapotát (asztalon lefordítva/felfordítva, vagy a kezében)
- A Table cards ugyan erre van, csak az asztalon lévő kártyákat tárolja és azt, hogy húzó vagy rakó pakli rész-e

####PostgreSQL:
- A User tábla egy felhasználó adatait tárolja (felhasználónév, jelszó, valamint a felhasználó id-ját)
- A Game táblába a játékok elején írunk, mikor létrehozunk egy játékot és a státuszának változtatásakor szerkesztjük
- A Player tábla a játékosokat reprezentálja, segítségével le lehet kérdezni adott játékban lévő felhasználók neveit, ami jól fog jönni az asztalnál ülő játékosok neveinek kiírásához.

Hogy jobban elképzelhető legyen a játék, csináltam egy layout tervet draw.io-val, amit itt lehet megnézni:

![](https://github.com/pintertamas/shed-backend/blob/master/docs/mockup.png?raw=true)

A backenden elkezdtük megírni az alapokat, már tudunk regisztálni, bejelentkezni, meg a játék létrehozás is működik valamilyen szinten.
Hostoltuk Herokura is, amin hiba nélkül futott, lehetett becsatlakozni játék lobbykba és üzeneteket küldeni egymásnak.
A Flutteres részen létrehoztam egy skálázható mappa architektúrát [ennek](https://medium.com/flutter-community/scalable-folder-structure-for-flutter-applications-183746bdc320) a cikknek alapján.
Mobilon pedig be lehet járni az alkalmazás nagy részét, bescannelni qr kódokat és a bennük tárolt játék nevek alapján játékokba becsatlakozni.
Weben még nincs sok minden meg, de lehet generálni QR kódot ami kódol egy szöveget, ezt a kódot pedig megjeleníti a képernyőn, amit be lehet scannelni mobillal.
A login tesztelésekor belefutottam egy hibába, mindig timeoutolt a post kérésem. Azt a feltételezhető okot találtam erre, hogy a gépem egyik portjára küldöm a hívást és ez okozza a bajt.
Ekkor kiraktam Herokura remélve hogy így már jól fog működni, viszont sajnos teljesen eltörtem az appot és most azt az üzenetet kapom a mobilon hogy ```WebSocketException: Connection to 'https://shed-backend.herokuapp.com:0/shed/018/1wnesojm/websocket#' was not upgraded to websocket```
Ennek a kijavítását a jövő hétre hagyom, remélhetőleg könnyen megjavul, mivel már futottam bele ebbe a hibába korábban és valahogy megoldottam.

## 5. hét
Ezen a héten megfejtettük, hogy a websocket eltörését mi okozza.
Ebben nagy segítséget nyújtott az, hogy Herokun megtaláltuk a logok helyét, ahol azt láttuk, hogy a "/login" endpoint elérésekor a JWT tokenre panaszkodik a szerver, aminek annál a fázisnál még nem kellene szóba jönnie.
Rájöttünk hogy a regisztráció rosszul lett megírva, úgyhogy azt átírtuk és így már működött a login. Ekkor visszakaptuk a megfelelő Bearer tokent, amit így már be tudtunk adni a websocket fejlécébe, aminek hatására újra rendeltetésszerűen működött a szerver.
Elkészítettem a mobil appon egy töltő képernyőt, ami megkérdezi a szervertől, hogy a bejelentkezéskor shared_preferences-be lementett token érvényes-e még és ettől függően tovább irányít 1) egy login/register opciók közül választást kínáló oldalra, 2) az "üdvözlő" képernyőre, ahol választhatunk hogy mit akarunk a továbbiakban csinálni bejelentkezett felhasználóként.

## 6. hét
Elkezdtem haladni a webes résszel is, aminél egy problémába akardam már az elején. Nem volt konfigurálva a CORS a backenden, ezért webről nem tudtam hívásokat küldeni a backend felé.
Ahhoz, hogy ez megoldódjon, követtem a leírást a következő oldalon:
[```https://spring.io/guides/gs/rest-service-cors/```](https://spring.io/guides/gs/rest-service-cors/)
A globális beállítást választottam és így már működött az API hívás, meg tudtam jeleníteni egy új játék nevét és az ahhoz tartozó QR kódot a képernyőn.

## 7. hét
Megcsináltam, hogy a becsatlakozott játékosok lekérik az addig belépett playerek listáját, onnantól kezdve meg websocketen keresztül érkező join illetve leave üzenetek hatására frissítik a lobby megjelenését.
game-start üzenet hatására átnavigálnak a játék képernyőjére.

## 8. hét
Ezen a héten megcsináltam egy one time password rendszert az alkalmazáshoz, ami azt teszi lehetővé, hogy a regisztráló játékosok csak az emailben megkapott kód beírása után lesznek csak ténylegesen elmentve.
Ezek a jelszók a szerveren vannak egy cache tárolóban, ami úgy működik, hogy általam megadott ideig (5 perc) vannak tárolva, aztán kitörlődnek automatikusan, így a felhasználónak ennyi ideig van lehetősége felhasználni azt saját maga azonosításához.
Ehhez még szépítettem is az email küldő funkcióban lévő email templatet, hogy barátságosabban nézzenek ki az emailek.
Mobilon megcsináltam a bekért inputok validációját, ehhez létrehoztam egy külön service-t, valamint a hibaüzeneteket mutató popupot is elkészítettemm így lesz visszajelzés a usereknek arról, hogy miért akadnak el pl. a regisztrációban.

## 9 & 10. hét
A mobilos regisztrációkhoz tartozó one time password képernyőt megcsináltam dizájnosra és beraktam egy jelszó újraküldésre alkalmas gombot, amit egy percenként tudnak újra használni a felhasználók, ezzel levéve a terhelést a szerverről.
Megcsináltam weben a játékok böngészését, ami úgy működik, hogy pár másodperces rendszerességgel lekéri a kliens az új játékokat a szervertől és frissíti a listát.
A mellette lévő szabály beállíthatóságot is megcsináltam, így mostmár olyan játékot tudunk létrehozni amilyet csak akarunk sliderek, switchek meg dropdown buttonok segítségével.
A QR képernyő készítése közben eldöntöttem hogy kezdek valamit azzal a problémával, hogy a hot reloadnál teljesen újratöltődik az alkalmazás és még a legkisebb változtatások megnéséséért is újra létre kell hoznom egy játékot, ami sok időt vesz igénybe.
Főleg azért akartam minél hamarabb megoldani ezt, mert a játék képernyőn dolgozva egyre nehezebb lesz ilyen formájában tesztelni a kinézetbeli változtatásokat.
Nem igazán találtam erre megoldást az interneten, de egy megoldás volt, aminél egyrész egértettem hogy a hiba az, hogy a routingomban pont a QR képernyőnél argumentumokat passzolok át a képernyőnek, ami miatt nem a sima routingot használtam, hanem az ongenerateroute-ot, viszont nem állítottam be a settings-t az új képernyőnél, ezért az összes oldalt a create game URI-val jelenítette meg.
[```https://stackoverflow.com/questions/62442045/page-url-not-changing-in-flutter-but-the-page-content-changes-fine```](https://stackoverflow.com/questions/62442045/page-url-not-changing-in-flutter-but-the-page-content-changes-fine)
Ezt megjavítva most van egy workaround megoldásom a problémára, ami úgy néz ki, hogy ha hot reloadolok akkor ott marad a képernyő, csak a képernyő argumentek nullázódnak, ami miatt le kell kezelnem ezeket az értékeket.
Egyelőre a játék képernyőnél nem tudom hogy fogom ezt megoldani mert feltehetően csomó adat lesz ott amiket nem akarnék placeholderekkel helyettesíteni.
