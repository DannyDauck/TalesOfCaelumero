# Tales Of Caelumero
### Danny Dauck

<p>
<img src="img/img.jpeg" alt=""/>
</p>
<summary> <b> About </b> </summary>
<br>
Tales of Caelumero ist die Abschlussarbeit des Kotlin-Moduls meiner schulischen Ausbildung zum Junior Mobile Developer.

Es handelt sich um ein Online-Trading-Card-Rpg im MVVM-Pattern, das sowohl eine Room- als auch Firebase-Datenbank
verwendet. Alle Karten, Spieler und GlobalChat-Messages befinden sich erstmal in der Firebase-Datenbank. Die Room-Datenbank speichert Daten
lokal und aktualisiert z.B. Kartendaten automatisch beim Start der App, wenn eine neue Karte hinzugekommen ist, um Anfragen
an die Firebase-Datenbank drastisch zu reduzieren. 

Die enthaltenen Chibi-Charakter und Images sind größtenteils mit gencraft (Bild-KI) erstellt. UI-Elemente wie Buttons oder Navigationbar
sind häufig selbst erstellte Vector-Grafiken.

Ausserdem enthält die App einen GlobalChat.

Die App ist funktionsfähig und man kann sowohl einen Spieler erstellen, als auch gegen andere Spieler im Player vs.Player oder Multibattle-Modus
antreten.

Einige Fragmente sind noch nicht fertig gestellt, wie z.B. das Fragment zum bearbeiten des eigenen Decks, werden aber bald kommen

---
<details>
  <summary> <b> RecyclerViews</b> </summary>
  Die Haupt-RecyclerView stellt die Karten dar und passt sich je nach Kartentyp und Verwendung an. Durch Verwendung des String-Parameters
  "type" kann z.B. umgestellt werden zwischen Einfach-, Mehrfach- oder gar keiner Auswahlmöglichkeit, bestimmte Animation, Treffer oder 
  Healing können ausgelöst werden, etc.
  Sie wird vorallem im BattleFragment mehrfach verwendet.

  Weiter RecyclerViews stellen z.B. ein SpielerItem oder ein BattleItem dar.
  
</details>

---

<details>
<summary><b> API-Call</b></b></summary>

Es ist ein API-Call eingebaut, der die Weltzeit in London abfragt um Manipulation durch User zu verhindern (z.B. beim täglichen kostenlosen Boosterpack). Dies war Anforderung der Prüfung. Mittlerweile ist dieser ausgegliedert und die Anfrage läuft über ein Google-Network-Timeprotocoll, weil dies einfach stabiler und sicherer ist.
  
</details>

---

<details>
<summary><b> Battle System</b></b></summary>

Das Spiel enthält zwei grundlegende Kampfsysteme:

# Player vs. Player

Hier spielt ein Spieler gegen direkt gegen einen anderen. Jeder Spieler hat für jeden Zug 24h Zeit

# Multibattle

Im Multibattle tritt ein Spieler gegen viele andere Spieler an. Man kann an einem Multibattle als Spieler antreten oder als Gegner
einen Zug ausführen.

Als Spieler ist das System im Grunde gleich zu Player vs. Player, tritt man jedoch als Gegner an betritt man Spielfeld in einem bereits
laufenden Spiel und führt einen Zug mit einem zugewiesenen Deck gegen einen Spieler aus. Wenn der Zug abgeschlossen ist, erhält man als
Belohnung InGame-Währung.
  
</details>

---

<details>
  <summary><b> Spezielle Klassen</b></summary>
  # Class ImageLoader

  Die ImageLoader Klasse kommt im SplashScreen zum Einsatz. Sie überprüft, welche Images bereits beim User lokal gespeichert sind und gleicht diese mit
  der Firebase-Datenbank ab. Sie lädt dann automatisch alle Bilder die noch nicht vorhanden sind. Das hat folgendende Vorteile:
  - Anfragen an die Firebase Datenbank werden drastisch reduziert, was natürlich auch Nutzungskosten spart
  - Ausserdem kann die App schnell und einfach um Karten über Firestore erweitert werden, ohne das wirklich ein Update ausgelöst werden muss.

 # Class GradientText

 Die Klasse GradientText ermöglicht es ein eigenes XML-Element zu verwenden, das wie der Name schon sagt, einen Text mit Verlaufsfarbe erstellt
 (ähnlich einer WordArt). Sie kann in einer XML-Datei auch über die Attribute startColor, middleColer und endColor farblich angepasste werden.


 # Class SoundManager

 Die Klasse SoundManager verwaltet sowohl einen Soundpool für VFX-Sounds als auch einen MediaPlayer für Hintergrund-Musik. Sie ist als Singleton-Klasse
 eingerichtet

 
</details>

---

<details>
  <summary><b> Besonderheiten der App</b></summary>
  
  # InGame-Shop

  Der User hat im Spiel die Möglichkeit überschüssige Karten gegen InGame-Währung zu verkaufen und im InGame-Shop dafür spezielle Booster oder
  einzelne Karten zu kaufen

 # PlayStore-Shop

 Es gibt auch einen PlayStore-Shop, der die Möglichkeit bietet Booster-Packs oder einezelne Karten gegen Geld zu kaufen. Die Funktionen dafür sind
 integriert und funkltionieren auch, bis jetzt kostenlos.
 Das Billing-System ist noch nicht eingebunden, da die App noch nicht bei Google registriert ist und wird in zukunft auch eingestzt werden.


 # Anpassungsfähiger Hintergrund

 Der Hintergrund im HomeScreen und einigen anderen Fragmenten erzeugt durch 2 überlagernde Elemente einen 3D-Effekt, ähnlich wie bei einem
 Parallax-Turning-Screen.

 Er passt das Image je nach Zeit in der sich der User einlogged an

 # Global Chat

 Das Spiel enthält einen Global Chat der die Messages nach Sprache des Users filtert. Er ist recht einfach aufgebaut:

 In der Firebase-Datenbank wird immer die letzte geschriebene Nachricht gespeichert und lokal im Spiel hinzugefügt, wenn die Sprache
 mit der Sprache des Users übereinstimmt. Da es sich um einen globalen Chat handelt, ist auch immer nur letzte geschriebene Nachricht relevant.

 
</details>

---

<details>
  <summary><b> About the music</b></summary>

  Alle im Spiel enthaltenen Songs sind von mir und meinem Bruder selbst.

  Sie wurden im HomeRecording mit CakeWalk aufgenommen und bearbeitet
  
</details>

---
