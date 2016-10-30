# mucel
Minimalist multicellular simulator.

Install
=======
Mucel can be run in Intellij IDEA or directly by SBT.
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
Install [SBT](http://www.scala-sbt.org/index.html).
Or, for Debian Jessie and similar distros:
```bash
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
sudo apt-get update
sudo apt-get install sbt
```

Run mucel:
```bash
cd mucel-scala
sbt run
```
Features / To do list
=====================
* [x] 2D particle collision simulator
* [x] visualization according to real elapsed time
* [x] exact preemptory collision calculation
* [x] each organism cells grouped inside an ivisible bubble
* [ ] organize each organism cells in some way to avoid n\*n tests
* [ ] different types of cells
       * [x] solid
       * [ ] sensor
       * [ ] bulb
       * [ ] motor
       * [ ] wire
* [ ] energy dissipation on hit
* [ ] reproduction
* [ ] organism editor
* [ ] friction
* [x] improve performance
       * [x] parallelize
       * [ ] optimize by caching or avoiding redundant calculations
       * [ ] distributed
       * [ ] GPU headless
* [ ] other versions
       * [ ] mobile
       * [ ] 3D
