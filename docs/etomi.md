# Haladási napló
# 1. hét
Elkészítettük a [specifikációt](https://github.com/pintertamas/shed-backend/blob/master/docs/specifikacio.pdf) amiben minden fontos dolgot leírtunk ami a projekthez tartozik,ennek készítése során is felmerült a kérdés, hogy websocket-et vagy mqtt-t 
használjunk majd, de végül az mqtt mellett tettük le a voksunkat, hosszabb utánanézést követően.
Emelett megbeszéltük, hogy hogyan fogjuk szétosztani a munkát az első pár héten.
A terv az, hogy amíg nincsen kész az alap kommunikációt biztosító backend, addig közösen dolgozunk rajta, majd ha elkészültünk vele és képesek leszünk bármiféle információt átadni rajta, elkezdjük a mobil kliens fejlesztését is.
Előre láthatólag én fogok többet foglalkozni a backenddel, P.Tomi pedig a Flutteres résszel, viszont mindketten fogunk mindkét oldallal foglalkozni, a feladatokat pedig taskboard használatával fogjuk elosztani.
A következő lépés az volt,hogy megterveztük a program adatmodeljét(lent látható) amihez a draw.io-t használtuk.
Adatmodel: ![adatmodell](https://github.com/pintertamas/shed-backend/blob/master/docs/data_model_1.png?raw=true)

# 2. hét
A levelezős konzultáció után megpróbáltunk egy rendes [ER diagramot](https://github.com/pintertamas/shed-backend/blob/master/docs/data_model_2.png?raw=true) készíteni és frissítettük a [specifikációt](https://github.com/pintertamas/shed-backend/blob/master/docs/specifikacio2.pdf), így már látható a játék részletes szabálya és el is képzelhető, hogy a felhasználók mit fognak látni, mikor játszanak a játékkal. Ehhez csináltunk látványtervet is, ami alább látható.
[!insert pictures here]()
Kicsit jobban utánanéztünk, hogy hogyan tudnánk a gyakorlatban használni a RabbitMQ-t és arra jutottunk hogy mégiscsak a websocketes megközelítés valószínűleg jobb lesz a kétirányú kommunikáció miatt számunkra.
Megcsináltunk egy Spring alapú websocketes kommunikációt lehetővé tevő alkalmazást és Flutterben is csináltunk hozzá egy kezdetleges demo alkalmazást.
Csináltunk egy SSL Certificatet az alábbi [tutorial](https://www.thomasvitale.com/https-spring-boot-ssl-certificate/) segítségével, így már HTTPS-t használ az alkalmazásunk.
