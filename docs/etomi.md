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

## 6. hét
Ezen a héten a websocketes problémával ment el a legtöbb idő.
Egy backend probléma volt első sorban azon belül is a jwt tokenes authentikációnak sajátitottuk de miután átirtuk kiderült hogy nemcsak itt rontottuk el hanem a frontenden is volt baj.
A backenden ezen kivül egy-két dolgot refactoráltam,hibákat javítottam(modeleknél controllereknél,serviceknél is) és elkezdtem írni egy kezdetleges játék logikát amivel létre tudunk hozni egy játékot azokhoz tudunk játékosokat csataloztatni.
Ezt viszont én sajnálatos módon nem tudtam tesztelni mert a postman hibát dobott Connect Econrefused néven amire nem tudtam megoldást találni.
A cache-es megoldástól még kicsit tartozkodtam mert még nem teljesen értem a műkődését.

## 7. hét
Ezen a héten a munka CORS konfigurációval kezdődött , mivel Tominak a frontenden(weben) nem mükődött az egyik endpoint(Corsos hibát kapott) ,így ezt a problémát gyorsan orvosoltuk WebSecurityConfigban.
Refaktoráltam egy két response üzenetet és így már nem 10000 soros JSON - ket adok vissza egy-egy endpointon.
Backenden megtanultam hogy kell rendesen Enumokkal dologozni ebben ez a [tutoriál](https://thorben-janssen.com/jpa-21-type-converter-better-way-to/) segített
A Websocketet elkezdtük csinálni de rájöttünk hogy még nem igazán értjük hogy is műkődik igazán de egy [tutoriál](https://medium.com/swlh/websockets-with-spring-part-3-stomp-over-websocket-3dab4a21f397) alapján megértettük és elkezdtük a mi use case-ünkre megvalósítani.
Mostmár tudunk rajta keresztül üzenetet küldeni a backendre amire reagál is backend.

## 8.hét
Ez a hét refaktorálással kezdödőtt minden szinten.
A modelekből kiszedtem a függvényeket és áthelyeztem őket a service rétegbe.
A service rétegben a dto-kat kiszedtem és függvényeket áthelyeztem amik nem ahhoz az osztályhoz tartoztak
Controller szinten egységesítetem a kivételkezelést és mostmár csak az ő felelősége a dto-k kezelése.
Transactional annotáciokat elkezdtem használni a service rétegben.
Mostantól email cím is kell a regisztráciohoz és ha valaki regisztrált akkor kap egy emailt,hogy sikeres volt.
Tomi ezt tovább fejlesztette és mostmár kapnak egy OneTimePasswordot emailben a felhasználók amit be kell majd irniuk frontenden hogy sikeres legyen a regisztráció.
A nem használt játékokat és hozzá tartozó rekordokat egy ütemező kitörli majd az adatbázisból így helyet spórolva és pénzt mivel nem kell nagyobb adatbázist vennünk.

## 9 - 10. hét
Tomi kérésére kellett csinálni új endpointokat amivel kényelmesen lehet lekérdezni adatokat, ezeket megcsináltam.
Mostantól csak "vicces nevű" játékok lesznek és azok egészen biztosan egyediek lesznek.
Az összes player service függvényre @Transactional(isolation = Isolation.REPEATABLE_READ) annotációt raktam amivel kiküszöböltük a nem várt viselkedést a lobbyban.
Lapokat már kitudunk osztani a játékosoknak meg az asztalra ennek implementálásának során felmerült egy olyan probléma amit elkerültem volna legszívesebben ez pedig a körkörös dependency injection serviceknél.
Ezt sok refaktorálással és a kód minőség romlásása(playerServiceben használnom kell a gameRepositoryt) árán tudtam megoldani.
Egy olyan bug is előkerült ,hogy ha csatlakoztatok 3 játékost egy játékhoz(2-nél még nem volt ilyen probléma) akkor a game.deck listának a mérete megnött teljesen érthetlen okból mivel a kódból nézve ennek nem kéne megtörtténie.
Szerencsére az adatbázisban jól vannak a cardconfigok így azokat újra lekérve betudtam állítani a gamenek a deckjét arra aminek kéne lennie alapból is.
Összes Enumot megcsináltam a [tutoriál](https://thorben-janssen.com/jpa-21-type-converter-better-way-to/) alapján.

## 11.hét
Ezen a héten elkezdtem a szabályokat implementálni a játék logikába ezt strategy mintával tettem meg.
Ennek során felmerült egy olyan probléma hogy elkéne tárolni valahol a kártyák sorrendjét a paklikon belül.
Erre két megoldáson gondolkozok egyik a szerver memoriájában vagy backenden egy külön oszlopot szentelnék ennek.
Végül ugy döntöttem hogy backenden lesz ennek külön sor de miközben ezen gondolkoztam rájöttem hogy a TableCard meg PlayerCard táblákban,
ott az id(amit az adatbázis generál), amit használhatok sorrendre és ezekbe a táblákba már összekeverve kerülnek majd be a kártyák.
Egy olyan probléma is felmerült hogy háromszor probált csatlakozni egy játékos és háromszor is mentettem el az adatbázisba annak ellenére hogy REPEATABLE_READ izolációs szinten volt,
ezt felemeltem SERIALIZABLE szintre ez megoldotta a backenden a problémát ezután frontenden is orvosoltuk a hibát, amiért 3x-or probált csatlakozni.