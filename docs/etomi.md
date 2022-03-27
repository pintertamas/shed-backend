# Haladási napló
## 1. hét
Elkészítettük a [specifikációt](https://github.com/pintertamas/shed-backend/blob/master/docs/specifikacio.pdf) amiben minden fontos dolgot leírtunk ami a projekthez tartozik,ennek készítése során is felmerült a kérdés, hogy websocket-et vagy mqtt-t 
használjunk majd, de végül az mqtt mellett tettük le a voksunkat, hosszabb utánanézést követően.
Emelett megbeszéltük, hogy hogyan fogjuk szétosztani a munkát az első pár héten.
A terv az, hogy amíg nincsen kész az alap kommunikációt biztosító backend, addig közösen dolgozunk rajta, majd ha elkészültünk vele és képesek leszünk bármiféle információt átadni rajta, elkezdjük a mobil kliens fejlesztését is.
Előre láthatólag én fogok többet foglalkozni a backenddel, P.Tomi pedig a Flutteres résszel, viszont mindketten fogunk mindkét oldallal foglalkozni, a feladatokat pedig taskboard használatával fogjuk elosztani.
A következő lépés az volt,hogy megterveztük a program adatmodeljét(lent látható) amihez a draw.io-t használtuk.
Adatmodel: ![adatmodell](https://github.com/pintertamas/shed-backend/blob/master/docs/data_model_1.png?raw=true)

## 2-3 hét
A levelezős konzultáció után megpróbáltunk egy rendes [ER diagramot](https://github.com/pintertamas/shed-backend/blob/master/docs/data_model_2.png?raw=true) készíteni és frissítettük a [specifikációt](https://github.com/pintertamas/shed-backend/blob/master/docs/specifikacio2.pdf), így már látható a játék részletes szabálya és el is képzelhető, hogy a felhasználók mit fognak látni, mikor játszanak a játékkal.
Kicsit jobban utánanéztünk, hogy hogyan tudnánk a gyakorlatban használni a RabbitMQ-t és arra jutottunk hogy mégiscsak a websocketes megközelítés valószínűleg jobb lesz a kétirányú kommunikáció miatt számunkra.
Megcsináltunk egy Spring alapú websocketes kommunikációt lehetővé tevő alkalmazást és Flutterben is csináltunk hozzá egy kezdetleges demo alkalmazást.
Csináltunk egy SSL Certificatet az alábbi [tutorial](https://www.thomasvitale.com/https-spring-boot-ssl-certificate/) segítségével, így már HTTPS-t használ az alkalmazásunk.

## 4-5. hét
Az elmúlt két alatt nagyon sok mindent csináltunk.Először is elkészitettünk(rajzoltunk) egy adatbázis sémát [paintben](https://github.com/pintertamas/shed-backend/blob/master/docs/datamodel_sketch.png?raw=true) és [draw.io-n](https://github.com/pintertamas/shed-backend/blob/master/docs/db_plan.png?raw=true) is, ami alapján már tudtunk normális adatmodelt készíteni.
Az adatmodelt a két hét alatt nagyon sokat változtattuk, de úgy érzem az [aktuális verzió](https://github.com/pintertamas/shed-backend/blob/master/docs/data_model_4.png?raw=true) lesz az utolsó módósítás.Ez a sok modositás azért volt fájó mivel a backenden mindig át kellett irni a már meglévő modelleket és a felsőbb rétegekbe is bele kellett néha nyúlni.
A backenden elkészítettem a modelleket illetve néhány service-t és controller-t is megírtam már. Tudunk már regisztrálni és bejelentkezni is már ezekhez jwt tokenes megoldást használunk.
Problémát jelentett a kapcsolatok jelőlése erre a konzin remelém majd tudok választ kapni hogy ezek megfelelnek-e így.
Adatbázisnak először mongodb-t használtunk mert témalabról ezzel már van tapasztalatunk de hosszas tanakodás után átáltunk sql alapú adatbázisra(postgresql) mivel szerétnek ezzel is megismerkedni.
Ezek mellett elkészítettük a projekt [mockupját](https://github.com/pintertamas/shed-backend/blob/master/docs/mockup.png?raw=true) webre és mobilra is.

## 6 hét
Ezen a héten a websocketes problémával ment el a legtöbb idő.Elöször egy backend problémának hittük azon belül is a jwt tokenes authentikációnak sajátiottuk de miután átirtuk kiderült hogy rossz helyen keressük a megoldást a problémára mivel a frontenden volt a probléma.
A backenden ezen kivül egy két dolgot refactoráltam,hibákat javítottam(modeleknél controllereknél,serviceknél is) és elkezdtem írni egy kezdetleges játék logikát amivel létre tudunk hozni egy játékot azokhoz tudunk játékosokat csataloztatni. A cache-es megoldástól még kicsit tartozkodtam mert még nem teljesen értem a műkődését.