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
A levelezős konzultáció után megpróbáltunk egy rendes [ER diagramot](https://github.com/pintertamas/shed-backend/blob/master/docs/data_model_2.png?raw=true) készíteni és frissítettük a [specifikációt](https://github.com/pintertamas/shed-backend/blob/master/docs/specifikacio2.pdf), így már látható a játék részletes szabálya és el is képzelhető hogy a felhasználók mit fognak látni mikor játszanak a játékkal. Ehhez csináltunk látványtervet is, ami alább látható.

[!insert pictures here]()

Kicsit jobban utánanéztünk hogy hogyan tudnánk a gyakorlatban használni a RabbitMQ-t és arra a döntésre jutottunk hogy a websocketes megközelítés valószínűleg találóbb lesz a kétirányú kommunikáció miatt.
Megcsináltunk egy Spring alapú websocketes kommunikációt lehetővé tevő alkalmazást és Flutterben is csináltunk hozzá egy demo alkalmazást amivel websocketen lehet küldeni és fogadni üzeneteket.
Csináltunk egy SSL Certificatet az alábbi [tutorial](https://www.thomasvitale.com/https-spring-boot-ssl-certificate/) segítségével, így már HTTPS-t használ az alkalmazásunk.
