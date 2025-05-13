🙎 Uživatelská dokumentace

📌 Orientace v menu
Po spuštění se objevíme v menu, kde jsou tři možnosti - start, load, exit.

Po stisknutí tlačítka start se nám zobrazí dialog pro vybrání levelu. Kliknutím se daný level vybere a tlačítko OK ve spod obrazovky nás přesune do hry.

Stisknutí tlačítka load zobrazí dialog s již uloženými hrami. U každého uložení můžeme vidět, jak se jmenuje (to se nastavuje při ukládání hry), dále vidíme datum a čas kdy bylo uložení vytvořeno a také jméno mapy pro kterou byla hra uložena. Kliknutím na nějaké uložení a stisknutí load ve spod obrazovky nás přenese do hry s progresem z uložení.

Tlačítko exit hru ukončí.

🏁 Průběh a cíl hry
Hráč objeví ve spod mapy. Skákáním po platformách se dostává do vyšších úrovní mapy. Ovšem to že se hráč dostane nahoru neznamená, že nemůže spadnout opět úplně dolů na začátek mapy. 🤬
Cílem je vyskákat na vrch mapy, kde se nachází princezna, kterou musí hráč vysvobodit a tím hru ukončit.

Pro ukončení hry je potřeba mít sesbírané všechny klíče na mapě. Sebrání klíče je provedeno automaticky jakmile se hráč ke klíči přiblíží. V pravém dolním rohu je zobrazeno, kolik máme aktuálně sebraných klíčů a kolik jich je celkem potřeba.

Na mapě se zobrazují vylepšení, která nám mohou pomoci s překážkami. Po sebrání vylepšení se nám zvětší výška a délka skoku. Vylepšení trvá 10 sekund, poté je deaktivováno. Při aktivním vylepšení můžeme vidět v levém dolním rohu kolik nám zbývá sekund do deaktivace.


⏹️ Pauzové menu
Při vyvolané pauze hry, se nám zobrazí tři tlačítka: "Resume", "Save and exit" a "Exit".
Stisknutí save and exit nám otevře dialog kam zadáme jméno uložení hry a hra se uloží a zavře.


🕹️ Ovládání
Arrow left - pohyb/skok doleva
Arrow right - pohyb/skok doprava
Space - skok, čím delší stisknutí, tím větší skok, šipka doleva/doprava nastaví směr skoku
Esc - zastavení/spuštění hry, otevření/zavření pauzového menu
E - ukončení hry, když je hráč v blízkosti princezny
N - přeskočení na další level (určené pro testování)

🖋️ Logování
Logování je ovládáno pomocí argumentů při spuštění aplikace. Je možné ovládat úroveň logování nebo logovat do souboru.

Argumenty pro logování
--log-enable - zapnutí logování
--log-level=severe/warning/info/fine - nastavení úrovně logování, servere = pouze nejkritičtější logy - fine = nejdetailnější logování
--log-file - zapnutí logování do souboru, soubor s logy se objeví ve složce logs


🗺️ Tvorba vlastních map

Obecné informace
Mapa je definovaná v souboru JSON. Šířka hry je 1200px, výška je 900px.
Mapy se nacházejí ve složce maps . Pro vytvoření vlastního levelu je potřeba vytvořit podsložku ve složce maps, jméno této složky bude jméno mapy, které se bude zobrazovat ve hře. V této vytvořené složce vytvořte soubor map_data.json (soubor se musí jmenovat přesně takto), v tomto souboru budou všechna data mapy.


Struktura souboru
Samotný soubor je strukturován takto:

"levels" (1)

"id"
"platforms"
"keys"
"powerUp"


"keysStats" (2)

"allCount"
"collected"


"playerStart" (3)

"x"
"y"


"levelStats" (4)

"levelCount"
"startingLevel"




(1) Levels
V objektu levels se nachází definování všech platforem, klíčů a vylepšení. Levelů může být libovolné množství, od sedmého levelu se ale pozadí začne opakovat, je tedy doporučené mít levelů právě sedm.

Takto vypadá jeden level, musí zde vždy být všechny položdy: id, platforms, keys, powerUp
id:

číslo levelu
začíná od 1, každý další level má o jedna větší než ten předchozí

platforms:

list objektů, každý objekt je jedna platforma

x, y - pozice horního levého kraje platformy
x = 0 levý kraj pole, y = 0 horní kraj pole

width, height - šiřka a výška platformy

type - typ platformy, ovlivňuje to vzhled platformy, možnosti jsou: dirt, stone, cloud, wood, ice

keys:

list, který definuje, jaké klíče jsou na daném levelu

x, y - pozice klíče na mapě (stejné jako u platformy)

id - pořadí klíče, začíná od 1, počítá se celkově, ne pouze v rámci daného levelu - tzn. v dalším levelu by byl klíč s id = 3
pokud nechceme žádné klíče na daném levelu, necháme prázdný list

powerup:

vše stejné jako u keys


end:

u posledního (ne nutně posledního, ale tam kde chceme konec) levelu je objekt end, který značí princeznu, tedy konec hry
x, y - pozice x, y stejně jako u platformy


(2) keysStats
Tento objekt dává statistiku o klíčích
allCount:

počet všech klíčů, musí se shodovat s počtem definovaných klíčů v levels


collected:

list již sebraných klíčů
je zde možné definovat nějaké již sebrané klíče ze začátku (je potřeba uvést jejich id)



(3) playerStart
Objekt definuje na jakých souřadnicích se hráč bude nacházet ze začátku hry.
x, y:

x-ová a y-ová souřadnice hráče na poli, stejné jako u platformy



(4) levelStats
Definování informací o levelech na mapě
levelCount:

celkový počet levelů, musí se shodovat s početem definovaných levelů v objektu levels


startingLevel:

id levelu na kterém bude hráč startovat, nemusí být vždy nastaven na 1, např. chceme, aby hráč musel seskákat dolů pro nějaký klíč



Možné problémy s vytvářením levelů

id u levelů, klíčů a vylepšení nejsou správně definována, nevytváří správnou řadu
hodnota celkového počtu levelů nebo klíčů nesedí s jejich skutečným definovaným počtem
hráč by neměl mít startovní pozici uvnitř nějaké platformy
klíče a vylepšení by neměli být uvnitř platformy, není je pak možné sebrat
využití nedefinovaného typu platformy
x-ová a y-ová pozice všech objektů by měla být mýt v rámci definovaného pole (x: 0-1200, y: 0-900)
pokud se na daném levelu nenachází žádný klíč nebo vylepšení, stejně musí být prázdné pole pro keys nebo powerup definované v levelu
hra musí obsahovat konec na některém z levelů
platformy musí být rozmístěny tak aby po nich bylo možné vyskákat

Template pro tvorbu levelů:
