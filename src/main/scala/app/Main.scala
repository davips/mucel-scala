package app

import java.awt.Color
import javax.swing.JFrame
import data.Cfg
import view.Animation

object Main extends App {
  def run() {
    val anima = Animation(1 / 30d)
    val frame = new JFrame()
    frame.getContentPane.add(anima)
    frame.setBackground(Color.gray)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(Cfg.frameWidth, Cfg.frameHeight)
    frame.setVisible(true)
    while (true) {
      anima.update()
      frame.repaint()
    }
  }

  run()
}
