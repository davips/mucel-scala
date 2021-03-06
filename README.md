# mucel
Minimalistic multicellular simulator.
The idea here is similar to the Game Of Life, i.e. a minimal set of rules sufficient to generate life-like behavior.
In the present case, the world is also 2D but not discrete.
 

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

Screenshot
-------------
![Image](https://github.com/davips/mucel-scala/blob/master/shot.png)



Features / To do list
=====================
* [x] 2D collision simulator
* [x] real elapsed time visualization 
* [x] exact preemptory calculation
* [x] different types of cells:
       * [x] bubble (membrane)
       * [x] sensor, bulb, motor, wire
       * [x] ghost (only for internal collisions)
       * [ ] cell eater?
       * [ ] hit absorver?
       * [ ] add nonpermanent bulbs to organisms (ignites like a motor)
* [x] sample organisms:
       * [x] planarian (runs away from light)
       * [ ] moth (guided by light)
       * [ ] flea (runs towards light)
* [ ] reproduction (how to combine organisms?)
       * [ ] egg cell? (when two egg cells hit, create an averaged organism)
       * [ ] reproduction gate?       
* [ ] improve plausibility of friction
* [ ] organism editor
* [ ] energy dissipation on hit? (where does the heat go?)
* [x] improve performance
       * [x] parallelize
       * [ ] organize each organism cells in some way to avoid n\*n tests
       * [ ] optimize by caching or avoiding redundant calculations
* [x] versions
       * [ ] desktop
       * [ ] server headless       
       * [ ] distributed
       * [ ] GPU headless       
       * [ ] mobile
       * [ ] 3D
