mucel-scala
===========
Mucel can be run in Intellij IDEA or directly by SBT.
The first step is to clone this repository.
```bash
git clone https://github.com/davips/mucel-scala
```

Intellij IDEA
-------------
Open the project folder.
Find the Main.scala file (Ctrl+n).
Run the Main.scala file (Ctrl+Shift+F10).

SBT
---
Install SBT
```bash
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
sudo apt-get update
sudo apt-get install sbt
```

Run mucel-scala
```bash
cd mucel-scala
sbt run
```
