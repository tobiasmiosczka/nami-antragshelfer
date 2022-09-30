NaMi-Antragshelfer
======================================================================
[![Java CI with Maven](https://github.com/tobiasmiosczka/nami-antragshelfer/actions/workflows/maven.yml/badge.svg)](https://github.com/tobiasmiosczka/nami-antragshelfer/actions/workflows/maven.yml)

Der NaMi-Antragshelfer ist ein Programm zum Ausfüllen von Förderungsanträgen und Generieren von Notfall-Telefonlisten für Pfadfinderstämme der [DPSG][1]. Die benötigten Mitgliederdaten können aus der [NaMi][2]-Datenbank heruntergeladen und mithilfe einer einfachen Benutzeroberfläche ausgewählt werden. 
![Benutzeroberfläche der Version 4.0](https://raw.githubusercontent.com/TobiasMiosczka/NaMi/master/pictures/preview.PNG "Benutzeroberfläche der Version 4.0")

Download
----------------------------------------------------------------------
Die aktuelle Version des NaMi-Antragshelfers kann [hier][3] heruntergeladen werden.

Systemvoraussetzungen
----------------------------------------------------------------------
Zum Ausführen des NaMi-Antragshelfers wird eine Installation des Java Development Kit (JDK) 18 benötigt.
Die kostenlose Version des JDKs (OpenJDK) gibt es [hier][4].

Unterstützte Anträge & Listen
----------------------------------------------------------------------
* Diözesanverband Münster
  * Förderungsantrag and den Diözesanverband Münster
  * Anmeldung Freizeit
* Stadt Dinslaken
  * Förderungsantrag an BDKJ Dinslaken
  * Dokumentation der Verordnung zum Schutz vor Neuinfizierungen mit dem Coronavirus SARS-CoV-2
* Listen
  * Notfall-Telefonliste
  * Anwesenheitsliste
  
Danksagung
----------------------------------------------------------------------
Ein besonderer Dank geht an [Fabian Lipp][5] für die Veröffentlichung der ursprünglichen Klassen des NaMi-Connectors.

Kompilierung
----------------------------------------------------------------------
Zur Kompilierung benötigt das Projekt JDK 18.
Die Kompilierung und Paketierung erfolgt über die folgenden Maven-Befehle:
```
mvn clean package
```

Changelog
----------------------------------------------------------------------
Alle Änderungen sind in [CHANGELOG.md][6] dokumentiert.


Lizenz
----------------------------------------------------------------------
Der Code unterliegt der GNU GPL. Der vollständige Lizenztext befindet sich in [LICENSE][7].


```
Copyright (C) 2013 - 2022 Tobias Miosczka

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
```
[1]: https://dpsg.de/
[2]: https://nami.dpsg.de/
[3]: https://github.com/tobiasmiosczka/nami-antragshelfer/releases/latest
[4]: https://adoptium.net/de/temurin/releases/?version=18
[5]: https://github.com/fabianlipp
[6]: https://github.com/TobiasMiosczka/NaMiAntragshelfer/blob/master/CHANGELOG.md
[7]: https://github.com/TobiasMiosczka/NaMiAntragshelfer/blob/master/LICENSE
