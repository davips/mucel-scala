# mucel-scala
Minimalist multicellular simulator.

Install
=======
Mucel can be run in Intellij IDEA or directly by SBT.
These instructions were tested on Debian Jessie.
The first common step is to clone this repository.
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
Features / To do list
=====================
* [x] 2D particle collision simulator
* [x] visualize according to real elapsed time
* [x] exact preemptory collision calculation
* [ ] rigid bodies
* [ ] rotation
* [ ] different types of cells
       * [x] structure
       * [ ] sensor
       * [ ] bulb
       * [ ] motor
       * [ ] wire
* [ ] reproduction
* [ ] mov. quantity proportional to area
* [ ] friction?
* [ ] mobile version
* [ ] distributed version
* [ ] GPU headless version
* [ ] 3D version?
