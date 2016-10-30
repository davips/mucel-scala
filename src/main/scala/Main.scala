//based loosely on http://www.java2s.com/Code/JavaAPI/java.awt/GraphicsdrawImageImageimgintxintyImageObserverob.htm

import java.awt.Color
import javax.swing.JFrame

import breeze.linalg.DenseVector
import things.{HWall, VWall}

import scala.util.Random

object Main extends App {
  val seed = 41
  val rnd = new Random(seed)

  def run() {
    val (frameWidth, frameHeight) = (600, 600)
    val frame = new JFrame()
    val walls = Seq(HWall(frameHeight / 2d), HWall(-frameHeight / 2d), VWall(frameWidth / 2d), VWall(-frameWidth / 2d))
    val world = World(walls, Seq.fill(4)(Bio.newOrg(rnd, walls)))

    val anima = Animation(1 / 30d, frameWidth, frameHeight, world)
    frame.getContentPane.add(anima)
    frame.setBackground(Color.gray)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(frameWidth, frameHeight)
    frame.setVisible(true)
    while (true) {
      anima.update()
      frame.repaint()
    }
  }

  run()
}
